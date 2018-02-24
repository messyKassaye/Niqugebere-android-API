package com.example.meseret.niqugebere.profile.transporter.transporterFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.TransporterRecyclerviewAdapter;
import com.example.meseret.niqugebere.companies.companyAdapterModel.TransporterAdapterModel;
import com.example.meseret.niqugebere.profile.transporter.transporterAdapter.TransportRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.transporter.transporterAdapterModel.TransportAdapterModel;
import com.example.meseret.niqugebere.profile.transporter.transporterClient.TransportClient;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.Transport;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransporterHomeFragment extends Fragment {

    private String token;
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private List<TransportAdapterModel>list;
    private TransportRecyclerviewAdapter adapter;
    public TransporterHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_transporter_home, container, false);

        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",""));

        list=new ArrayList<>();
        adapter=new TransportRecyclerviewAdapter(getActivity(),list);
        recyclerView=(RecyclerView)view.findViewById(R.id.transport_home_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit=getUserAPIretrofit();
        TransportClient client=retrofit.create(TransportClient.class);
        Call<List<Transport>> call=client.getTransportations(getToken());
        call.enqueue(new Callback<List<Transport>>() {
            @Override
            public void onResponse(Call<List<Transport>> call, Response<List<Transport>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        TransportAdapterModel model=new TransportAdapterModel();
                        model.setId(response.body().get(i).getId());
                        model.setTo_company(response.body().get(i).getTo_company());
                        model.setTo_woreda(response.body().get(i).getTo_woreda());
                        model.setTitle(response.body().get(i).getTitle());
                        model.setTotal_quantity(response.body().get(i).getTotal_quantity());
                        model.setProduct_name(response.body().get(i).getProduct_name());
                        model.setProduct_sub_name(response.body().get(i).getProduct_sub_name());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Transport>> call, Throwable t) {

            }
        });

        return view;
    }
    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
