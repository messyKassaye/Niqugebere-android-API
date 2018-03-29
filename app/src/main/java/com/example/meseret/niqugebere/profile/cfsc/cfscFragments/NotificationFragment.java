package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.NotificationRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.DemandsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Notifications;
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
public class NotificationFragment extends Fragment {
    private List<Notifications> list;
    private NotificationRecyclerviewAdapter adapter;
    private RecyclerView recyclerView;
    private SharedPreferences preferences;
    private String token;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification, container, false);

        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));

        list=new ArrayList<>();
        adapter=new NotificationRecyclerviewAdapter(getActivity(),list);
        recyclerView=(RecyclerView)view.findViewById(R.id.notification_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Notifications>> call=client.notificationIndex(getToken());
        call.enqueue(new Callback<List<Notifications>>() {
            @Override
            public void onResponse(Call<List<Notifications>> call, Response<List<Notifications>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        Notifications model=new Notifications();
                        model.setNotifable_id(response.body().get(i).getNotifable_id());
                        model.setNotification_category_id(response.body().get(i).getNotification_category_id());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Notifications>> call, Throwable throwable) {

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
