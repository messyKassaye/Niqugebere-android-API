package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.helpers.CircleTransform;
import com.example.meseret.niqugebere.profile.cfsc.CFSCProfile;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.InventoryPayment;
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
public class ShowMyInventoryFragment extends Fragment {

    private SharedPreferences preferences;
    private String token;
    private TextView company_name,total_payment;
    private ImageView company_photo;
    public ShowMyInventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_my_inventory, container, false);
        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        company_name=(TextView)view.findViewById(R.id.payed_company_name);
        company_photo=(ImageView)view.findViewById(R.id.payed_company_photo);
        total_payment=(TextView)view.findViewById(R.id.total_payment);

        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<InventoryPayment>>call=client.getMyInventory(getToken());
        call.enqueue(new Callback<List<InventoryPayment>>() {
            @Override
            public void onResponse(Call<List<InventoryPayment>> call, Response<List<InventoryPayment>> response) {
               if (response.isSuccessful()){
                   if (response.body().size()>0){
                       Glide.with(getActivity())
                               .load(Uri.parse(Projectstatics.IMAPGE_PATH+response.body().get(0).getPhoto_path())) // add your image url
                               .transform(new CircleTransform(getActivity())) // applying the image transformer
                               .into(company_photo);
                       company_name.setText(response.body().get(0).getName());
                       if (response.body().get(0).getTotal_payment().equals("")){
                           total_payment.setText("You haven't send any inventory");
                       }else {
                           String payment="<font COLOR=\'#242424\'><b>" + "Your total payment:  " + "</b></font>"
                                   + "<font COLOR=\'#05B070\'>" +response.body().get(0).getTotal_payment()+ "  ETB</font>";
                           total_payment.setText(Html.fromHtml(payment));
                       }
                   }else{
                       total_payment.setTextColor(Color.BLACK);
                       total_payment.setText("You haven't send any inventory");
                   }
               }
            }

            @Override
            public void onFailure(Call<List<InventoryPayment>> call, Throwable t) {

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
