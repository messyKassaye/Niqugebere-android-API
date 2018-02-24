package com.example.meseret.niqugebere.companies.companyAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.CompaniesProductDetails;
import com.example.meseret.niqugebere.companies.companyAdapterModel.ProductAdapterModel;
import com.example.meseret.niqugebere.companies.fragments.CompanyProductFragment;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Meseret on 1/23/2018.
 */

public class ProductRecyclerviewAdapter extends RecyclerView.Adapter<ProductRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductAdapterModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView toppsellername;
        public ImageView top_sellers_phtoto;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            toppsellername = (TextView) view.findViewById(R.id.mall_name);
            top_sellers_phtoto = (ImageView) view.findViewById(R.id.mall_image);
        }
    }


    public ProductRecyclerviewAdapter(Context mContext, List<ProductAdapterModel> albumList) {
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
        final ProductAdapterModel model = albumList.get(position);
        holder.toppsellername.setText(model.getName());
        holder.top_sellers_phtoto.setTag(model.getId());
        // loading album cover using Glide library
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getPhoto()).placeholder(R.drawable.background_tile2_small).into(holder.top_sellers_phtoto);
        holder.top_sellers_phtoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    /*kittenDetails.setSharedElementEnterTransition(new DetailsBackPortTransition());
                    kittenDetails.setEnterTransition(new com.transitionseverywhere.Fade());
                    setExitTransition(new com.transitionseverywhere.Fade());
                    kittenDetails.setSharedElementReturnTransition(new DetailsBackPortTransition());*/
                    Toast.makeText(mContext,"hello",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent=new Intent(mContext, CompaniesProductDetails.class);
                    intent.putExtra("company_id",view.getTag().toString());
                    intent.putExtra("product_name",model.getName());
                    intent.putExtra("product_id",model.getProduct_id());
                    mContext.startActivity(intent);
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