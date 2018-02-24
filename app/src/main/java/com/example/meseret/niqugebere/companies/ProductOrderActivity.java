package com.example.meseret.niqugebere.companies;

import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.companies.companyAdapter.TransporterRecyclerviewAdapter;
import com.example.meseret.niqugebere.companies.companyAdapterModel.TransporterAdapterModel;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.Order;
import com.example.meseret.niqugebere.companies.companyModels.Transporter;
import com.example.meseret.niqugebere.companies.companyModels.Woreda;
import com.example.meseret.niqugebere.model.APISuccessResponse;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProductOrderActivity extends AppCompatActivity {
private String posted_product_id;
    private Toolbar toolbar;

    private LinearLayout order_layout,transport_layout;
    private TextView order_info;
    private Spinner woreda_spinner;
    private ArrayList<String> woreda_list;
    private CustomSpinnerAdapter adapter;
    private Resources res;
    private EditText full_name,phone_number,total_quantity;
    private Button send_order;
    private TextView error;
    private String company_id;
    private ArrayList<Integer> id_holder;
    private int woreda_id;
    private ProgressBar pr;
    private String product_name;
    private TextView transport_info;

    private RecyclerView recyclerView;
    private List<TransporterAdapterModel> list;
    private TransporterRecyclerviewAdapter recyclerviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_order);

        toolbar=(Toolbar)findViewById(R.id.order_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(getIntent().getStringExtra("product_name"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setProduct_name(getIntent().getStringExtra("product_name"));
        res=getResources();
        posted_product_id=getIntent().getStringExtra("posted_product_id");
        company_id=getIntent().getStringExtra("company_id");
        order_info=(TextView)findViewById(R.id.order_info);
        order_info.setText(getIntent().getStringExtra("product_name")+" will be shipped to your address after filling the following information's");

        order_layout=(LinearLayout)findViewById(R.id.order_layout);
        transport_layout=(LinearLayout)findViewById(R.id.transport_layout);


        pr=(ProgressBar)findViewById(R.id.order_pr);
        full_name=(EditText)findViewById(R.id.order_full_name_or_customer_id);
        phone_number=(EditText)findViewById(R.id.order_phone_no);
        total_quantity=(EditText)findViewById(R.id.order_quantity);
        error=(TextView)findViewById(R.id.order_error);
        woreda_spinner=(Spinner)findViewById(R.id.order_woreda);
        woreda_list=new ArrayList<>();
        woreda_list.add("Select your current living woreda");
        adapter=new CustomSpinnerAdapter(this, R.layout.spinner_dropdown, woreda_list, res);
        woreda_spinner.setAdapter(adapter);
        id_holder=new ArrayList<>();
        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<Woreda>> call=client.getWoreda();
        call.enqueue(new Callback<List<Woreda>>() {
            @Override
            public void onResponse(Call<List<Woreda>> call, Response<List<Woreda>> response) {
                if (response.body().size()>0){
                    for (int i=0;i<response.body().size();i++){
                        woreda_list.add(response.body().get(i).getName());
                        id_holder.add(response.body().get(i).getId());
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Woreda>> call, Throwable t) {

            }
        });
       woreda_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               if (i>=1){
                   setWoreda_id(id_holder.get(i-1));
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
        send_order=(Button)findViewById(R.id.send_order);
        send_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String full_name_value=full_name.getText().toString();
                String phone_no_value=phone_number.getText().toString();
                String total_quantity_value=total_quantity.getText().toString();
                int woreda_value=getWoreda_id();
                if (full_name_value.equals("")){
                    showError("Please enter your name");
                }else if(phone_no_value.equals("")){
                    showError("Please enter your phone number");
                }else if(total_quantity_value.equals("")){
                    showError("Please tell us how much you need");
                }else if (woreda_id<=0){
                    showError("Please select your current living address or woreda");
                }
                else {
                   sendOrder(company_id,full_name_value,phone_no_value,total_quantity_value,String.valueOf(woreda_value));
                }
            }
        });


    }

    public void showError(String text){
        error.setVisibility(View.VISIBLE);
        error.setText(text);
    }
    public void sendOrder(String company_id,String full_name,String phone,String total,String woreda){
        pr.setVisibility(View.VISIBLE);
        Order order=new Order(company_id,getPosted_product_id(),full_name,phone,total,woreda);
        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<APISuccessResponse>call=client.sendProductOrder(order);
        call.enqueue(new Callback<APISuccessResponse>() {
            @Override
            public void onResponse(Call<APISuccessResponse> call, Response<APISuccessResponse> response) {
                if (response.body().getStatus().equals("true")){
                    pr.setVisibility(View.GONE);
                    showTransport();
                }else {
                    pr.setVisibility(View.GONE);
                    showError("Something is not good please check your connection");
                }
            }

            @Override
            public void onFailure(Call<APISuccessResponse> call, Throwable t) {

            }
        });
    }
    public void showTransport(){
        order_layout.setVisibility(View.GONE);
        transport_layout.setVisibility(View.VISIBLE);
        transport_info=(TextView)findViewById(R.id.transport_info);
        transport_info.setText(getProduct_name()+" will be delivered to you within 2-3 days. if you need transport service for this product here are a list of transport service provider who are coming to around your area");

        list=new ArrayList<>();
        recyclerviewAdapter=new TransporterRecyclerviewAdapter(this,list);
        recyclerView=(RecyclerView)findViewById(R.id.transporter_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<Transporter>> call=client.getNearByTransporter(String.valueOf(getWoreda_id()));
        call.enqueue(new Callback<List<Transporter>>() {
            @Override
            public void onResponse(Call<List<Transporter>> call, Response<List<Transporter>> response) {
                for (int i=0;i<response.body().size();i++){
                    TransporterAdapterModel model=new TransporterAdapterModel();
                    model.setId(response.body().get(i).getId());
                    model.setDriver_name(response.body().get(i).getDriver_name());
                    model.setCar_photo(response.body().get(i).getCar_photo());
                    model.setType_of_car(response.body().get(i).getType_of_car());
                    model.setWeight(response.body().get(i).getWeight());
                    list.add(model);
                }
                recyclerviewAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(recyclerviewAdapter);
            }

            @Override
            public void onFailure(Call<List<Transporter>> call, Throwable t) {
              Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
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

    public String getPosted_product_id() {
        return posted_product_id;
    }

    public void setPosted_product_id(String posted_product_id) {
        this.posted_product_id = posted_product_id;
    }

    public int getWoreda_id() {
        return woreda_id;
    }

    public void setWoreda_id(int woreda_id) {
        this.woreda_id = woreda_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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
