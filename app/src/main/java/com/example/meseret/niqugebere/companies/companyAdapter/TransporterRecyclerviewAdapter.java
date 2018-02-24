package com.example.meseret.niqugebere.companies.companyAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapterModel.TransporterAdapterModel;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Meseret on 1/25/2018.
 */

public class TransporterRecyclerviewAdapter extends RecyclerView.Adapter<TransporterRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<TransporterAdapterModel> albumList;
    private SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView driver_name,car_type,weight;
        public Button choose;
        public ImageView car_photo;

        public MyViewHolder(View view) {
            super(view);
            driver_name = (TextView) view.findViewById(R.id.driver_name);
            car_photo = (ImageView) view.findViewById(R.id.car_photo);
            car_type=(TextView)view.findViewById(R.id.car_type);
            weight=(TextView)view.findViewById(R.id.car_weight);
            choose=(Button)view.findViewById(R.id.choosed);

        }
    }


    public TransporterRecyclerviewAdapter(Context mContext, List<TransporterAdapterModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.near_by_transport_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TransporterAdapterModel model = albumList.get(position);
        String driver_names="<font COLOR=\'#ffffff\'><b>" + "Driver name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getDriver_name() + "</font>";
        String car_types="<font COLOR=\'#ffffff\'><b>" + "Car type:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getType_of_car() + "</font>";
        String weights="<font COLOR=\'#ffffff\'><b>" + "Car weight:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getWeight()+ "</font>";

        holder.driver_name.setText(Html.fromHtml(driver_names));
        holder.car_type.setText(Html.fromHtml(car_types));
        holder.weight.setText(Html.fromHtml(weights));
        holder.choose.setTag(model.getId());
        // loading album cover using Glide library
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getCar_photo()).placeholder(R.drawable.background_tile2_small).into(holder.car_photo);
        holder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
