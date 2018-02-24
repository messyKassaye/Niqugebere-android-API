package com.example.meseret.niqugebere.profile.transporter.transporterClient;

import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.Paths;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.Transport;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.TransportFrom;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.Vehicles;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Meseret on 2/17/2018.
 */

public interface TransportClient {

    @GET("paths")
    Call<List<Paths>> getPaths(@Query("token")String token);

    @POST("register_vehicles")
    Call<ResponseToken> registerVehicles(@Query("token")String token,@Body Vehicles vehicles);
    @GET("transport_to")
    Call<List<Transport>> getTransportations(@Query("token")String token);

    @GET("transport_from")
    Call<List<TransportFrom>> getTransportationsFrom(@Query("token")String token, @Query("id")int id);
}
