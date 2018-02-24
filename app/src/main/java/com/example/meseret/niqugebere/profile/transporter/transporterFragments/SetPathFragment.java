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
import com.example.meseret.niqugebere.profile.transporter.transporterAdapter.PathsrecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.transporter.transporterAdapterModel.PathsAdapterModel;
import com.example.meseret.niqugebere.profile.transporter.transporterClient.TransportClient;
import com.example.meseret.niqugebere.profile.transporter.transporterModel.Paths;
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
public class SetPathFragment extends Fragment {

   private String token;
    private SharedPreferences preferences;

    private List<PathsAdapterModel> list;
    private PathsrecyclerviewAdapter adapter;
    private RecyclerView recyclerView;
    public SetPathFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_set_path, container, false);
        preferences=getActivity().getSharedPreferences("token",0);

        setToken(preferences.getString("token",""));

        list=new ArrayList<>();
        adapter=new PathsrecyclerviewAdapter(getActivity(),list);
        recyclerView=(RecyclerView)view.findViewById(R.id.path_recycleriew);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit=getUserAPIretrofit();
        TransportClient client=retrofit.create(TransportClient.class);
        Call<List<Paths>> call=client.getPaths(getToken());
        call.enqueue(new Callback<List<Paths>>() {
            @Override
            public void onResponse(Call<List<Paths>> call, Response<List<Paths>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        PathsAdapterModel model=new PathsAdapterModel();
                        model.setId(response.body().get(i).getId());
                        model.setStart(response.body().get(i).getStart());
                        model.setEnd(response.body().get(i).getEnd());
                        list.add(model);

                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Paths>> call, Throwable t) {

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
