package com.example.meseret.niqugebere.systemadmin;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adapter.ApprovalRecyclerviewAdapter;
import com.example.meseret.niqugebere.adaptermodel.CompaniesModel;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.model.Companies;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SystemAdminActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private List<CompaniesModel> list;
    private ApprovalRecyclerviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_admin);

        toolbar=(Toolbar)findViewById(R.id.sy_tool_bar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Smart Farm");
        setSupportActionBar(toolbar);
        list=new ArrayList<>();
        adapter=new ApprovalRecyclerviewAdapter(this,list);
        recyclerView=(RecyclerView)findViewById(R.id.approve_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        MainClient client=retrofit.create(MainClient.class);
        Call<List<Companies>>call=client.getApproval();
        call.enqueue(new Callback<List<Companies>>() {
            @Override
            public void onResponse(Call<List<Companies>> call, Response<List<Companies>> response) {
                if (response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        CompaniesModel model=new CompaniesModel();
                        model.setId(response.body().get(i).getId());
                        model.setCompany_name(response.body().get(i).getCompany_name());
                        model.setOwner_name(response.body().get(i).getOwner_name());
                        model.setPhone(response.body().get(i).getPhone());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Companies>> call, Throwable t) {

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
}
