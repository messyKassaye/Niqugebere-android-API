package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.content.Intent;
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

public class ShowSupplyrecyclerviewAdapter extends RecyclerView.Adapter<ShowSupplyrecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<SuppliesAdapterModel> albumList;
    private boolean is_runnig=true;
    private String supplies_id;
    private String token;
    private ProgressBar pr;
    private LinearLayout supply_mainlayout;
    private TextView supply_success_message;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView company_photo,supplied_product_photo;
        public TextView product_category_name,product_sub_category_name,supplied_product_name,availability_date,price;
        public Button show_applier,edit,delete;
        public MyViewHolder(View view) {
            super(view);
            supplied_product_photo=(ImageView)view.findViewById(R.id.supply_product_image);
            product_category_name=(TextView)view.findViewById(R.id.product_category_name);
            product_sub_category_name=(TextView)view.findViewById(R.id.product_sub_category_name);
            availability_date=(TextView)view.findViewById(R.id.suppliy_availability);
            supplied_product_name=(TextView)view.findViewById(R.id.supplied_product_name);
            price=(TextView)view.findViewById(R.id.supply_price);
            show_applier=(Button)view.findViewById(R.id.show_applier);
            edit=(Button)view.findViewById(R.id.edit_supply);
            delete=(Button)view.findViewById(R.id.delete_supply);

        }
    }


    public ShowSupplyrecyclerviewAdapter(Context mContext, List<SuppliesAdapterModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_supply_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SuppliesAdapterModel model = albumList.get(position);



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
        holder.product_category_name.setText(Html.fromHtml(product_name));
        holder.product_sub_category_name.setText(Html.fromHtml(product_sub_name));
        holder.supplied_product_name.setText(Html.fromHtml(supplied_product_name));
        holder.price.setText(Html.fromHtml(supplied_product_price));
        holder.availability_date.setText(Html.fromHtml(supplied_product_availablity));
        setToken(model.getToken());
        holder.show_applier.setTag(model.getId());
        holder.edit.setTag(model.getId());
        holder.show_applier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ShowSupplyRequesterActivity.class);
                intent.putExtra("id",view.getTag().toString());
                intent.putExtra("name",model.getTitle());
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