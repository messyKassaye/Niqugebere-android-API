package com.example.meseret.niqugebere.adapter;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adaptermodel.PullAdapterModel;
import com.example.meseret.niqugebere.helpers.CircleTransform;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.SuppliesRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.SuppliesAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meseret on 2/15/2018.
 */

public class PullRecyclerviewAdapter extends RecyclerView.Adapter<PullRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<PullAdapterModel> albumList;
    private boolean is_runnig=true;
    private String supplies_id;
    private String token;
    private ProgressBar pr;
    private LinearLayout supply_mainlayout;
    private TextView supply_success_message;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView company_photo,supplied_product_photo;
        public TextView company_name,supplied_date,product_category_name,product_sub_category_name,supplied_product_name,price,woreda_name,special_name;
        public Button apply;
        public MyViewHolder(View view) {
            super(view);
            company_photo = (ImageView)view.findViewById(R.id.supply_company_image);
            supplied_product_photo=(ImageView)view.findViewById(R.id.supply_product_image);
            company_name=(TextView)view.findViewById(R.id.supply_company_name);
            supplied_date=(TextView)view.findViewById(R.id.supply_date);
            product_category_name=(TextView)view.findViewById(R.id.product_category_name);
            product_sub_category_name=(TextView)view.findViewById(R.id.product_sub_category_name);
            supplied_product_name=(TextView)view.findViewById(R.id.supplied_product_name);
            price=(TextView)view.findViewById(R.id.supply_price);
            woreda_name=(TextView)view.findViewById(R.id.supply_woreda);
            special_name=(TextView)view.findViewById(R.id.supply_special_name);
            apply=(Button)view.findViewById(R.id.supply_apply);

        }
    }


    public PullRecyclerviewAdapter(Context mContext, List<PullAdapterModel> albumList) {
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
                .inflate(R.layout.pull_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final PullAdapterModel model = albumList.get(position);

        Glide.with(mContext)
                .load(Uri.parse(Projectstatics.IMAPGE_PATH+model.getCompany_photo())) // add your image url
                .transform(new CircleTransform(mContext)) // applying the image transformer
                .into(holder.company_photo);
        Picasso.with(mContext).load(Projectstatics.IMAPGE_PATH+model.getProduct_photo()).placeholder(R.drawable.background_tile2_small).into(holder.supplied_product_photo);
        holder.company_name.setText(model.getCompany_name());
        String product_name="<font COLOR=\'#ffffff\'><b>" + "Category name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getCategory_name() + "</font>";
        String product_sub_name="<font COLOR=\'#ffffff\'><b>" + "Category type:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getSub_category_name() + "</font>";
        String supplied_product_name="<font COLOR=\'#ffffff\'><b>" + "Product name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getProduct_name() + "</font>";
        String supplied_product_price="<font COLOR=\'#ffffff\'><b>" + "Unit price:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getPrice()+" ETB"+ "</font>";
        String supplied_woreda="<font COLOR=\'#ffffff\'><b>" + "Woreda:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getWoreda()+"</font>";
        String supplied_special_name="<font COLOR=\'#ffffff\'><b>" + "Specific name at "+model.getWoreda()+":  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getSpecial_name()+"</font>";
        holder.product_category_name.setText(Html.fromHtml(product_name));
        holder.product_sub_category_name.setText(Html.fromHtml(product_sub_name));
        holder.supplied_product_name.setText(Html.fromHtml(supplied_product_name));
        holder.price.setText(Html.fromHtml(supplied_product_price));
        holder.woreda_name.setText(Html.fromHtml(supplied_woreda));
        holder.special_name.setText(Html.fromHtml(supplied_special_name));
        holder.apply.setTag(model.getId());
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSupplies_id(view.getTag().toString());

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