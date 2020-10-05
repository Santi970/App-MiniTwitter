package com.example.minitwitter.ui;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.minitwitter.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.ProfileViewModel;
import com.example.minitwitter.ui.profile.ProfileFragment;
import com.example.minitwitter.ui.tweets.NuevoTweetDialogFragment;
import com.example.minitwitter.ui.tweets.TweetListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import retrofit2.http.Url;


public class DashboardActivity extends AppCompatActivity implements PermissionListener {

    FloatingActionButton fab;
    ImageView ivAvatar;
    ProfileViewModel profileViewModel;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment f = null;

            switch (item.getItemId()) {

                case R.id.navigation_home:
                     f = TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL);
                     fab.show();
                    break;
                case R.id.navigation_tweets_like:
                     f = TweetListFragment.newInstance(Constantes.TWEET_LIST_FAVS);
                     fab.hide();
                    break;
                case R.id.navigation_profile:
                    f = new ProfileFragment();
                    fab.hide();

                    break;
            }

            if (f != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContainter, f)
                        .commit();

                return true;
            }

                return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        fab = findViewById(R.id.fab);
        ivAvatar = findViewById(R.id.imageViewToolbarPhoto);

        getSupportActionBar().hide();


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragmentContainter, TweetListFragment.newInstance(Constantes.TWEET_LIST_ALL))
                .commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NuevoTweetDialogFragment dialog = new NuevoTweetDialogFragment();
                dialog.show(getSupportFragmentManager(),"NuevoTweetDialogFragment");

            }
        });


        String photoURL =   SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);
        if(!photoURL.isEmpty()) {
            Glide.with(this)
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoURL)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .centerCrop()
                    .skipMemoryCache(true)
                    .into(ivAvatar);
        }

        profileViewModel.photoProfile.observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String photo) {
                Glide.with(DashboardActivity.this)
                        .load(Constantes.API_MINITWITTER_FILES_URL + photo)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .into(ivAvatar);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (requestCode == Constantes.SELECT_PHOTO_GALERY) {
                if (data != null) {
                    Uri imagenSeleccionada = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(imagenSeleccionada,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int imagenIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String fotoPath = cursor.getString(imagenIndex);
                        profileViewModel.uploadPhoto(fotoPath);
                        cursor.close();
                    }

                }
            }
        }
    }

    @Override
    public void onPermissionGranted(PermissionGrantedResponse response) {

        Intent seleccionarFoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(seleccionarFoto, Constantes.SELECT_PHOTO_GALERY);
    }

    @Override
    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
        Toast.makeText(this, "No se puede seleccionar la fotografia por falta de permisos", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {

    }
}
