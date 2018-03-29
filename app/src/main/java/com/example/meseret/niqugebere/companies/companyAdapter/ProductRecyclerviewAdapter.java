package com.example.meseret.niqugebere.companies.companyAdapter;

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
import android.widget.Toast;

import com.example.meseret.niqugebere.MainActivity;
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
        public TextView category_name,product_name,price;
        public ImageView product_photo;
        public CardView cardView;
        public Button contact;

        public MyViewHolder(View view) {
            super(view);
            category_name = (TextView) view.findViewById(R.id.posted_product_category_name);
            product_name=(TextView)view.findViewById(R.id.posted_product_name);
            price=(TextView)view.findViewById(R.id.posted_product_price);
            product_photo = (ImageView) view.findViewById(R.id.posted_product_photo);
            contact=(Button)view.findViewById(R.id.posted_product_contact_btn);
        }
    }


    public ProductRecyclerviewAdapter(Context mContext, List<ProductAdapterModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final ProductAdapterModel model = albumList.get(position);
        String category_name="<font COLOR=\'#ffffff\'><b>" + "Category Type:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getCategory_name() + "</font>";
        String product_name="<font COLOR=\'#ffffff\'><b>" + "Product name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getName() + "</font>";
        String supplied_product_price="<font COLOR=\'#ffffff\'><b>" + "Unit price:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getPrice()+" ETB"+ "</font>";
        String price_value="<font COLOR=\'#ffffff\'><b>" + "Price:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getPrice()+ "</font>";
        holder.category_name.setText(Html.fromHtml(category_name));
        holder.product_name.setText(Html.fromHtml(product_name));
        holder.price.setText(Html.fromHtml(price_value));
        holder.contact.setTag(model.getId());
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
        // loading album cover using Glide library
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getPhoto()).placeholder(R.drawable.background_tile2_small).into(holder.product_photo);

    }


    /**
     * Click listener for popup menu items
     */

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}