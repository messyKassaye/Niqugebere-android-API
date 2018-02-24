package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.meseret.niqugebere.profile.cfsc.ShowSupplyRequesterActivity;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.SuppliesAdapterModel;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meseret on 1/31/2018.
 */

public class SupplyRequesterRecyclerviewAdapter extends RecyclerView.Adapter<SupplyRequesterRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<SuppliesAdapterModel> albumList;
    private boolean is_runnig=true;
    private String supplies_id;
    private String token;
    private ProgressBar pr;
    private LinearLayout supply_mainlayout;
    private TextView supply_success_message;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView company_photo;
        public TextView company_name,quantity,delivery_type;
        public Button start_shipping;
        public MyViewHolder(View view) {
            super(view);
            company_photo=(ImageView)view.findViewById(R.id.requester_company_photo);
            company_name=(TextView)view.findViewById(R.id.requester_company_name);
            quantity=(TextView)view.findViewById(R.id.requested_quantity);
            delivery_type=(TextView)view.findViewById(R.id.delivery_type);


        }
    }


    public SupplyRequesterRecyclerviewAdapter(Context mContext, List<SuppliesAdapterModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_supply_requester_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SuppliesAdapterModel model = albumList.get(position);



        Glide.with(mContext)
                .load(Uri.parse(Projectstatics.IMAPGE_PATH+model.getCompany_photo())) // add your image url
                .transform(new CircleTransform(mContext)) // applying the image transformer
                .into(holder.company_photo);
        holder.company_name.setText(model.getCompany_name());
        String company_name="<font COLOR=\'#ffffff\'><b>" + "Campany name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getCompany_name() + "</font>";
        String product_name="<font COLOR=\'#ffffff\'><b>" + "Category name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getCategory_name() + "</font>";
        String product_sub_name="<font COLOR=\'#ffffff\'><b>" + "Category type:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getSub_category_name() + "</font>";
        String supplied_product_name="<font COLOR=\'#ffffff\'><b>" + "Product name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getTitle() + "</font>";
        String total_quantity="<font COLOR=\'#ffffff\'><b>" + "Total request:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getTotal_quantity()+ "</font>";
        //holder.company_name.setText(Html.fromHtml(company_name));
        setToken(model.getToken());
        holder.quantity.setText(Html.fromHtml(total_quantity));
        holder.delivery_type.setText("Not delivered");
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