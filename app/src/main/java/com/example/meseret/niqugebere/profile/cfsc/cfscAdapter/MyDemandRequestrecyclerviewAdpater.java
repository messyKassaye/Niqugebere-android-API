package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.meseret.niqugebere.R;
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
 * Created by meseret on 2/26/18.
 */

public class MyDemandRequestrecyclerviewAdpater extends RecyclerView.Adapter<MyDemandRequestrecyclerviewAdpater.MyViewHolder> {

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
        public ImageView supplied_product_photo;
        public TextView product_category_name,product_sub_category_name,supplied_product_name,price,no_of_appliers;
        public Button apply;
        public MyViewHolder(View view) {
            super(view);
            supplied_product_photo=(ImageView)view.findViewById(R.id.supply_product_image);
            product_category_name=(TextView)view.findViewById(R.id.product_category_name);
            product_sub_category_name=(TextView)view.findViewById(R.id.product_sub_category_name);
            supplied_product_name=(TextView)view.findViewById(R.id.supplied_product_name);
            price=(TextView)view.findViewById(R.id.supply_price);
            apply=(Button)view.findViewById(R.id.supply_apply);
            no_of_appliers=(TextView)view.findViewById(R.id.no_of_appliers);

        }
    }


    public MyDemandRequestrecyclerviewAdpater(Context mContext, List<DemandsAdapterModel> albumList) {
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
                .inflate(R.layout.demand_notifacations_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final DemandsAdapterModel model = albumList.get(position);

        preferences=mContext.getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));

        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getProduct_photo()).placeholder(R.drawable.background_tile2_small).into(holder.supplied_product_photo);
        String product_name="<font COLOR=\'#242424\'><b>" + "Category name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getCategory_name() + "</font>";
        String product_sub_name="<font COLOR=\'#242424\'><b>" + "Category type:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getSub_category_name() + "</font>";
        String supplied_product_name="<font COLOR=\'#242424\'><b>" + "Product name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getTitle() + "</font>";
        String supplied_product_price="<font COLOR=\'#242424\'><b>" + "Unit price:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getPrice()+" ETB"+ "</font>";
        String supplied_product_availablity="<font COLOR=\'#242424\'><b>" + "Delivered in:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getAvailability()+ "</font>";
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<ResponseToken> call=client.getNoOfAppliers(getToken(),model.getId());
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                String no_of_appliers="<font COLOR=\'#ff0000\'><b>" + "No of appliers:  " + "</b></font>"
                        + "<font COLOR=\'#ff0000\'>" +response.body().getStatus()+ "</font>";
                holder.no_of_appliers.setText(Html.fromHtml(no_of_appliers));
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {

            }
        });
        holder.product_category_name.setText(Html.fromHtml(product_name));
        holder.product_sub_category_name.setText(Html.fromHtml(product_sub_name));
        holder.supplied_product_name.setText(Html.fromHtml(supplied_product_name));
        holder.price.setText(Html.fromHtml(supplied_product_price));
        setToken(model.getToken());
        holder.apply.setTag(model.getId());
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id= view.getTag().toString();
                Intent intent=new Intent(mContext, DemandAwardingActivity.class);
                intent.putExtra("id",id);
                mContext.startActivity(intent);

            }
        });
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