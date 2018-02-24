package com.example.meseret.niqugebere.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.MainActivity;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.clients.UsersClient;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.model.PushModel;
import com.example.meseret.niqugebere.model.ResponseToken;
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
public class PushFragment extends Fragment {
    private Spinner woreda_spinner,product_spinner,product_category_spinner,availability_spinner;
    private ArrayList<String> woreda_list,product_list,product_category_list,available_list;
    private CustomSpinnerAdapter adapter,product_adapter,product_category_adapter,available_adapter;
    private Resources res;
    private ArrayList<Integer> id_holder,product_id_holder,product_category_holder;
    private int woreda_id,product_id;
    private EditText quantity_edt,description_edt;
    private Button push_btn;
    private TextView error;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private PushModel pushModel;
    private LinearLayout main_layout,success_layout;
    private ProgressBar pr;

    public PushFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_push, container, false);

        preferences=getActivity().getSharedPreferences("push_data",0);
        editor=preferences.edit();
        res=getResources();
        main_layout=(LinearLayout)view.findViewById(R.id.push_main_layout);
        success_layout=(LinearLayout)view.findViewById(R.id.push_success_layout);

        error=(TextView)view.findViewById(R.id.push_error);

        quantity_edt=(EditText)view.findViewById(R.id.push_quantity);
        description_edt=(EditText)view.findViewById(R.id.push_description);

        push_btn=(Button)view.findViewById(R.id.push_btn);
        pr=(ProgressBar)view.findViewById(R.id.push_pr);

        availability_spinner=(Spinner)view.findViewById(R.id.push_product_available_spinner);
        available_list=new ArrayList<>();

            available_list.add(getResources().getString(R.string.select_available_date));
            available_list.add("In 1 day");
            available_list.add("In a week");
            available_list.add("In 2 week");
            available_list.add("in 1 month");
        available_adapter=new CustomSpinnerAdapter(getActivity(),R.layout.spinner_dropdown,available_list,res);
        availability_spinner.setAdapter(available_adapter);
        availability_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>1){
                    String availability=availability_spinner.getSelectedItem().toString();
                    editor.putString("available",availability);
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        product_category_spinner=(Spinner)view.findViewById(R.id.push_product_category_spinner);
        product_category_list=new ArrayList<>();
        product_category_holder=new ArrayList<>();
        product_category_list.add(getResources().getString(R.string.select_your_product_category));
        product_category_adapter=new CustomSpinnerAdapter(getActivity(),R.layout.spinner_dropdown,product_category_list,res);
        product_category_spinner.setAdapter(product_category_adapter);
        product_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=1){
                    int cate_id=i-1;
                    quantity_edt.setVisibility(View.VISIBLE);
                    availability_spinner.setVisibility(View.VISIBLE);
                    push_btn.setVisibility(View.VISIBLE);
                    editor.putString("product_cat_id",""+product_category_holder.get(cate_id));
                    editor.commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        product_spinner=(Spinner)view.findViewById(R.id.push_product_spinner);
        product_list=new ArrayList<>();
        product_id_holder=new ArrayList<>();
        product_list.add(getResources().getString(R.string.select_your_product));
        product_adapter=new CustomSpinnerAdapter(getActivity(),R.layout.spinner_dropdown,product_list,res);
        product_spinner.setAdapter(product_adapter);







        woreda_spinner=(Spinner)view.findViewById(R.id.push_woreda_spinner);
        woreda_list=new ArrayList<>();
        woreda_list.add(getResources().getString(R.string.living_woreda));

        id_holder=new ArrayList<>();

        adapter=new CustomSpinnerAdapter(getActivity(), R.layout.spinner_dropdown, woreda_list, res);
        woreda_spinner.setAdapter(adapter);

        push_btn=(Button)view.findViewById(R.id.push_btn);
        push_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences= getActivity().getSharedPreferences("fcm_token",0);
                Toast.makeText(getActivity(),preferences.getString("token",""),Toast.LENGTH_LONG).show();

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

    public int getWoreda_id() {
        return woreda_id;
    }

    public void setWoreda_id(int woreda_id) {
        this.woreda_id = woreda_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }
}
