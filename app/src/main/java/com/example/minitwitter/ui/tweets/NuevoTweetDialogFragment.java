package com.example.minitwitter.ui.tweets;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.common.SharedPreferencesManager;
import com.example.minitwitter.data.TweetViewModel;


public class NuevoTweetDialogFragment extends DialogFragment implements View.OnClickListener {

    ImageView ivColse, ivAvatar;
    Button btnTwittear;
    EditText etMensaje;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NORMAL,R.style.FullScrenDiaglogStyle);   //ponemos el estilo que hicimos en styles.


        }

    @Override       //este metodo nos va a permitir cargar una lista personalizada de nuestro fragmento de dialogo.
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_nuevo_tweet_dialog, container, false);


        ivColse = view.findViewById(R.id.imageViewClose);
        ivAvatar = view.findViewById(R.id.imageViewAvatar);
        btnTwittear = view.findViewById(R.id.buttonTwittear);
        etMensaje = view.findViewById(R.id.editTextMensaje);

        //Eventos click

        btnTwittear.setOnClickListener(this);
        ivColse.setOnClickListener(this);

        //Seteamos la imagen del usuario de perfil
        String photoURL =   SharedPreferencesManager.getSomeStringValue(Constantes.PREF_PHOTOURL);
        if(!photoURL.isEmpty()) {
            Glide.with(getActivity())
                    .load(Constantes.API_MINITWITTER_FILES_URL + photoURL)
                    .into(ivAvatar);
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String mensaje = etMensaje.getText().toString();

        if (id == R.id.buttonTwittear){
            if (mensaje.isEmpty()){
                Toast.makeText(getActivity(), "Debes escribir un texto en el mensaje", Toast.LENGTH_SHORT).show();
            }else {
                TweetViewModel tweetViewModel = new ViewModelProvider
                        (getActivity()).get(TweetViewModel.class);
                tweetViewModel.insertTweet(mensaje);
                getDialog().dismiss();

            }
        }else if (id == R.id.imageViewClose) {
            if (!mensaje.isEmpty()) {
                showDialogConfirm();
            } else {
                getDialog().dismiss();
            }
        }
    }

    private void showDialogConfirm() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setMessage("Desea cancelar el tweet? El mensaje se eliminará")
                .setTitle("Cancelar tweet");

        builder.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                getDialog().dismiss();
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

    }
}