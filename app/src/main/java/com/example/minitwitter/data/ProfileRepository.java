package com.example.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.MyApp;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.retrofit.AuthTwitterService;
import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.AuthTwitterClient;
import com.example.minitwitter.retrofit.response.ResponseUpLoadFoto;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileRepository {
    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<ResponseUserProfile> userProfile;
    MutableLiveData<String> photoProfile;

    ProfileRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        userProfile = getProfile();
        if (photoProfile == null) {
            photoProfile = new MutableLiveData<>();
        }
    }

    public MutableLiveData<String> getPhotoProfile(){
        return photoProfile;
    }

    public MutableLiveData<ResponseUserProfile> getProfile() {
        if (userProfile == null) {
            userProfile = new MutableLiveData<>();
        }

        Call<ResponseUserProfile> call = authTwitterService.getProfile();
        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });

        return userProfile;
    }

    public void updateProfile(RequestUserProfile requestUserProfile) {
        Call<ResponseUserProfile> call = authTwitterService.updateProfile(requestUserProfile);

        call.enqueue(new Callback<ResponseUserProfile>() {
            @Override
            public void onResponse(Call<ResponseUserProfile> call, Response<ResponseUserProfile> response) {
                if (response.isSuccessful()) {
                    userProfile.setValue(response.body());
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUserProfile> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void uploadPhoto(String photoPath){
        File file = new File(photoPath);
        RequestBody requestBody = RequestBody.create( MediaType.parse("image.jpg"), file);
        Call<ResponseUpLoadFoto> call =  authTwitterService.uploadProfilePhoto(requestBody);

        call.enqueue(new Callback<ResponseUpLoadFoto>() {
            @Override
            public void onResponse(Call<ResponseUpLoadFoto> call, Response<ResponseUpLoadFoto> response) {
                if (response.isSuccessful()){
                    SharedPreferencesManager.setSomeStringValue(Constantes.PREF_PHOTOURL, response.body().getFilename());
                    photoProfile.setValue(response.body().getFilename());

                }else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseUpLoadFoto> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }

}