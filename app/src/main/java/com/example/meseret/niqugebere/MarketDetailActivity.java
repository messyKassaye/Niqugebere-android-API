package com.example.meseret.niqugebere;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.meseret.niqugebere.adapter.MarketItemRecyclerviewAdapter;
import com.example.meseret.niqugebere.adaptermodel.MarketItemsAdapterModel;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.model.MarketItems;
import com.example.meseret.niqugebere.model.MarketLists;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MarketDetailActivity extends AppCompatActivity {
    private String product_id;
    SharedPreferences preferences;
    private TabLayout tabLayout;
    private ArrayList<String> id_holder;
    private ProgressBar pr;
    boolean is_truee=false;

    private List<MarketItemsAdapterModel>list;
    private MarketItemRecyclerviewAdapter adapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getIntent().getStringExtra("type")+" markets");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setProduct_id(getIntent().getStringExtra("id"));

        tabLayout=(TabLayout)findViewById(R.id.tabs);
        pr=(ProgressBar)findViewById(R.id.supplier_pr);
        pr.setVisibility(View.VISIBLE);

        id_holder=new ArrayList<>();
        Retrofit retrofit=getUserAPIretrofit();
        MainClient client=retrofit.create(MainClient.class);
        Call<List<MarketLists>> call=client.getMarketCategory(getProduct_id());
        call.enqueue(new Callback<List<MarketLists>>() {
            @Override
            public void onResponse(Call<List<MarketLists>> call, Response<List<MarketLists>> response) {
                pr.setVisibility(View.GONE);
                if (response.body().size()>0){
                    is_truee=true;
                    for (int i=0;i<response.body().size();i++){
                        TabLayout.Tab tab=tabLayout.newTab();
                        tab.setText(response.body().get(i).getName());
                        id_holder.add(response.body().get(i).getId());
                        tabLayout.addTab(tab);
                    }
                }else {
                    tabLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<MarketLists>> call, Throwable t) {

            }
        });

        list=new ArrayList<>();
        adapter=new MarketItemRecyclerviewAdapter(this,list);
        recyclerView=(RecyclerView)findViewById(R.id.markets_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (is_truee){
                    pr.setVisibility(View.VISIBLE);
                    final String id= id_holder.get(tab.getPosition());
                    Retrofit retrofit1=getUserAPIretrofit();
                    MainClient client1=retrofit1.create(MainClient.class);
                    Call<List<MarketItems>> call1=client1.marketPost(id);
                    call1.enqueue(new Callback<List<MarketItems>>() {
                        @Override
                        public void onResponse(Call<List<MarketItems>> call, Response<List<MarketItems>> response) {
                            pr.setVisibility(View.GONE);
                            if(response.isSuccessful()){
                                adapter.clearApplications();
                                if (response.body().size()>0){
                                    for (int i=0;i<response.body().size();i++){
                                        MarketItemsAdapterModel model=new MarketItemsAdapterModel();
                                        model.setId(response.body().get(i).getId());
                                        model.setCompany_id(response.body().get(i).getCompany_id());
                                        model.setProduct_name(response.body().get(i).getProduct_name());
                                        model.setProduct_photo(response.body().get(i).getProduct_photo());
                                        model.setUnit_price(response.body().get(i).getUnit_price());
                                        list.add(model);
                                    }
                                    adapter.notifyDataSetChanged();
                                    recyclerView.setAdapter(adapter);
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<List<MarketItems>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
