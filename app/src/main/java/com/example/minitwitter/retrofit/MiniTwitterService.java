package com.example.minitwitter.retrofit;



import com.example.minitwitter.retrofit.request.RequestLogin;
import com.example.minitwitter.retrofit.request.RequestSignUp;
import com.example.minitwitter.retrofit.response.ResponseAuth;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MiniTwitterService {

    @POST("auth/login")
    Call<ResponseAuth> doLogin(@Body RequestLogin requestLogin);

    @POST("auth/signup")
    Call<ResponseAuth> doSignUp(@Body RequestSignUp requestSignUp);



}
