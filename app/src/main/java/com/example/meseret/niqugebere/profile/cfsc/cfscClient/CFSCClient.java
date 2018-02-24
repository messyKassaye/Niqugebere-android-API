package com.example.meseret.niqugebere.profile.cfsc.cfscClient;


import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCInvetory;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCProductCategory;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCProductSubCategory;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CfscProfileModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Demands;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.InventoryPayment;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Products;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.RequestedProduct;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Supplies;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Transporter;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Meseret on 1/26/2018.
 */

public interface CFSCClient {

    @GET("cfsc_profile")
    Call<List<CfscProfileModel>> getCfscProfile(@Query("token")String token);

    @Multipart
    @POST("upload_cfsc_profile")
    Call<ResponseToken> upload(@Query("token")String token,
                                     @Part MultipartBody.Part file);

    @GET("update_description")
    Call<ResponseToken> updateDescription(@Query("token")String token,@Query("description")String description);

    @GET("product_selection")
    Call<List<Products>> getProducts(@Query("token")String toke);

    @GET("update_product_selection")
    Call<ResponseToken> companyProductSelection(@Query("token")String token,@Query("id")String id);

    @GET("supply")
    Call<List<Supplies>> getSupplies(@Query("token")String token);

    @GET("supply_request")
    Call<ResponseToken> sendSupplyResult(@Query("token")String token,@Query("id")String id,@Query("quantity")String quanity);

    @GET("get_product_category")
    Call<List<CFSCProductCategory>> getProductCategory(@Query("token")String token);

    @GET("get_sub_product_category")
    Call<List<CFSCProductSubCategory>> getSubProductCategory(@Query("token")String token,@Query("id")Integer id);

    @Multipart
    @POST("post_supply")
    Call<ResponseToken> posSupply(@Query("token")String token,
                                  @Part MultipartBody.Part file,
                                  @Part("sub_category_id")  RequestBody sub_category_id,
                                  @Part("title")  RequestBody product_name,
                                  @Part("price")  RequestBody price,
                                  @Part("availability")  RequestBody availability,
                                  @Part("description")  RequestBody description
                                  );



    @Multipart
    @POST("demand_post")
    Call<ResponseToken> postDemand(@Query("token")String token,
                                  @Part MultipartBody.Part file,
                                  @Part("sub_category_id")  RequestBody sub_category_id,
                                  @Part("title")  RequestBody product_name,
                                  @Part("price")  RequestBody price,
                                  @Part("availability")  RequestBody availability,
                                  @Part("description")  RequestBody description
    );

    @GET("show_supply")
    Call<List<Supplies>> showSupply(@Query("token")String token);

    @GET("supply_requester")
    Call<List<Supplies>> showSupplyRequester(@Query("token")String token,@Query("id")String id);


    @GET("demands")
    Call<List<Demands>> getDemands(@Query("token")String token);


    @POST("post_inventory")
    Call<ResponseToken> sendInventory(@Query("token")String token, @Body CFSCInvetory inventory);

    @GET("my_payment")
    Call<List<InventoryPayment>> getMyInventory(@Query("token")String token);


    @GET("single_supply_request")
    Call<List<Supplies>> getRequestedProduct(@Query("token")String token,@Query("id")String id);

    @GET("demand_apply")
    Call<ResponseToken> applyForDemand(@Query("token")String token,@Query("id")String id);

    @GET("notification")
    Call<ResponseToken> getNotifications(@Query("token")String token);

    @GET("demand_notification")
    Call<List<Demands>> getDemandNotification(@Query("token")String token);

    @GET("get_no_of_appliers")
    Call<ResponseToken> getNoOfAppliers(@Query("token")String token,@Query("id")String id);

    @GET("demand_awarding")
    Call<List<Demands>> getDemandAwarding(@Query("token")String token,@Query("id")String id);

    @GET("get_awarding_company")
    Call<List<Demands>> getAwardingCompany(@Query("token")String token,@Query("id")String id);

    @GET("demand_awarded")
    Call<ResponseToken> awardDemand(@Query("token")String token,@Query("id")String id);

    @GET("transporters")
    Call<List<Transporter>> getTransporters(@Query("token")String token);

    @GET("award_transportation")
    Call<ResponseToken> awardTransportation(@Query("token")String token,@Query("id")String id);
}
