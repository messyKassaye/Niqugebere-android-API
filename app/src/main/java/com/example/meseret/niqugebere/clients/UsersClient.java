package com.example.meseret.niqugebere.clients;



import com.example.meseret.niqugebere.model.LoginUser;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.model.Users;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Meseret on 11/28/2017.
 */

public interface UsersClient {

    @POST("signup")
    Call<ResponseToken> signUp(@Body Users users);

    @POST("login")
    Call<ResponseToken> login(@Body LoginUser login);
    @GET("check_verification")
    Call<List<ResponseToken>> checkverification(@Query("token") String token);

    @GET("fcm_token")
    Call<ResponseToken> sendFCMToken(@Query("token")String token);
}
