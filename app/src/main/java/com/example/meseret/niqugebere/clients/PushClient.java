package com.example.meseret.niqugebere.clients;

import com.example.meseret.niqugebere.model.ResponseToken;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by meseret on 2/25/18.
 */

public interface PushClient {

    @GET("cfsc/identify")
    Call<ResponseToken> identifyCFSC(@Query("tin")String tin);
}
