package com.example.meseret.niqugebere.clients;

import com.example.meseret.niqugebere.adaptermodel.CompaniesModel;
import com.example.meseret.niqugebere.adaptermodel.PullAdapterModel;
import com.example.meseret.niqugebere.model.CarType;
import com.example.meseret.niqugebere.model.CommercialFarmCenter;
import com.example.meseret.niqugebere.model.Companies;
import com.example.meseret.niqugebere.model.MarketItems;
import com.example.meseret.niqugebere.model.MarketLists;
import com.example.meseret.niqugebere.model.Markets;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.model.Roles;
import com.example.meseret.niqugebere.model.Services;
import com.example.meseret.niqugebere.model.Sponsors;
import com.example.meseret.niqugebere.model.Version;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Meseret on 1/20/2018.
 */

public interface MainClient {

    @GET("partners")
    Call<List<Sponsors>> getSponsors();

    @GET("get_cfsc")
    Call<List<CommercialFarmCenter>> getCfsc();

    @GET("get_services")
    Call<List<Services>> geServices();

    @GET("get_categories")
    Call<List<Roles>> getRoles();

    @GET("car_type")
    Call<List<CarType>> getCarType();

    @GET("markets")
    Call<List<Markets>> getMarkets();

    @GET("market_category")
    Call<List<MarketLists>> getMarketCategory(@Query("id")String id);

    @GET("posted_item_for_market")
    Call<List<MarketItems>> marketPost(@Query("id")String id);

    @GET("approval")
    Call<List<Companies>> getApproval();

    @GET("approve_request")
    Call<ResponseToken> approveCompanyRequest(@Query("id")String id);

    @GET("pull")
    Call<List<PullAdapterModel>> getPull(@Query("woreda_id")Integer woreda_id,@Query("sub_id")Integer sub_id);

    @GET("version")
    Call<Version> getVersions();
}
