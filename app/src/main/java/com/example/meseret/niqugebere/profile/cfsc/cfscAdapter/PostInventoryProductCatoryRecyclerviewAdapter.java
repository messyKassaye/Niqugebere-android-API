package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.model.ProductCategory;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.PostInventoryFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.PostSupplyFragment;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meseret on 2/27/18.
 */

public class PostInventoryProductCatoryRecyclerviewAdapter extends RecyclerView.Adapter<PostInventoryProductCatoryRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductCategory> albumList;
    private boolean is_runnig=true;
    private PostInventoryFragment fragment;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button product_name;


        public MyViewHolder(View view) {
            super(view);
            product_name = (Button)view.findViewById(R.id.product_select_btn);
        }
    }


    public PostInventoryProductCatoryRecyclerviewAdapter(Context mContext, List<ProductCategory> albumList, PostInventoryFragment fragments) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.fragment=fragments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_select_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProductCategory model = albumList.get(position);
        holder.product_name.setText(model.getName());
        holder.product_name.setTag(model.getId());
        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);
        holder.product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product_id=view.getTag().toString();
                String product_names=model.getName();
                fragment.displayProductSubCategory(product_id,product_names);
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
}