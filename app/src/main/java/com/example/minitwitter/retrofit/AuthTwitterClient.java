package com.example.minitwitter.retrofit;


import com.example.minitwitter.common.Constantes;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthTwitterClient {
    private static AuthTwitterClient instance = null;
    private AuthTwitterService miniTwitterService;
    private Retrofit retrofit;

    public AuthTwitterClient() {


        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.addInterceptor(new AuthInterceptor());
        OkHttpClient cliente = okHttpClientBuilder.build();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constantes.API_MINITWITTER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(cliente)
                .build();

        miniTwitterService = retrofit.create(AuthTwitterService.class);
    }


    public static AuthTwitterClient getInstance(){
        if (instance == null){
            instance = new AuthTwitterClient();
        }
        return instance;
    }

    public AuthTwitterService getAuthTwitterService(){
        return  miniTwitterService;
    }
}
