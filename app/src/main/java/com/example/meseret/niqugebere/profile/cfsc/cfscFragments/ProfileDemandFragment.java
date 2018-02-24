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
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.DemandsRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.DemandsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Demands;
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
public class ProfileDemandFragment extends Fragment {
    private RecyclerView demand_recyclerview;
    private List<DemandsAdapterModel> demand_list;
    private DemandsRecyclerviewAdapter demand_adapter;

    private String token;
    private SharedPreferences preferences;
    public ProfileDemandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_demand, container, false);

        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        demand_list=new ArrayList<>();
        demand_adapter=new DemandsRecyclerviewAdapter(getActivity(),demand_list);
        demand_recyclerview=(RecyclerView)view.findViewById(R.id.profile_demand_recyclerview);
        demand_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        demand_recyclerview.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Demands>> call=client.getDemands(getToken());
        call.enqueue(new Callback<List<Demands>>() {
            @Override
            public void onResponse(Call<List<Demands>> call, Response<List<Demands>> response) {
                if (response.body().size()>0){
                    for (int i=0;i<response.body().size();i++){
                        DemandsAdapterModel model=new DemandsAdapterModel();
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
                        demand_list.add(model);
                    }
                }
                demand_adapter.notifyDataSetChanged();
                demand_recyclerview.setAdapter(demand_adapter);
            }

            @Override
            public void onFailure(Call<List<Demands>> call, Throwable t) {

            }
        });

        return view;
    }
    public void getDemands(View view){


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
