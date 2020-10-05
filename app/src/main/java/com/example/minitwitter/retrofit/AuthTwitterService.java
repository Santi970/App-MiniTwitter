package com.example.minitwitter.retrofit;


//Desde aqui tmb vamos a consumir la api.
//Para acceder a este servicio utilizaremos un cliente diferente que incluira el token. PETICIONES PRIVADAS.

import android.app.DownloadManager;

import com.example.minitwitter.retrofit.request.RequestCreateTweet;
import com.example.minitwitter.retrofit.request.RequestUserProfile;
import com.example.minitwitter.retrofit.response.ResponseUpLoadFoto;
import com.example.minitwitter.retrofit.response.ResponseUserProfile;
import com.example.minitwitter.retrofit.response.Tweet;
import com.example.minitwitter.retrofit.response.TweetDeleted;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AuthTwitterService {


    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweets(@Body RequestCreateTweet requestCreateTweet);

    @POST("tweets/like/{idTweet}")
    Call<Tweet> likeTweet(@Path("idTweet")int idTweet);

    @DELETE("tweets/{idTweet}")
    Call<TweetDeleted> deleteTweet(@Path("idTweet") int idTweet);


    @GET("users/profile")
    Call<ResponseUserProfile> getProfile();

    @PUT ("users/profile")
    Call<ResponseUserProfile>  updateProfile(@Body RequestUserProfile requestUserProfile);

    @Multipart
    @POST("users/uploadprofilephoto")
    Call<ResponseUpLoadFoto> uploadProfilePhoto(@Part("file\"; filename=\"photo.jpg\" ") RequestBody file);

}
