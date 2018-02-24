package com.example.meseret.niqugebere.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adaptermodel.CompaniesModel;
import com.example.meseret.niqugebere.adaptermodel.MarketItemsAdapterModel;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.model.Companies;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meseret on 2/3/2018.
 */

public class ApprovalRecyclerviewAdapter extends RecyclerView.Adapter<ApprovalRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<CompaniesModel> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView company_name,owner_name,phone;
        public Button approve,call;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            company_name = (TextView) view.findViewById(R.id.approval_company_name);
            owner_name=(TextView)view.findViewById(R.id.owner_name);
            phone=(TextView) view.findViewById(R.id.owner_phone);
            approve=(Button)view.findViewById(R.id.approve_company_btn);
            call=(Button)view.findViewById(R.id.call_to_approve);
        }
    }


    public ApprovalRecyclerviewAdapter(Context mContext, List<CompaniesModel> albumList) {
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
                .inflate(R.layout.approval_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CompaniesModel model = albumList.get(position);
        String product_name="<font COLOR=\'#ffffff\'><b>" + "Company Name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getCompany_name() + "</font>";
        String unit_price="<font COLOR=\'#ffffff\'><b>" + "Owner name:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getOwner_name()+ "</font>";
        String contact="<font COLOR=\'#ffffff\'><b>" + "Phone Number:  " + "</b></font>"
                + "<font COLOR=\'#05B070\'>" +model.getPhone()+ "</font>";
        holder.company_name.setText(Html.fromHtml(product_name));
        holder.owner_name.setText(Html.fromHtml(unit_price));
        holder.phone.setText(Html.fromHtml(contact));
        holder.approve.setTag(model.getId());
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=view.getTag().toString();
                Retrofit retrofit=getUserAPIretrofit();
                MainClient client=retrofit.create(MainClient.class);
                Call<ResponseToken> call=client.approveCompanyRequest(id);
                call.enqueue(new Callback<ResponseToken>() {
                    @Override
                    public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                        if (response.body().getStatus().equals("ok")){

                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseToken> call, Throwable t) {

                    }
                });

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
    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }
}