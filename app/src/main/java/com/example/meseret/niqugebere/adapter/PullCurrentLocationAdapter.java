package com.example.meseret.niqugebere.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.Woreda;
import com.example.meseret.niqugebere.fragments.PullFragment;
import com.example.meseret.niqugebere.fragments.PushFragment;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by meseret on 2/25/18.
 */

public class PullCurrentLocationAdapter extends RecyclerView.Adapter<PullCurrentLocationAdapter.MyViewHolder> {

    private Context mContext;
    private List<Woreda> albumList;
    private boolean is_runnig=true;
    private PushFragment fragment;
    private CustomFilter filter;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public Button product_name;


        public MyViewHolder(View view) {
            super(view);
            product_name = (Button)view.findViewById(R.id.product_select_btn);

        }
    }



    public PullCurrentLocationAdapter(Context mContext, List<Woreda> albumList, PushFragment fragments) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.fragment=fragments;
        filter=new CustomFilter(this);
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
                .inflate(R.layout.product_select_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Woreda model = albumList.get(position);
        holder.product_name.setText(model.getName());
        holder.product_name.setTag(model.getId());
        // loading album cover using Glide library
        //Glide.with(mContext).load(ProjectStatic.IMAPGE_PATH+model.getPhoto_path()).into(holder.mall_phtoto);
        holder.product_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String product_id=view.getTag().toString();
                String product_names=model.getName();
                fragment.displayPullButton(product_id,product_names);
            }
        });

    }


    public Filter getfFilter(){
        return filter;
    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    class  CustomFilter extends Filter{
        private PullCurrentLocationAdapter adapter;
        private List<Woreda> backupData;
        public CustomFilter(PullCurrentLocationAdapter adapter) {
            super();
            this.adapter = adapter;
            backupData=new ArrayList<>();
        }

        public List<Woreda> getBackupData() {
            //fragment.displayProgressBar(true);
            Retrofit retrofit=getUserAPIretrofit();
            CompanyClient client=retrofit.create(CompanyClient.class);
            Call<List<Woreda>> call=client.getWoreda();
            call.enqueue(new Callback<List<Woreda>>() {
                @Override
                public void onResponse(Call<List<Woreda>> call, Response<List<Woreda>> response) {
                    if(response.isSuccessful()){
                        //fragment.displayProgressBar(false);
                        for (int i=0;i<response.body().size();i++){
                            Woreda model=new Woreda();
                            model.setId(response.body().get(i).getId());
                            model.setName(response.body().get(i).getName());
                            backupData.add(model);
                        }
                    }
                }

                @Override
                public void onFailure(Call<List<Woreda>> call, Throwable throwable) {

                }
            });
            return backupData;
        }


        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            albumList.clear();
            final FilterResults results = new FilterResults();
            if (constraint.length() == 0) {
                albumList.addAll(getBackupData());
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (final Woreda mWords : getBackupData()) {
                    if (mWords.getName().toLowerCase().startsWith(filterPattern)) {
                        albumList.add(mWords);
                    }
                }
            }
            results.values=albumList;
            results.count=albumList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            this.adapter.notifyDataSetChanged();
            Log.d("size update:",String.valueOf(getBackupData().size()));
        }
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