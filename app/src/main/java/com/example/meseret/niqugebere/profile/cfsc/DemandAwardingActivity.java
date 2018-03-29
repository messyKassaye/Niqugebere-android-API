package com.example.meseret.niqugebere.profile.cfsc;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.AwardingRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.DemandsRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.TransporterRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.DemandsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.TransporterAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Demands;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Transporter;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DemandAwardingActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String id;
    private String token;
    private SharedPreferences preferences;

    public ImageView supplied_product_photo;
    public TextView product_category_name,product_sub_category_name,supplied_product_name,price;
    public TextView awarding_info;

    private List<DemandsAdapterModel> list;
    private AwardingRecyclerviewAdapter adapter;
    private RecyclerView recyclerView;

    public ProgressBar pr;
    private String productName="";

    private RecyclerView transporter_recyclerview;
    private List<TransporterAdapterModel> transporter_list;
    private TransporterRecyclerviewAdapter transporter_adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_awarding);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Demand Awarding");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        preferences=getSharedPreferences("token",0);

        setToken(preferences.getString("token",""));
        setId(getIntent().getStringExtra("id"));

        supplied_product_photo=(ImageView)findViewById(R.id.supply_product_image);
        product_category_name=(TextView)findViewById(R.id.product_category_name);
        product_sub_category_name=(TextView)findViewById(R.id.product_sub_category_name);
        supplied_product_name=(TextView)findViewById(R.id.supplied_product_name);
        price=(TextView)findViewById(R.id.supply_price);
        awarding_info=(TextView)findViewById(R.id.awarding_info);

        pr=(ProgressBar)findViewById(R.id.award_pr);
        pr.setVisibility(View.VISIBLE);

        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Demands>> call=client.getDemandAwarding(getToken(),getId());
        call.enqueue(new Callback<List<Demands>>() {
            @Override
            public void onResponse(Call<List<Demands>> call, Response<List<Demands>> response) {
                if(response.isSuccessful()){
                    pr.setVisibility(View.GONE);
                    awarding_info.setText("List of company applied for "+response.body().get(0).getTitle());
                }
            }

            @Override
            public void onFailure(Call<List<Demands>> call, Throwable t) {

            }
        });

        list=new ArrayList<>();
        adapter=new AwardingRecyclerviewAdapter(getApplicationContext(),list);
        recyclerView=(RecyclerView)findViewById(R.id.awarding_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Call<List<Demands>> call1=client.getAwardingCompany(getToken(),getId());
        call1.enqueue(new Callback<List<Demands>>() {
            @Override
            public void onResponse(Call<List<Demands>> call, Response<List<Demands>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        DemandsAdapterModel model=new DemandsAdapterModel();
                        model.setId(response.body().get(i).getId());
                        model.setCompany_photo(response.body().get(i).getCompany_photo());
                        model.setCompany_name(response.body().get(i).getCompany_name());
                        list.add(model);
                    }

                }
                adapter.notifyDataSetChanged();
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Demands>> call, Throwable t) {

            }
        });





    }
    public void showTransporter(){
        awarding_info.setText("Select transporter for "+getProductName());
        recyclerView.setVisibility(View.GONE);

        transporter_list=new ArrayList<>();
        transporter_adapter=new TransporterRecyclerviewAdapter(getApplicationContext(),transporter_list);
        transporter_recyclerview=(RecyclerView)findViewById(R.id.transporter_recyclerview);
        transporter_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
        transporter_recyclerview.setItemAnimator(new DefaultItemAnimator());
        transporter_recyclerview.setVisibility(View.VISIBLE);
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Transporter>> call=client.getTransporters(getToken(),getId());
        call.enqueue(new Callback<List<Transporter>>() {
            @Override
            public void onResponse(Call<List<Transporter>> call, Response<List<Transporter>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        TransporterAdapterModel model=new TransporterAdapterModel();
                        model.setCompany_name(response.body().get(i).getCompany_name());
                        model.setCapacity(response.body().get(i).getCapacity());
                        model.setPlate_no(response.body().get(i).getPlate_no());
                        transporter_list.add(model);
                    }
                    transporter_adapter.notifyDataSetChanged();
                    transporter_recyclerview.setAdapter(transporter_adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Transporter>> call, Throwable t) {

            }
        });
    }



    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setInfo(String text){
        awarding_info.setText(text);
    }
    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
