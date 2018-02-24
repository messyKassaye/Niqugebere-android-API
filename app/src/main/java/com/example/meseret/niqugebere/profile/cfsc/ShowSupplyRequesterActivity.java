package com.example.meseret.niqugebere.profile.cfsc;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.SupplyRequesterRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.SuppliesAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.RequestedProduct;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Supplies;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowSupplyRequesterActivity extends AppCompatActivity {
   private Toolbar toolbar;
   private String supplied_id;

    private List<SuppliesAdapterModel>list;
    private SupplyRequesterRecyclerviewAdapter adapter;
    private RecyclerView recyclerView;
    private SharedPreferences preferences;
    private ImageView product_photo;
    private TextView product_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_requesterr);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Appliers for "+getIntent().getStringExtra("name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        setSupplied_id(getIntent().getStringExtra("id"));
        preferences=getSharedPreferences("token",0);

        product_photo=(ImageView)findViewById(R.id.requested_product_photo);
        product_name=(TextView)findViewById(R.id.requested_product_name);
        findRequestedProductDetails();
        getRequester();

}
    public void findRequestedProductDetails(){
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Supplies>> call=client.getRequestedProduct(preferences.getString("token",null),getSupplied_id());
        call.enqueue(new Callback<List<Supplies>>() {
            @Override
            public void onResponse(Call<List<Supplies>> call, Response<List<Supplies>> response) {
                Picasso.with(getApplicationContext()).load(Projectstatics.IMAPGE_PATH+response.body().get(0).getProduct_photo()).placeholder(R.drawable.background_tile2_small).into(product_photo);
                product_name.setText(response.body().get(0).getTitle());
            }

            @Override
            public void onFailure(Call<List<Supplies>> call, Throwable t) {

            }
        });
    }

    public void getRequester(){
        list=new ArrayList<>();
        adapter=new SupplyRequesterRecyclerviewAdapter(this,list);
        recyclerView=(RecyclerView)findViewById(R.id.supply_requester_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Supplies>>call=client.showSupplyRequester(preferences.getString("token",null),getSupplied_id());
        call.enqueue(new Callback<List<Supplies>>() {
            @Override
            public void onResponse(Call<List<Supplies>> call, Response<List<Supplies>> response) {
                if (response.isSuccessful()){
                    if (response.body().size()<=0){
                        Toast.makeText(getApplicationContext(),"No one is applied for this product ):",Toast.LENGTH_LONG).show();
                    }else {
                       if (response.body().size()>0){
                           for (int i=0;i<response.body().size();i++){
                               SuppliesAdapterModel model=new SuppliesAdapterModel();
                               model.setCompany_name(response.body().get(i).getCompany_name());
                               model.setCategory_name(response.body().get(i).getCategory_name());
                               model.setSub_category_name(response.body().get(i).getSub_category_name());
                               model.setTotal_quantity(response.body().get(i).getTotal_quantity());
                               model.setTitle(response.body().get(i).getTitle());
                               model.setCompany_photo(response.body().get(i).getCompany_photo());
                               list.add(model);
                           }

                           adapter.notifyDataSetChanged();
                           recyclerView.setAdapter(adapter);
                       }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Supplies>> call, Throwable t) {

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

    public String getSupplied_id() {
        return supplied_id;
    }

    public void setSupplied_id(String supplied_id) {
        this.supplied_id = supplied_id;
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
