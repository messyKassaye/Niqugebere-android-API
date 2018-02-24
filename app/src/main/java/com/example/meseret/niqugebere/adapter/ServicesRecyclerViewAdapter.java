package com.example.meseret.niqugebere.adapter;

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
import com.example.meseret.niqugebere.adaptermodel.CommercialFarmCenterAdapterModel;
import com.example.meseret.niqugebere.adaptermodel.ServicesAdapterModel;
import com.example.meseret.niqugebere.companies.CFSCDetailsActivity;
import com.example.meseret.niqugebere.model.Services;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.example.meseret.niqugebere.services.ServicesActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Meseret on 1/27/2018.
 */

public class ServicesRecyclerViewAdapter extends RecyclerView.Adapter<ServicesRecyclerViewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ServicesAdapterModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mallname;
        public ImageView mall_phtoto;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            mallname = (TextView) view.findViewById(R.id.mall_name);
            mall_phtoto = (ImageView) view.findViewById(R.id.mall_image);
            cardView=(CardView)view.findViewById(R.id.top_mall_card_view);
        }
    }


    public ServicesRecyclerViewAdapter(Context mContext, List<ServicesAdapterModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_malls_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ServicesAdapterModel model = albumList.get(position);
        holder.mallname.setText(model.getName());
        holder.mall_phtoto.setTag(model.getId());
        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getPhoto_path()).placeholder(R.drawable.background_tile2_small).into(holder.mall_phtoto);
        holder.mall_phtoto.setTag(model.getId());
        holder.mall_phtoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(mContext, ServicesActivity.class);
                intent.putExtra("id",view.getTag().toString());
                intent.putExtra("type",model.getName());
                mContext.startActivity(intent);

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