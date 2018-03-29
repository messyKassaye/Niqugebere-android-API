package com.example.meseret.niqugebere.profile.transporter.transporterAdapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.transporter.transporterAdapterModel.PathsAdapterModel;
import com.example.meseret.niqugebere.profile.transporter.transporterAdapterModel.TransportAdapterModel;
import com.example.meseret.niqugebere.profile.transporter.transporterClient.TransportClient;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.Transport;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.TransportFrom;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.TransportationBid;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Meseret on 2/18/2018.
 */

public class TransportRecyclerviewAdapter extends RecyclerView.Adapter<TransportRecyclerviewAdapter.MyViewHolder> {

    private Context mContext;
    private List<TransportAdapterModel> albumList;
    private boolean is_runnig=true;
    private String supplies_id;
    private String token;
    private ProgressBar pr;
    private LinearLayout supply_mainlayout;
    private TextView supply_success_message;
    private SharedPreferences preferences;
    private String demandAggrementId;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView from_company,from_woreda,to_company,to_woreda,product_name,product_sub_name,title,total;
        public Button apply;
        public MyViewHolder(View view) {
            super(view);
            from_company=(TextView) view.findViewById(R.id.from_company);
            from_woreda=(TextView)view.findViewById(R.id.from_woreda);
            to_company=(TextView) view.findViewById(R.id.to_company);
            to_woreda=(TextView) view.findViewById(R.id.to_woreda);
            product_name=(TextView)view.findViewById(R.id.product_name);
            product_sub_name=(TextView)view.findViewById(R.id.product_sub_name);
            title=(TextView)view.findViewById(R.id.title);
            total=(TextView)view.findViewById(R.id.total_quantity);
            apply=(Button)view.findViewById(R.id.transport_apply);


        }
    }


    public TransportRecyclerviewAdapter(Context mContext, List<TransportAdapterModel> albumList) {
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
                .inflate(R.layout.transport_home_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final TransportAdapterModel model = albumList.get(position);

        preferences=mContext.getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));
        final Dialog dialog=new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transportations_bid_layouts);
        final EditText supply_quantity=(EditText)dialog.findViewById(R.id.supply_quantity);
        pr=(ProgressBar)dialog.findViewById(R.id.supply_pr);
        supply_mainlayout=(LinearLayout)dialog.findViewById(R.id.supply_main_layout);
        supply_success_message=(TextView)dialog.findViewById(R.id.supply_success_shower);
        Button apply_send=(Button)dialog.findViewById(R.id.apply_btn);

        holder.apply.setTag(model.getId());
        holder.to_woreda.setText("woreda; "+model.getTo_woreda());
        holder.to_company.setText("Company: "+model.getTo_company());
        holder.title.setText("Product name: "+model.getTitle());
        holder.product_name.setText("Product category: "+model.getProduct_name());
        holder.product_sub_name.setText("Product type: "+model.getProduct_sub_name());
         holder.total.setText("Total Quantity: "+model.getTotal_quantity());
        Retrofit retrofit=getUserAPIretrofit();
        TransportClient client=retrofit.create(TransportClient.class);
        final Call<List<TransportFrom>> call=client.getTransportationsFrom(getToken(),model.getId());
        call.enqueue(new Callback<List<TransportFrom>>() {
            @Override
            public void onResponse(Call<List<TransportFrom>> call, Response<List<TransportFrom>> response) {
                holder.from_company.setText("Company: "+response.body().get(0).getFrom_company());
                holder.from_woreda.setText("Woreda: "+response.body().get(0).getFrom_woreda());

            }

            @Override
            public void onFailure(Call<List<TransportFrom>> call, Throwable t) {

            }
        });
        holder.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id=view.getTag().toString();
                setDemandAggrementId(id);
                dialog.show();
            }
        });
        apply_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String price=supply_quantity.getText().toString();
                if(!price.equals("")) {
                    TransportationBid bid=new TransportationBid(getDemandAggrementId(),price);
                    Retrofit retrofit1 = getUserAPIretrofit();
                    TransportClient client1 = retrofit1.create(TransportClient.class);
                    Call<ResponseToken> call1=client1.transportationBid(getToken(),bid);
                    call1.enqueue(new Callback<ResponseToken>() {
                        @Override
                        public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                            if(response.isSuccessful()){
                                dialog.dismiss();
                                Toast.makeText(mContext,"Successfully applied",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseToken> call, Throwable throwable) {

                        }
                    });
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

    public void getNumberOfAppliers(String id){
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<ResponseToken> call=client.getNoOfAppliers(getToken(),id);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {

            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {

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

    public String getDemandAggrementId() {
        return demandAggrementId;
    }

    public void setDemandAggrementId(String demandAggrementId) {
        this.demandAggrementId = demandAggrementId;
    }
}