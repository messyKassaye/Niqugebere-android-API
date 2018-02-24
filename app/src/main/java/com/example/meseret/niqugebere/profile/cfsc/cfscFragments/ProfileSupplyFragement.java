package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.SuppliesRecyclerviewAdapter;
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
public class ProfileSupplyFragement extends Fragment {

    private RecyclerView supply_recyclerview;
    private List<SuppliesAdapterModel> supply_list;
    private SuppliesRecyclerviewAdapter supply_adapter;

    private String token;
    private SharedPreferences preferences;
    public ProfileSupplyFragement() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_supply_fragement, container, false);

        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        supply_list=new ArrayList<>();
        supply_adapter=new SuppliesRecyclerviewAdapter(getActivity(),supply_list);
        supply_recyclerview=(RecyclerView)view.findViewById(R.id.profile_supply_recyclerview);
        supply_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        supply_recyclerview.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Supplies>> call=client.getSupplies(getToken());
        call.enqueue(new Callback<List<Supplies>>() {
            @Override
            public void onResponse(Call<List<Supplies>> call, Response<List<Supplies>> response) {
                if (response.body().size()>0){
                    for (int i=0;i<response.body().size();i++){
                        SuppliesAdapterModel model=new SuppliesAdapterModel();
                        model.setProduct_photo(response.body().get(i).getProduct_photo());
                        model.setCompany_photo(response.body().get(i).getCompany_photo());
                        model.setCompany_name(response.body().get(i).getCompany_name());
                        model.setCategory_name(response.body().get(i).getCategory_name());
                        model.setSub_category_name(response.body().get(i).getSub_category_name());
                        model.setTitle(response.body().get(i).getTitle());
                        model.setPrice(response.body().get(i).getPrice());
                        model.setAvailability(response.body().get(i).getAvailability());
                        model.setId(response.body().get(i).getId());
                        model.setToken(getToken());
                        supply_list.add(model);
                    }
                }
                supply_adapter.notifyDataSetChanged();
                supply_recyclerview.setAdapter(supply_adapter);
            }

            @Override
            public void onFailure(Call<List<Supplies>> call, Throwable t) {

            }
        });

        return view;
    }
    public void  getSupplies(View view){

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
