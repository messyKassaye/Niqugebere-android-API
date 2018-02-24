package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.CFSCProductsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meseret on 1/29/2018.
 */

public class CFSCProductReyclerviewAdapter extends RecyclerView.Adapter<CFSCProductReyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<CFSCProductsAdapterModel> albumList;
    private boolean is_runnig=true;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button product_name;


        public MyViewHolder(View view) {
            super(view);
            product_name = (Button)view.findViewById(R.id.product_select_btn);
        }
    }


    public CFSCProductReyclerviewAdapter(Context mContext, List<CFSCProductsAdapterModel> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_select_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CFSCProductsAdapterModel model = albumList.get(position);
        holder.product_name.setText(model.getName());
        holder.product_name.setTag(model.getId());
        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);
        holder.product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (is_runnig()){
                    Retrofit retrofit=getUserAPIretrofit();
                    CFSCClient client=retrofit.create(CFSCClient.class);
                    Call<ResponseToken>call=client.companyProductSelection(model.getToken(),view.getTag().toString());
                    call.enqueue(new Callback<ResponseToken>() {
                        @Override
                        public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                           if (response.body().getStatus().equals("ok")){
                               holder.product_name.setVisibility(View.GONE);
                           }
                        }

                        @Override
                        public void onFailure(Call<ResponseToken> call, Throwable t) {

                        }
                    });
                }else {
                   Toast.makeText(mContext,"Please wait we are making on it ):",Toast.LENGTH_LONG).show();
                }
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