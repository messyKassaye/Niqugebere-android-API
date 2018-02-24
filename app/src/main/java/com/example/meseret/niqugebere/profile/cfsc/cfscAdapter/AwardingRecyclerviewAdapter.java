package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.helpers.CircleTransform;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.DemandAwardingActivity;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.DemandsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meseret on 2/16/2018.
 */

public class AwardingRecyclerviewAdapter extends RecyclerView.Adapter<AwardingRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<DemandsAdapterModel> albumList;
    private boolean is_runnig=true;
    private String supplies_id;
    private String token;
    private ProgressBar pr;
    private LinearLayout supply_mainlayout;
    private TextView supply_success_message;
    private SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView company_photo;
        public TextView company_name;
        public Button award;
        public MyViewHolder(View view) {
            super(view);
            company_photo=(ImageView)view.findViewById(R.id.awarding_company_photo);
            company_name=(TextView)view.findViewById(R.id.awarding_company_name);
            award=(Button)view.findViewById(R.id.award_btn);


        }
    }


    public AwardingRecyclerviewAdapter(Context mContext, List<DemandsAdapterModel> albumList) {
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
                .inflate(R.layout.awarding_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DemandsAdapterModel model = albumList.get(position);

        preferences=mContext.getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));
        holder.company_name.setText(model.getCompany_name());
        Glide.with(mContext)
                .load(Uri.parse(Projectstatics.IMAPGE_PATH+model.getCompany_photo())) // add your image url
                .transform(new CircleTransform(mContext)) // applying the image transformer
                .into(holder.company_photo);
        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);
        holder.award.setTag(model.getId());
        holder.award.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DemandAwardingActivity)holder.award.getContext()).pr.setVisibility(View.VISIBLE);
                Retrofit retrofit=getUserAPIretrofit();
                CFSCClient client=retrofit.create(CFSCClient.class);
                Call<ResponseToken> call=client.awardDemand(getToken(),view.getTag().toString());
                call.enqueue(new Callback<ResponseToken>() {
                    @Override
                    public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                        if(response.isSuccessful()){
                            ((DemandAwardingActivity)holder.award.getContext()).pr.setVisibility(View.GONE);
                            if(response.body().getStatus().equals("ok")){
                                clearApplications();
                                ((DemandAwardingActivity)holder.award.getContext()).showTransporter();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseToken> call, Throwable t) {

                    }
                });
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

    public void getNumberOfAppliers(String id){
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<ResponseToken> call=client.getNoOfAppliers(getToken(),id);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {

            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {

            }
        });
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