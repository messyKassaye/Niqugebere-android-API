package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.DemandNotificationAdapter;
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
public class MyDemandRequestFragment extends Fragment {

    private List<DemandsAdapterModel> list;
    private DemandNotificationAdapter adapter;
    private RecyclerView recyclerView;
    private SharedPreferences preferences;
    private String token;
    public MyDemandRequestFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_demand_request, container, false);
        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));
        list=new ArrayList<>();
        adapter=new DemandNotificationAdapter(getActivity(),list);
        recyclerView=(RecyclerView)view.findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Demands>> call=client.getDemandNotification(getToken());
        call.enqueue(new Callback<List<Demands>>() {
            @Override
            public void onResponse(Call<List<Demands>> call, Response<List<Demands>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        DemandsAdapterModel model=new DemandsAdapterModel();
                        model.setProduct_photo(response.body().get(i).getProduct_photo());
                        model.setTitle(response.body().get(i).getTitle());
                        model.setCategory_name(response.body().get(i).getCategory_name());
                        model.setSub_category_name(response.body().get(i).getSub_category_name());
                        model.setId(response.body().get(i).getId());
                        model.setPrice(response.body().get(i).getPrice());
                        model.setTotal_quantity(response.body().get(i).getTotal_quantity());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Demands>> call, Throwable t) {

            }
        });
        return view;
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
