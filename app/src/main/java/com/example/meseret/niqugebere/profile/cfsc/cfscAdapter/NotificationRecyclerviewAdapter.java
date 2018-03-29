package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.DemandAwardingActivity;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.DemandsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Demands;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Notifications;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meseret on 2/16/2018.
 */

public class NotificationRecyclerviewAdapter extends RecyclerView.Adapter<NotificationRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Notifications> albumList;

    private String token;
    private SharedPreferences preferences;
    private RecyclerView demand_recyclerview;
    private RecyclerView supply_recyclerview;

    private List<Demands> demandsList;
    private DemandNotificationrecylerviewAdapter demandAdapter;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView supplied_product_photo;
        public TextView product_category_name,product_sub_category_name,supplied_product_name,price,no_of_appliers;
        public Button apply;
        public MyViewHolder(View view) {
            super(view);
            demand_recyclerview=(RecyclerView)view.findViewById(R.id.demand_notification_recyclerview);
            demand_recyclerview.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.VERTICAL,false));
            supply_recyclerview=(RecyclerView)view.findViewById(R.id.supply_notification_recyclerview);

            demandsList=new ArrayList<>();

        }
    }


    public NotificationRecyclerviewAdapter(Context mContext, List<Notifications> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }
    public void clearApplications() {
        int size = this.albumList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                albumList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Notifications model = albumList.get(position);
        preferences=mContext.getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));
        if(model.getNotification_category_id()==1){
            demandAdapter=new DemandNotificationrecylerviewAdapter(mContext,demandsList);
            Retrofit retrofit=getUserAPIretrofit();
            CFSCClient client= retrofit.create(CFSCClient.class);
            Call<List<Demands>> call=client.notificationShow(getToken(),model.getNotifable_id(),model.getNotification_category_id());
            call.enqueue(new Callback<List<Demands>>() {
                @Override
                public void onResponse(Call<List<Demands>> call, Response<List<Demands>> response) {
                    if(response.isSuccessful()){
                       for (int i=0;i<response.body().size();i++){
                           Demands demands=new Demands();
                           demands.setCompany_name(response.body().get(i).getCompany_name());
                           demands.setTitle(response.body().get(i).getTitle());
                           demands.setTotal_quantity(response.body().get(i).getTotal_quantity());
                           demandsList.add(demands);
                       }
                       demandAdapter.notifyDataSetChanged();
                        demand_recyclerview.setAdapter(demandAdapter);
                    }
                }

                @Override
                public void onFailure(Call<List<Demands>> call, Throwable throwable) {

                }
            });
        }


        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);


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
    @Override
    public int getItemCount() {
        return albumList.size();
    }
}