package com.example.meseret.niqugebere.companies.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.AboutUs;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyAboutusFragment extends Fragment {

    private View view;
    private TextView about;
    private SharedPreferences preferences;
    public CompanyAboutusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_compant_aboutus, container, false);
        preferences=getActivity().getSharedPreferences("company_data",0);

        about=(TextView)view.findViewById(R.id.companies_aboutus);

        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<AboutUs>> call=client.getCompanyAboutus(preferences.getString("id",null));
        call.enqueue(new Callback<List<AboutUs>>() {
            @Override
            public void onResponse(Call<List<AboutUs>> call, Response<List<AboutUs>> response) {
                if (response.isSuccessful()){
                    about.setText(response.body().get(0).getDescription());
                }
            }

            @Override
            public void onFailure(Call<List<AboutUs>> call, Throwable t) {

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

}
