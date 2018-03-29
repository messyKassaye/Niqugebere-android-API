package com.example.meseret.niqugebere.companies.companyAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adapter.CommercialFarmCenterAdapter;
import com.example.meseret.niqugebere.adaptermodel.CommercialFarmCenterAdapterModel;
import com.example.meseret.niqugebere.companies.CFSCDetailsActivity;
import com.example.meseret.niqugebere.companies.companyModels.CompanyServices;
import com.example.meseret.niqugebere.companies.fragments.CompanyServiceFragment;
import com.example.meseret.niqugebere.model.Services;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by meseret on 3/3/18.
 */

public class ServiceCategoryRecyclerviewAdapter extends RecyclerView.Adapter<ServiceCategoryRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<CompanyServices> albumList;
    private CompanyServiceFragment fragment;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView service_name;
        public ImageView service_photo;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            service_name = (TextView) view.findViewById(R.id.service_name);
            service_photo = (ImageView) view.findViewById(R.id.service_images);
            cardView=(CardView)view.findViewById(R.id.top_mall_card_view);
        }
    }


    public ServiceCategoryRecyclerviewAdapter(Context mContext, CompanyServiceFragment fragment,List<CompanyServices> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.fragment=fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_layouts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        CompanyServices model = albumList.get(position);
        holder.service_name.setText(model.getName());
        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getPhoto_path()).placeholder(R.drawable.background_tile2_small).into(holder.service_photo);
        holder.service_photo.setTag(model.getId());
        holder.service_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             fragment.getMyService(view.getTag().toString());
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
}