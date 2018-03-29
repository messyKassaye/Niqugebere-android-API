package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adaptermodel.MarketItemsAdapterModel;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.model.MarketItems;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.ShowSupplyrecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.SuppliesAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Supplies;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowSupply extends Fragment {

    private TabLayout tabLayout;
    private ArrayList<String> id_holder;
    private ProgressBar pr;
    private SharedPreferences preferences;
    private String token;
    private RecyclerView recyclerView;

    private List<SuppliesAdapterModel>list;
    private ShowSupplyrecyclerviewAdapter adapter;
    public ShowSupply() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view= inflater.inflate(R.layout.fragment_show_supply, container, false);

        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        tabLayout=(TabLayout)view.findViewById(R.id.tabs);
        pr=(ProgressBar)view.findViewById(R.id.supplier_pr);
        pr.setVisibility(View.VISIBLE);

        TabLayout.Tab tab=tabLayout.newTab();
        tab.setText("My Supply");
        tabLayout.addTab(tab);

        TabLayout.Tab tab2=tabLayout.newTab();
        tab2.setText("Supply request");
        tabLayout.addTab(tab2);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

               if (tab.getPosition()==0){
                   showMySupply(view);
               }

              }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        showMySupply(view);

        return  view;
    }

    public void showMySupply(View view){
        list=new ArrayList<>();
        adapter=new ShowSupplyrecyclerviewAdapter(getActivity(),list);
        recyclerView=(RecyclerView)view.findViewById(R.id.show_suppply_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Supplies>> call=client.showSupply(getToken());
        call.enqueue(new Callback<List<Supplies>>() {
            @Override
            public void onResponse(Call<List<Supplies>> call, Response<List<Supplies>> response) {
                if(response.isSuccessful()){
                    if (response.body().size()>0){
                        pr.setVisibility(View.GONE);
                        for (int i=0;i<response.body().size();i++){
                            SuppliesAdapterModel model=new SuppliesAdapterModel();
                            model.setCategory_name(response.body().get(i).getCategory_name());
                            model.setSub_category_name(response.body().get(i).getSub_category_name());
                            model.setProduct_photo(response.body().get(i).getProduct_photo());
                            model.setPrice(response.body().get(i).getPrice());
                            model.setId(response.body().get(i).getId());
                            model.setAvailability(response.body().get(i).getAvailability());
                            model.setTitle(response.body().get(i).getTitle());
                            list.add(model);
                        }
                        adapter.notifyDataSetChanged();
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Supplies>> call, Throwable t) {

            }
        });

    }

    public void showMySupplyRequest(View view){

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
}
