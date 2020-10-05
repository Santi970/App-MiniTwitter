package com.example.minitwitter.ui.tweets;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.minitwitter.R;
import com.example.minitwitter.common.Constantes;
import com.example.minitwitter.data.TweetViewModel;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

public class BottomModalTweetFragment extends BottomSheetDialogFragment {

    private TweetViewModel tweetViewModel;
    private int     idTweetEliminar;   //aca guardamos el argumento que nos llega

    public static BottomModalTweetFragment newInstance(int idTweet) {   //el que invoque al fragmento de newInstance debe pasar el idTweet.
        BottomModalTweetFragment fragment = new BottomModalTweetFragment();   //instanciamos el bottom con el fragment.
        Bundle  args = new Bundle();              //Creamos un bundle para pasar argumentos a ese fragment de arriba
        args.putInt(Constantes.ARG_TWEET_ID, idTweet);    //args de tipo entero, y creamo sen constantes una variable  ARG_TWEET_ID y le pasamos el id de teweet
        fragment.setArguments(args);
        return fragment;              //Devolvermos la referencia al fragmento que instanciamos
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null){   //si es distinto de null es que llego algun parametro.
            idTweetEliminar = getArguments().getInt(Constantes.ARG_TWEET_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_modal_tweet_fragment, container, false);

        final NavigationView nav = v.findViewById(R.id.navigation_view_bottom_tweet);  //gestinamos los eventos click sobre el menu.
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();    //identificamos el elemento donde se hizo click

                if (id == R.id.action_delete_tweet){
                    tweetViewModel.deleteTweet(idTweetEliminar);  //
                    getDialog().dismiss();     //obtenemos la referencia al cuadro de dialogo que se abrio y se cierra.
                    return true;
                }
                return false;
            }
        });

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tweetViewModel = new ViewModelProvider(getActivity()).get(TweetViewModel.class);
    }

}