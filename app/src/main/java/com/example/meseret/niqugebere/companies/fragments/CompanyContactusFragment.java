package com.example.meseret.niqugebere.companies.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.ContactUs;
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
public class CompanyContactusFragment extends Fragment {

  private View view;
    private SharedPreferences preferences;
    private TextView region,zone,woreda,special_name,phone;
    public CompanyContactusFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_company_contactus, container, false);

        preferences=getActivity().getSharedPreferences("company_data",0);

        region=(TextView)view.findViewById(R.id.company_region);
        zone=(TextView)view.findViewById(R.id.company_zone);
        woreda=(TextView)view.findViewById(R.id.company_woreda);
        special_name=(TextView)view.findViewById(R.id.company_special_name);
        phone=(TextView)view.findViewById(R.id.company_phone);


        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<ContactUs>> call=client.getCompanyContactUs(preferences.getString("id",null));
        call.enqueue(new Callback<List<ContactUs>>() {
            @Override
            public void onResponse(Call<List<ContactUs>> call, Response<List<ContactUs>> response) {
               if (response.isSuccessful()){
                   String region_name="<font COLOR=\'#ffffff\'><b>" + "Region:  " + "</b></font>"
                           + "<font COLOR=\'#05B070\'>" +response.body().get(0).getRegion() + "</font>";
                   String zone_name="<font COLOR=\'#ffffff\'><b>" + "Zone:  " + "</b></font>"
                           + "<font COLOR=\'#05B070\'>" +response.body().get(0).getZone() + "</font>";
                   String woreda_name="<font COLOR=\'#ffffff\'><b>" + "Woreda:  " + "</b></font>"
                           + "<font COLOR=\'#05B070\'>" +response.body().get(0).getWoreda() + "</font>";
                   String special_names="<font COLOR=\'#ffffff\'><b>" + "Special name:  " + "</b></font>"
                           + "<font COLOR=\'#05B070\'>" +response.body().get(0).getSpecial_name()+ "</font>";
                   String phones="<font COLOR=\'#ffffff\'><b>" + "Phone Number:  " + "</b></font>"
                           + "<font COLOR=\'#05B070\'>" +response.body().get(0).getPhone()+ "</font>";
                   region.setText(Html.fromHtml(region_name));
                   zone.setText(Html.fromHtml(zone_name));
                   woreda.setText(Html.fromHtml(woreda_name));
                   special_name.setText(Html.fromHtml(special_names));
                   phone.setText(Html.fromHtml(phones));
               }
            }

            @Override
            public void onFailure(Call<List<ContactUs>> call, Throwable t) {

            }
        });


        return  view;
    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

}
