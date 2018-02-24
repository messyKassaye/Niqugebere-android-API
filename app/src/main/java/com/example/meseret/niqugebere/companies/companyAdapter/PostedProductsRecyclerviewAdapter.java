package com.example.meseret.niqugebere.companies.companyAdapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.CompaniesProductDetails;
import com.example.meseret.niqugebere.companies.ProductOrderActivity;
import com.example.meseret.niqugebere.companies.companyAdapterModel.PostedProductsAdapterModel;
import com.example.meseret.niqugebere.companies.companyAdapterModel.ProductAdapterModel;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Meseret on 1/25/2018.
 */

public class PostedProductsRecyclerviewAdapter extends RecyclerView.Adapter<PostedProductsRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<PostedProductsAdapterModel> albumList;
    private SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView product_name,unit,price,sub_name;
        public Button order;
        public ImageView product_photo;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            product_name = (TextView) view.findViewById(R.id.posted_product_name);
            product_photo = (ImageView) view.findViewById(R.id.posted_product_photo);
            unit=(TextView)view.findViewById(R.id.posted_measurement_unit_and_quantity);
            price=(TextView)view.findViewById(R.id.posted_product_unit_price);
            sub_name=(TextView)view.findViewById(R.id.posted_product_sub_category);
            order=(Button)view.findViewById(R.id.post_order);
        }
    }


    public PostedProductsRecyclerviewAdapter(Context mContext, List<PostedProductsAdapterModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posted_products_layouts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PostedProductsAdapterModel model = albumList.get(position);
        String sub_name="<font COLOR=\'#ffffff\'><b>" + "Product type:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getSub_category_name() + "</font>";
        String product_name="<font COLOR=\'#ffffff\'><b>" + "Product name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getProduct_name() + "</font>";
        String unit_name="<font COLOR=\'#ffffff\'><b>" + "Unit:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getQuantity()+model.getUnit() + "</font>";
        String price_value="<font COLOR=\'#ffffff\'><b>" + "Price:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getUnit_price()+" ETB" + "</font>";
        holder.sub_name.setText(Html.fromHtml(sub_name));
        holder.product_name.setText(Html.fromHtml(product_name));
        holder.unit.setText(Html.fromHtml(unit_name));
        holder.price.setText(Html.fromHtml(price_value));
        holder.order.setTag(model.getId());
        holder.order.setText("Order now");
        // loading album cover using Glide library
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getProduct_photo()).placeholder(R.drawable.background_tile2_small).into(holder.product_photo);
        holder.order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferences=mContext.getSharedPreferences("company_data",0);
                Intent intent=new Intent(mContext, ProductOrderActivity.class);
                intent.putExtra("posted_product_id",view.getTag().toString());
                intent.putExtra("product_name",model.getProduct_name());
                intent.putExtra("company_id",preferences.getString("id",null));
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
