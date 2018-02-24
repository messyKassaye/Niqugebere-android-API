package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCInvetory;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCProductCategory;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCProductSubCategory;
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
public class PostInventoryFragment extends Fragment {

    private String company_id;
    private ArrayList<String> category_list,sub_category_list,availability_list;
    private ArrayList<Integer> category_id_holder,sub_category_id_holder;
    private int category_id,sub_category_id;
    private CustomSpinnerAdapter registration_type_adapter,sub_category_adapter;
    private Spinner post_product_category,post_product_sub_category_spinner;
    private Resources res;
    private SharedPreferences preferences;
    private String token;
    private Button post_supply_btn,upload_btn;
    private static int PICK_IMAGE_FROM_GALLERY_REQUEST=1;
    private Uri path;
    private EditText product_name_edt,product_price_edt;
    private ImageView post_product_image;
    private ProgressBar pr;
    private ViewFlipper flipper;
    private View first_view,success_view;
    private TextView succes_message,erro_shower;

    public PostInventoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_inventory, container, false);
        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        flipper=(ViewFlipper)view.findViewById(R.id.inventory_flipper);
        first_view=LayoutInflater.from(getActivity()).inflate(R.layout.post_inventory_first_view,null);
        success_view=LayoutInflater.from(getActivity()).inflate(R.layout.post_supply_success_view,null);

        succes_message=(TextView)success_view.findViewById(R.id.post_supply_success_message);

        erro_shower=(TextView)first_view.findViewById(R.id.inventory_error);
        post_product_category=(Spinner)first_view.findViewById(R.id.post_product_category);
        category_list=new ArrayList<>();
        category_list.add(getResources().getString(R.string.selet_post_product_category));
        registration_type_adapter=new CustomSpinnerAdapter(getActivity(), R.layout.spinner_dropdown, category_list, res);
        post_product_category.setAdapter(registration_type_adapter);
        category_id_holder=new ArrayList<>();

        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<CFSCProductCategory>> call=client.getProductCategory(getToken());
        call.enqueue(new Callback<List<CFSCProductCategory>>() {
            @Override
            public void onResponse(Call<List<CFSCProductCategory>> call, Response<List<CFSCProductCategory>> response) {
                for (int i=0;i<response.body().size();i++){
                    category_list.add(response.body().get(i).getName());
                    category_id_holder.add(response.body().get(i).getId());
                }

            }

            @Override
            public void onFailure(Call<List<CFSCProductCategory>> call, Throwable t) {

            }
        });


        post_product_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=1){
                    setCategory_id(category_id_holder.get(i-1));
                    Retrofit retrofit1=getUserAPIretrofit();
                    CFSCClient client1= retrofit1.create(CFSCClient.class);
                    Call<List<CFSCProductSubCategory>>call1=client1.getSubProductCategory(getToken(),category_id_holder.get(i-1));
                    call1.enqueue(new Callback<List<CFSCProductSubCategory>>() {
                        @Override
                        public void onResponse(Call<List<CFSCProductSubCategory>> call, Response<List<CFSCProductSubCategory>> response) {
                            if (response.body().size()>0){
                                //sub_category_id_holder.rem;
                                sub_category_list.clear();
                                sub_category_id_holder.clear();
                                sub_category_list.add(getResources().getString(R.string.selet_post_product_category));
                                for (int i=0;i<response.body().size();i++){
                                    sub_category_list.add(response.body().get(i).getName());
                                    sub_category_id_holder.add(response.body().get(i).getId());
                                }
                            }else {
                                sub_category_list.add(getResources().getString(R.string.selet_post_product_category));
                            }
                        }

                        @Override
                        public void onFailure(Call<List<CFSCProductSubCategory>> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        post_product_sub_category_spinner=(Spinner)first_view.findViewById(R.id.post_supply_sub_category);
        sub_category_list=new ArrayList<>();
        sub_category_list.add(getResources().getString(R.string.selet_post_product_category));
        sub_category_adapter=new CustomSpinnerAdapter(getActivity(), R.layout.spinner_dropdown, sub_category_list, res);
        post_product_sub_category_spinner.setAdapter(sub_category_adapter);
        sub_category_id_holder=new ArrayList<>();
        post_product_sub_category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i>=1){
                    setSub_category_id(sub_category_id_holder.get(i-1));

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        product_name_edt=(EditText)first_view.findViewById(R.id.supply_post_product_name);
        product_price_edt=(EditText)first_view.findViewById(R.id.supply_post_product_price);

        pr=(ProgressBar)first_view.findViewById(R.id.post_supply_pr);

        post_supply_btn=(Button)first_view.findViewById(R.id.post_supply_btn);
        post_supply_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String product_name_value=product_name_edt.getText().toString();
                String product_quantity_value=product_price_edt.getText().toString();
                if(getCategory_id()==0){
                    erro_shower.setText("Please select product category");
                    erro_shower.setVisibility(View.VISIBLE);
                }else if(getSub_category_id()==0){
                    erro_shower.setText("Please enter product type");
                    erro_shower.setVisibility(View.VISIBLE);

                }else if(product_name_value.equals("")){
                  erro_shower.setText("Please enter product name");
                    erro_shower.setVisibility(View.VISIBLE);

                }else if(product_quantity_value.equals("")){
                 erro_shower.setText("Please enter product quantity");
                    erro_shower.setVisibility(View.VISIBLE);

                }else {
                    postInventory(String.valueOf(getCategory_id()),String.valueOf(getSub_category_id()),product_name_value,product_quantity_value);

                }

            }
        });



        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
        // set the animation type's to ViewFlipperActivity
        flipper.setInAnimation(in);
        flipper.setOutAnimation(out);
        // set interval time for flipping between views
        flipper.setFlipInterval(3000);
        flipper.setDisplayedChild(6);

        flipper.removeAllViews();
        flipper.addView(first_view);
        flipper.showNext();
        return view;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }
public void postInventory(String category_id,String sub_category_id,String product_name,String quantity){
    pr.setVisibility(View.VISIBLE);
    CFSCInvetory invetory=new CFSCInvetory(sub_category_id,product_name,quantity);
    Retrofit retrofit=getUserAPIretrofit();
    CFSCClient client=retrofit.create(CFSCClient.class);
    Call<ResponseToken> call=client.sendInventory(getToken(),invetory);
    call.enqueue(new Callback<ResponseToken>() {
        @Override
        public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
            if (response.body().getStatus().equals("ok")){
                pr.setVisibility(View.GONE);
                flipper.addView(success_view);
                flipper.showNext();
                succes_message.setTextColor(Color.GREEN);
                succes_message.setText(getResources().getString(R.string.inventory_success)+" "+response.body().getToken()+" ETB");

            }
        }

        @Override
        public void onFailure(Call<ResponseToken> call, Throwable t) {

        }
    });


}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

}
