package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.helpers.CircleTransform;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.DemandsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Demands;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meseret on 2/26/18.
 */

public class DemandNotificationrecylerviewAdapter extends RecyclerView.Adapter<DemandNotificationrecylerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<Demands> albumList;
    private boolean is_runnig=true;
    private String supplies_id;
    private String token;
    private ProgressBar pr;
    private LinearLayout supply_mainlayout;
    private SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView message;

        public MyViewHolder(View view) {
            super(view);

           message=(TextView)view.findViewById(R.id.demand_notification_message);


        }
    }


    public DemandNotificationrecylerviewAdapter(Context mContext, List<Demands> albumList) {
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
                .inflate(R.layout.demands_notification_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Demands model = albumList.get(position);

        preferences=mContext.getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));

        holder.message.setText(model.getCompany_name()+" Awarded you to deliver their demand of "+model.getTitle()+" with total quantity "+model.getTotal_quantity());
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


    /**
     * Click listener for popup menu items
     */

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public boolean is_runnig() {
        return is_runnig;
    }

    public void setIs_runnig(boolean is_runnig) {
        this.is_runnig = is_runnig;
    }

    public String getSupplies_id() {
        return supplies_id;
    }

    public void setSupplies_id(String supplies_id) {
        this.supplies_id = supplies_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}