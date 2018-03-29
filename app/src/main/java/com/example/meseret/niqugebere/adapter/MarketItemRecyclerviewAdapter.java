package com.example.meseret.niqugebere.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meseret.niqugebere.MarketDetailActivity;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adaptermodel.MarketItemsAdapterModel;
import com.example.meseret.niqugebere.adaptermodel.MarketsModelAdapter;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Meseret on 1/29/2018.
 */

public class MarketItemRecyclerviewAdapter extends RecyclerView.Adapter<MarketItemRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<MarketItemsAdapterModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,unit_price,posted_by;
        public ImageView product_phtoto;
        public Button contact;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.product_name);
            product_phtoto = (ImageView) view.findViewById(R.id.product_photo);
            unit_price=(TextView)view.findViewById(R.id.product_price);
            contact=(Button)view.findViewById(R.id.contact);
            posted_by=(TextView)view.findViewById(R.id.posted_by);
        }
    }


    public MarketItemRecyclerviewAdapter(Context mContext, List<MarketItemsAdapterModel> albumList) {
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
                .inflate(R.layout.market_item_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final MarketItemsAdapterModel model = albumList.get(position);
        String product_name="<font COLOR=\'#ffffff\'><b>" + "Name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getProduct_name() + "</font>";
        String unit_price="<font COLOR=\'#ffffff\'><b>" + "Price:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getUnit_price() + "</font>";
        String posted_by="<font COLOR=\'#ffffff\'><b>" + "Posted by:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getName() + "</font>";
        holder.product_name.setText(Html.fromHtml(product_name));
        holder.unit_price.setText(Html.fromHtml(unit_price));
        holder.posted_by.setText(Html.fromHtml(posted_by));
        holder.contact.setTag(model.getId());
        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getProduct_photo()).placeholder(R.drawable.background_tile2_small).into(holder.product_phtoto);
        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if(Build.VERSION.SDK_INT > 22)
                    {
                        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            Activity activity=(Activity)(AppCompatActivity)holder.contact.getContext();
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 101);

                            return;
                        }

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:0944181080"));
                        mContext.startActivity(callIntent);

                    }
                    else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:0944181080"));
                        mContext.startActivity(callIntent);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

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