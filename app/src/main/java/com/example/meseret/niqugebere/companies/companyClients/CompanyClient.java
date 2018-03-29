package com.example.meseret.niqugebere.companies.companyClients;

import com.example.meseret.niqugebere.companies.companyModels.AboutUs;
import com.example.meseret.niqugebere.companies.companyModels.CompanyDetail;
import com.example.meseret.niqugebere.companies.companyModels.CompanyName;
import com.example.meseret.niqugebere.companies.companyModels.CompanyServices;
import com.example.meseret.niqugebere.companies.companyModels.ContactUs;
import com.example.meseret.niqugebere.companies.companyModels.Order;
import com.example.meseret.niqugebere.companies.companyModels.PostedProducts;
import com.example.meseret.niqugebere.companies.companyModels.Prodcuts;
import com.example.meseret.niqugebere.companies.companyModels.ServiceDetail;
import com.example.meseret.niqugebere.companies.companyModels.Transporter;
import com.example.meseret.niqugebere.companies.companyModels.Woreda;
import com.example.meseret.niqugebere.model.APISuccessResponse;
import com.example.meseret.niqugebere.model.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Meseret on 1/21/2018.
 */

public interface CompanyClient {
    @GET("company_detail")
    Call<List<CompanyDetail>> getCompany(@Query("id")String id);

    @GET("get_product")
    Call<List<Prodcuts>>getProducts(@Query("id")String id);

    @GET("company_name")
    Call<List<CompanyName>> getCompanyName(@Query("id")String id);

    @GET("companies_aboutus")
    Call<List<AboutUs>> getCompanyAboutus(@Query("id")String id);

    @GET("companies_contactus")
    Call<List<ContactUs>> getCompanyContactUs(@Query("company_id")String id);

    @GET("companies_services")
    Call<List<CompanyServices>> getCompanyServices(@Query("id")String id);

    @GET("posted_products")
    Call<List<PostedProducts>> getCompanyPostedProducts(@Query("company_id")String company_id,@Query("product_category_id")String product_category_id);

    @POST("company_product_order")
    Call<APISuccessResponse> sendProductOrder(@Body Order order);

    @GET("get_woreda")
    Call<List<Woreda>> getWoreda();

    @GET("get_NearBy_Transporter")
    Call<List<Transporter>> getNearByTransporter(@Query("woreda_id")String id);

    @GET("service/index")
    Call<List<CompanyServices>> getServices(@Query("id")String id);

    @GET("service/show")
    Call<List<ServiceDetail>> showServices(@Query("id")String id,@Query("company_id")String company_id);

}
