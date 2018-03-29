package com.example.meseret.niqugebere.companies.companyAdapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyModels.CompanyServices;
import com.example.meseret.niqugebere.companies.companyModels.ServiceDetail;
import com.example.meseret.niqugebere.companies.fragments.CompanyServiceFragment;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by meseret on 3/3/18.
 */

public class ServiceDetailrecyclerviewAdapter extends RecyclerView.Adapter<ServiceDetailrecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ServiceDetail> albumList;
    private CompanyServiceFragment fragment;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public WebView description;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.service_title);
            description = (WebView) view.findViewById(R.id.service_description_webview);
        }
    }


    public ServiceDetailrecyclerviewAdapter(Context mContext, CompanyServiceFragment fragment,List<ServiceDetail> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.fragment=fragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_detail_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ServiceDetail model = albumList.get(position);
        holder.title.setText(model.getTitle());
        holder.description.loadData(model.getDescription(), "text/html", "UTF-8");

    }


    /**
     * Click listener for popup menu items
     */

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}