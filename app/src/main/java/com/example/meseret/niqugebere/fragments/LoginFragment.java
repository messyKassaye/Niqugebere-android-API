package com.example.meseret.niqugebere.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.clients.UsersClient;
import com.example.meseret.niqugebere.model.LoginUser;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.CFSCProfile;
import com.example.meseret.niqugebere.profile.suppliers.SuppliersProfile;
import com.example.meseret.niqugebere.profile.transporter.TransporterProfileActivity;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.example.meseret.niqugebere.systemadmin.SystemAdminActivity;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private EditText tin_no_edt,password_edt;
    private Button login_btn;
    private TextView login_error;
    private ProgressBar pr;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);

        tin_no_edt=(EditText)view.findViewById(R.id.sign_in_tin_no);
        password_edt=(EditText)view.findViewById(R.id.sign_in_password);

        login_error=(TextView)view.findViewById(R.id.login_error_text);
        pr=(ProgressBar)view.findViewById(R.id.create_pr);

        login_btn=(Button)view.findViewById(R.id.login_btn);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tin_no_value=tin_no_edt.getText().toString();
                String password_value=password_edt.getText().toString();
                if (tin_no_value.equals("")){
                    login_error.setVisibility(View.VISIBLE);
                    login_error.setText(getActivity().getResources().getString(R.string.login_tin_no_error));
                }else if (password_value.equals("")){
                    login_error.setVisibility(View.VISIBLE);
                    login_error.setText(getActivity().getResources().getString(R.string.login_passowrd_error));
                }else {
                    pr.setVisibility(View.VISIBLE);
                    if(tin_no_value.equals("smartfarm")&&password_value.equals("malladdis")){
                          Intent intent=new Intent(getActivity(), SystemAdminActivity.class);
                          startActivity(intent);
                    }else {
                        LoginUser loginUser=new LoginUser(tin_no_value,password_value);
                        Retrofit retrofit=getUserAuthAPIretrofit();
                        UsersClient client=retrofit.create(UsersClient.class);
                        Call<ResponseToken> call=client.login(loginUser);
                        call.enqueue(new Callback<ResponseToken>() {
                            @Override
                            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                                if(response.isSuccessful()){
                                    if (response.body().getStatus().equals("ok")){
                                        checkVerification(response.body().getToken());
                                    }else {
                                        pr.setVisibility(View.GONE);
                                        login_error.setVisibility(View.VISIBLE);
                                        login_error.setText(getActivity().getResources().getString(R.string.unknow_users));
                                    }
                                }

                            }

                            @Override
                            public void onFailure(Call<ResponseToken> call, Throwable t) {

                            }
                        });
                    }
                }
            }
        });
        return view;
    }

    public void checkVerification(final String token){
        Retrofit verification=getUserAPIretrofit();
        UsersClient verification_client=verification.create(UsersClient.class);
        Call<List<ResponseToken>> verification_call=verification_client.checkverification(token);
        verification_call.enqueue(new Callback<List<ResponseToken>>() {
            @Override
            public void onResponse(Call<List<ResponseToken>> call, Response<List<ResponseToken>> response) {

                if (response.body().get(0).getStatus().equals("1")){
                    pr.setVisibility(View.GONE);
                    if (response.body().get(0).getToken().equals("1")){
                        Intent intent=new Intent(getActivity(),CFSCProfile.class);
                        intent.putExtra("token",token);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }else if(response.body().get(0).getToken().equals("2")){
                        Intent intents=new Intent(getActivity(),SuppliersProfile.class);
                        intents.putExtra("token",token);
                        getActivity().startActivity(intents);
                        getActivity().finish();
                    }else if(response.body().get(0).getToken().equals("4")){
                        Intent tr_intent=new Intent(getActivity(), TransporterProfileActivity.class);
                        tr_intent.putExtra("token",token);
                        getActivity().startActivity(tr_intent);
                        getActivity().finish();
                    }
                }else {
                    pr.setVisibility(View.GONE);
                    login_error.setVisibility(View.VISIBLE);
                    login_error.setTextColor(Color.GREEN);
                    login_error.setText(getResources().getString(R.string.waiting));
                }
            }

            @Override
            public void onFailure(Call<List<ResponseToken>> call, Throwable t) {

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

    public Retrofit getUserAuthAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

}