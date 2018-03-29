package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adapter.PullProductRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PullProductSubCategoryRecyclerviewAdapter;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.fragments.PullFragment;
import com.example.meseret.niqugebere.model.MarketLists;
import com.example.meseret.niqugebere.model.Markets;
import com.example.meseret.niqugebere.model.ProductCategory;
import com.example.meseret.niqugebere.model.ProductSubCategory;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.model.Roles;
import com.example.meseret.niqugebere.profile.cfsc.UnfinishedActivity;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.SupplyProductrecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.SupplySubProductRecylerviewAdapter;
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
public class PostSupplyFragment extends Fragment {
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

    private String productName;
    private RecyclerView product_cateogry_recyclerview;
    private List<ProductCategory> product_category_list;
    private SupplyProductrecyclerviewAdapter product_recyclerview_adapter;
    private TextView product_type_textview;


    private String productSubName;
    private TextView product_sub_category_textview;
    private List<ProductSubCategory> productSubCategoryList;
    private SupplySubProductRecylerviewAdapter product_subcategory_adapter;
    private RecyclerView product_subcategory_recyclerview;

    private LinearLayout supplyHiddenLayout;

    public PostSupplyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post_supply, container, false);
        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        flipper=(ViewFlipper)view.findViewById(R.id.post_flipper);
        pr=(ProgressBar)view.findViewById(R.id.post_suplly_pr);
        first_view=LayoutInflater.from(getActivity()).inflate(R.layout.post_supply_first_view,null);
        success_view=LayoutInflater.from(getActivity()).inflate(R.layout.post_supply_success_view,null);

        succes_message=(TextView)success_view.findViewById(R.id.post_supply_success_message);



        product_category_list = new ArrayList<>();
        product_recyclerview_adapter = new SupplyProductrecyclerviewAdapter(getActivity(), product_category_list,this);
        product_type_textview=(TextView)first_view.findViewById(R.id.produc_type_textview);
        product_cateogry_recyclerview = (RecyclerView) first_view.findViewById(R.id.post_product_category);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        product_cateogry_recyclerview.setLayoutManager(mLayoutManager);
        product_cateogry_recyclerview.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        product_cateogry_recyclerview.setItemAnimator(new DefaultItemAnimator());

        pr.setVisibility(View.VISIBLE);
        Retrofit retrofit = getUserAPIretrofit();
        MainClient client = retrofit.create(MainClient.class);
        Call<List<Markets>> call = client.getMarkets();
        call.enqueue(new Callback<List<Markets>>() {
            @Override
            public void onResponse(Call<List<Markets>> call, Response<List<Markets>> response) {
                if (response.isSuccessful()) {
                    pr.setVisibility(View.GONE);
                    for (int i = 0; i < response.body().size(); i++) {
                        ProductCategory model = new ProductCategory();
                        model.setId(response.body().get(i).getId());
                        model.setName(response.body().get(i).getName());
                        product_category_list.add(model);
                    }
                    product_recyclerview_adapter.notifyDataSetChanged();
                    product_cateogry_recyclerview.setAdapter(product_recyclerview_adapter);

                }

            }

            @Override
            public void onFailure(Call<List<Markets>> call, Throwable t) {

            }
        });




        supplyHiddenLayout=(LinearLayout)first_view.findViewById(R.id.postSupplyHiddenLayout);
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

    public void displayProductSubCategory(String id,String product_name){
        pr.setVisibility(View.VISIBLE);
        setProductName(product_name);
        productSubCategoryList=new ArrayList<>();
        product_subcategory_adapter=new SupplySubProductRecylerviewAdapter(getActivity(),productSubCategoryList,this);
        product_subcategory_recyclerview=(RecyclerView)first_view.findViewById(R.id.product_sub_category_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        product_subcategory_recyclerview.setLayoutManager(mLayoutManager);
        product_subcategory_recyclerview.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        product_subcategory_recyclerview.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit = getUserAPIretrofit();
        MainClient client = retrofit.create(MainClient.class);
        Call<List<MarketLists>> call=client.getMarketCategory(id);
        call.enqueue(new Callback<List<MarketLists>>() {
            @Override
            public void onResponse(Call<List<MarketLists>> call, Response<List<MarketLists>> response) {
                if(response.isSuccessful()){
                    pr.setVisibility(View.GONE);
                    product_type_textview.setTextColor(getResources().getColor(R.color.bg));
                    product_type_textview.setText("You have Selected a product type of "+getProductName()+"");
                    product_cateogry_recyclerview.setVisibility(View.GONE);

                    product_sub_category_textview=(TextView)first_view.findViewById(R.id.product_sub_category_textview);
                    product_sub_category_textview.setVisibility(View.VISIBLE);
                    product_sub_category_textview.setText("Select what type of "+getProductName()+" you need to buy");

                    for (int i=0;i<response.body().size();i++){
                        ProductSubCategory model=new ProductSubCategory();
                        model.setId(response.body().get(i).getId());
                        model.setName(response.body().get(i).getName());
                        productSubCategoryList.add(model);
                    }
                    product_subcategory_adapter.notifyDataSetChanged();
                    product_subcategory_recyclerview.setAdapter(product_subcategory_adapter);
                }
            }

            @Override
            public void onFailure(Call<List<MarketLists>> call, Throwable throwable) {

            }
        });


    }
    public void displayWoredas(String id,String product_sub_name) {
        pr.setVisibility(View.GONE);
        setProductSubName(product_sub_name);
        setSub_category_id(Integer.parseInt(id));
        product_sub_category_textview.setTextColor(getResources().getColor(R.color.bg));
        product_sub_category_textview.setText("You have selected "+getProductSubName()+" a type of "+getProductName());
        product_subcategory_recyclerview.setVisibility(View.GONE);
        supplyHiddenLayout.setVisibility(View.VISIBLE);

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
        Call<ResponseToken>call=client.posSupply(getToken(),body,sub_category,product_name,product_price,product_availability,product_description);
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

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public String getProductSubName() {
        return productSubName;
    }

    public void setProductSubName(String productSubName) {
        this.productSubName = productSubName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
