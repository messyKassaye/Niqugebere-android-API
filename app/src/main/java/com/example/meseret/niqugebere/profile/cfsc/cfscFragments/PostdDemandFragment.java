package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
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
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCProductCategory;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CFSCProductSubCategory;
import com.example.meseret.niqugebere.profile.uploaders.FileUtils;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostdDemandFragment extends Fragment {

    private String company_id;
    private ArrayList<String> category_list,sub_category_list,availability_list;
    private ArrayList<Integer> category_id_holder,sub_category_id_holder;
    private int category_id,sub_category_id;
    private CustomSpinnerAdapter registration_type_adapter,sub_category_adapter,availability_adapter;
    private Spinner post_product_category,post_product_sub_category_spinner,availability_spinner;
    private Resources res;
    private SharedPreferences preferences;
    private String token;
    private Button post_supply_btn,upload_btn;
    private static int PICK_IMAGE_FROM_GALLERY_REQUEST=1;
    private Uri path;
    private EditText product_name_edt,product_price_edt,description_edt;
    private ImageView post_product_image;
    private ProgressBar pr;
    private ViewFlipper flipper;
    private View first_view,success_view;
    private TextView succes_message;

    public PostdDemandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_postd_demand, container, false);
        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        flipper=(ViewFlipper)view.findViewById(R.id.demand_flipper);
        first_view=LayoutInflater.from(getActivity()).inflate(R.layout.demand_first_view,null);
        success_view=LayoutInflater.from(getActivity()).inflate(R.layout.post_supply_success_view,null);

        succes_message=(TextView)success_view.findViewById(R.id.post_supply_success_message);

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
        description_edt=(EditText)first_view.findViewById(R.id.post_supply_description);
        post_product_image=(ImageView)first_view.findViewById(R.id.post_product_image);
        availability_spinner=(Spinner)first_view.findViewById(R.id.post_supply_availability);
        availability_list=new ArrayList<>();
        availability_list.add(getResources().getString(R.string.post_availability));
        availability_list.add("in 1 week");
        availability_list.add("in 2 weeks");
        availability_list.add("in 3 weeks");
        availability_list.add("in 1 month");
        availability_list.add("in 2 months");
        availability_adapter=new CustomSpinnerAdapter(getActivity(), R.layout.spinner_dropdown, availability_list, res);
        availability_spinner.setAdapter(availability_adapter);

        upload_btn=(Button)first_view.findViewById(R.id.post_upload_image);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_FROM_GALLERY_REQUEST);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY_REQUEST);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pr=(ProgressBar)first_view.findViewById(R.id.post_supply_pr);

        post_supply_btn=(Button)first_view.findViewById(R.id.post_supply_btn);
        post_supply_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                String product_name_value=product_name_edt.getText().toString();
                String product_price_value=product_price_edt.getText().toString();
                String description_value=description_edt.getText().toString();
                String availability=availability_spinner.getSelectedItem().toString();
                uploadPost(path,String.valueOf(getCategory_id()),String.valueOf(getSub_category_id()),product_name_value,product_price_value,description_value,availability);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_FROM_GALLERY_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            path = data.getData();
            post_product_image.setVisibility(View.VISIBLE);
            Picasso.with(getActivity()).load(path).into(post_product_image);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void uploadPost(Uri uri, String category_id, String sub_category_id, String name, String price, String description, String availability){
        pr.setVisibility(View.VISIBLE);
        File file = FileUtils.getFile(getActivity(), uri);
        final RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        RequestBody category =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, category_id);

        RequestBody sub_category =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, sub_category_id);
        RequestBody product_name =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, name);
        RequestBody product_price =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM,price );
        RequestBody product_description =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, description);
        RequestBody product_availability =
                RequestBody.create(
                        okhttp3.MultipartBody.FORM, availability);
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<ResponseToken>call=client.postDemand(getToken(),body,sub_category,product_name,product_price,product_availability,product_description);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if (response.body().getStatus().equals("ok")){
                    pr.setVisibility(View.GONE);
                    flipper.removeAllViews();
                    flipper.addView(success_view);
                    flipper.showNext();
                    succes_message.setText(getResources().getString(R.string.post_success_message));
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {

            }
        });

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
