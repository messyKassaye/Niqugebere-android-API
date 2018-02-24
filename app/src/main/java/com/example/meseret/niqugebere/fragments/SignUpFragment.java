package com.example.meseret.niqugebere.fragments;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.clients.UsersClient;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.Woreda;
import com.example.meseret.niqugebere.helpers.JustifiedTextView;
import com.example.meseret.niqugebere.model.CarType;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.model.Roles;
import com.example.meseret.niqugebere.model.Users;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private ViewFlipper flipper;
    private View first_view,farm_centers_dealer_comapny_layout,third_view,registration_success;

    private EditText first_name_edt,company_name_edt,tin_no_edt,phone_number_edt,spacial_name_edt,password_edt,re_password_edt;
    private Button next,next2,signup;
    private TextView erro_shower_first,error_shower_second,error_shower_third;
    private Spinner registration_type_spinner,zone_spinner;
    private CustomSpinnerAdapter registration_type,zone_adapter,woreda_adapter;
    private Resources res;
    private ArrayList<String> region_list,zone_list;
    private String first_name,last_name,company_name,tin_no,phone_number,region,zone,woreda,spacial_name,password;
    private List<Integer> region_holder,zone_holder,woreda_holder;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private String company_id;
    private ArrayList<String> category_list;
    private ArrayList<Integer> category_id_holder;
    private int category_id;
    private CustomSpinnerAdapter registration_type_adapter;


    private Spinner woreda_spinner;
    private ArrayList<String> woreda_list;
    private CustomSpinnerAdapter adapter;
    private ArrayList<Integer>id_holder;
    private int woreda_id;


    private ProgressBar pr;

    private EditText plate_number_edt;
    private Button transport_next;
    private TextView signup_info;
    private LinearLayout singup_info,select_type_view;
    private JustifiedTextView justifiedTextView,select_justify;
    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_sign_up, container, false);

        preferences=getActivity().getSharedPreferences("signup",0);
        editor=preferences.edit();


        flipper=(ViewFlipper)view.findViewById(R.id.signup_flipper);
        pr=(ProgressBar)view.findViewById(R.id.signup_pr);

        first_view=LayoutInflater.from(getActivity()).inflate(R.layout.singup_firstview,null);
        farm_centers_dealer_comapny_layout=LayoutInflater.from(getActivity()).inflate(R.layout.farm_center_and_dealer_company_layout,null);
        third_view=LayoutInflater.from(getActivity()).inflate(R.layout.signup_thirdview,null);
        registration_success=LayoutInflater.from(getActivity()).inflate(R.layout.registration_success_layout,null);

        justifiedTextView=new JustifiedTextView(getActivity(),getResources().getString(R.string.create_account_info));
        singup_info=(LinearLayout)first_view.findViewById(R.id.singup_info);
        singup_info.addView(justifiedTextView);

        select_justify=new JustifiedTextView(getActivity(),getResources().getString(R.string.registration_type));
        select_type_view=(LinearLayout)first_view.findViewById(R.id.select_type_view);
        select_type_view.addView(select_justify);

        first_name_edt=(EditText)first_view.findViewById(R.id.first_name);
        tin_no_edt=(EditText)first_view.findViewById(R.id.signup_tin_no);
        phone_number_edt=(EditText)first_view.findViewById(R.id.phone_no);
        erro_shower_first=(TextView)first_view.findViewById(R.id.first_signup_error);
        registration_type_spinner=(Spinner)first_view.findViewById(R.id.registration_type_spinner);
        category_list=new ArrayList<>();
        category_list.add(getResources().getString(R.string.registration_type_for_spinner));
        registration_type_adapter=new CustomSpinnerAdapter(getActivity(), R.layout.spinner_dropdown, category_list, res);
        registration_type_spinner.setAdapter(registration_type_adapter);
        category_id_holder=new ArrayList<>();

        Retrofit retrofit=getUserAPIretrofit();
        MainClient client=retrofit.create(MainClient.class);
        Call<List<Roles>>call=client.getRoles();
        call.enqueue(new Callback<List<Roles>>() {
            @Override
            public void onResponse(Call<List<Roles>> call, Response<List<Roles>> response) {
                if (response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        category_list.add(response.body().get(i).getName());
                        category_id_holder.add(response.body().get(i).getId());
                    }
                }

            }

            @Override
            public void onFailure(Call<List<Roles>> call, Throwable t) {

            }
        });
        registration_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=1){
                    setCategory_id(category_id_holder.get(i-1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        next=(Button)first_view.findViewById(R.id.next_btn);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String first_name_text= first_name_edt.getText().toString();
                String first_name_value = "";
                String last_name_value = "";
                String tin_no_value = tin_no_edt.getText().toString();
                String phone_number_value = phone_number_edt.getText().toString();
                int category_id = getCategory_id();

                if (first_name_text.equals("")) {
                    erro_shower_first.setVisibility(View.VISIBLE);
                    erro_shower_first.setText(R.string.enter_name_error);
                }  else if (tin_no_value.equals("")) {
                    erro_shower_first.setVisibility(View.VISIBLE);
                    erro_shower_first.setText(R.string.enter_tin_error);
                } else if (phone_number_value.equals("")) {
                    erro_shower_first.setVisibility(View.VISIBLE);
                    erro_shower_first.setText(R.string.enter_phone_error);
                } else if (category_id==0){
                    erro_shower_first.setVisibility(View.VISIBLE);
                    erro_shower_first.setText(R.string.select_registration_type_error);
                } else {
                    if (!first_name_text.contains(" ")){
                        erro_shower_first.setVisibility(View.VISIBLE);
                        erro_shower_first.setText(R.string.enter_lastname_error);
                    }else {
                        first_name_value=first_name_text.substring(0,first_name_text.indexOf(" "));
                        last_name_value=first_name_text.substring(first_name_text.lastIndexOf(" "),first_name_text.length());

                            editor.putString("first_name", first_name_value);
                            editor.commit();

                            editor.putString("last_name", last_name_value);
                            editor.commit();

                            editor.putString("tin_no", tin_no_value);
                            editor.commit();

                            editor.putString("phone_number", phone_number_value);
                            editor.commit();

                            editor.putString("category_id",""+category_id);
                            editor.commit();
                        flipper.removeView(farm_centers_dealer_comapny_layout);
                        flipper.addView(farm_centers_dealer_comapny_layout);
                        flipper.showNext();

                    }

                }

            }
        });




        company_name_edt=(EditText)farm_centers_dealer_comapny_layout.findViewById(R.id.company_name);
        woreda_spinner=(Spinner)farm_centers_dealer_comapny_layout.findViewById(R.id.woreda_spinner);
        spacial_name_edt=(EditText)farm_centers_dealer_comapny_layout.findViewById(R.id.spacial_name);

        woreda_list=new ArrayList<>();
        woreda_list.add(getResources().getString(R.string.current_company_living_address));
        adapter=new CustomSpinnerAdapter(getActivity(), R.layout.spinner_dropdown, woreda_list, res);
        woreda_spinner.setAdapter(adapter);
        id_holder=new ArrayList<>();
        CompanyClient client2=retrofit.create(CompanyClient.class);
        Call<List<Woreda>> call2=client2.getWoreda();
        call2.enqueue(new Callback<List<Woreda>>() {
            @Override
            public void onResponse(Call<List<Woreda>> call, Response<List<Woreda>> response) {
               if(response.isSuccessful()){
                   if (response.body().size()>0){
                       for (int i=0;i<response.body().size();i++){
                           woreda_list.add(response.body().get(i).getName());
                           id_holder.add(response.body().get(i).getId());
                       }
                   }
               }
            }

            @Override
            public void onFailure(Call<List<Woreda>> call, Throwable t) {

            }
        });
        woreda_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=1){
                    setWoreda_id(id_holder.get(i-1));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        next2=(Button)farm_centers_dealer_comapny_layout.findViewById(R.id.next2);

        next2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String spacial_name_value=spacial_name_edt.getText().toString();
                String company_name_value=company_name_edt.getText().toString();
                int woreda_value=getWoreda_id();
                if (company_name_value.equals("")){
                    error_shower_second.setVisibility(View.VISIBLE);
                    error_shower_second.setText(R.string.enter_companyname_error);
                }else if (woreda_value==0){
                    error_shower_second.setVisibility(View.VISIBLE);
                    error_shower_second.setText(R.string.select_woreda_error);
                }else if (spacial_name_value.equals("")){
                    error_shower_second.setVisibility(View.VISIBLE);
                    error_shower_second.setText(R.string.spacial_name_error);
                }else {
                    editor.putString("company_name",company_name_value);
                    editor.commit();

                    editor.putString("woreda_id",""+woreda_value);
                    editor.commit();

                    editor.putString("special_name",spacial_name_value);
                    editor.commit();

                    flipper.removeView(third_view);
                    flipper.addView(third_view);
                    flipper.showNext();
                }
            }
        });



        password_edt=(EditText)third_view.findViewById(R.id.password);
        re_password_edt=(EditText)third_view.findViewById(R.id.re_password);
        error_shower_third=(TextView)third_view.findViewById(R.id.third_signup_error);
        signup = (Button)third_view.findViewById(R.id.signup);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password_value= password_edt.getText().toString();
                String re_password_value= re_password_edt.getText().toString();
                if (password_value.equals("")){
                    error_shower_third.setVisibility(View.VISIBLE);
                    error_shower_third.setText(R.string.password_erro);
                }else if (re_password_value.equals("")){
                    error_shower_third.setVisibility(View.VISIBLE);
                    error_shower_third.setText(R.string.password_re_error);
                }else if (!password_value.equals(re_password_value)){
                    error_shower_third.setVisibility(View.VISIBLE);
                    error_shower_third.setText(R.string.not_much_password_erro);
                }else {
                    pr.setVisibility(View.VISIBLE);
                    String first_name_value=preferences.getString("first_name",null);
                    String last_name_value=preferences.getString("last_name",null);
                    String tin_no_value=preferences.getString("tin_no",null);
                    String phone_nuomber_value=preferences.getString("phone_number",null);
                    String categor_id_value=preferences.getString("category_id",null);
                    String company_name_value=preferences.getString("company_name",null);
                    String woreda_id_value=preferences.getString("woreda_id",null);
                    String special_name_value=preferences.getString("special_name",null);
                    String user_password_value=password_value;
                    registerUsers(first_name_value,last_name_value,tin_no_value,phone_nuomber_value,categor_id_value,company_name_value,woreda_id_value,special_name_value,user_password_value);

                }
            }
        });


        flipper.addView(first_view);
        flipper.showNext();

        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
        // set the animation type's to ViewFlipperActivity
        flipper.setInAnimation(in);
        flipper.setOutAnimation(out);
        // set interval time for flipping between views
        flipper.setFlipInterval(3000);
        flipper.setDisplayedChild(6);
        return view;
    }
    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public void registerUsers(String first_name_value,String last_name_value,String tin_no_value,String phone_nuomber_value,String categor_id_value,String company_name_value,String woreda_id_value,String special_name_value,String user_password_value){

        Users users=new Users(first_name_value,last_name_value,tin_no_value,phone_nuomber_value,categor_id_value,company_name_value,woreda_id_value,special_name_value,user_password_value);
        Retrofit retrofit1=getUserAuthAPIretrofit();
        UsersClient client1=retrofit1.create(UsersClient.class);
        Call<ResponseToken> call1=client1.signUp(users);
        call1.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if(response.isSuccessful()){
                    if (response.body().getStatus().equals("ok")){
                        pr.setVisibility(View.GONE);
                        editor.clear();
                        flipper.removeAllViews();
                        flipper.removeView(registration_success);
                        flipper.addView(registration_success);
                        flipper.showNext();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {

            }
        });

    }

    public Retrofit getUserAuthAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.AUTH_URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getWoreda_id() {
        return woreda_id;
    }

    public void setWoreda_id(int woreda_id) {
        this.woreda_id = woreda_id;
    }

}
