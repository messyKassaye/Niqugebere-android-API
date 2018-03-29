package com.example.meseret.niqugebere.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.MainActivity;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adapter.CurrentLocationRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PullCurrentLocationAdapter;
import com.example.meseret.niqugebere.adapter.PullProductRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PullProductSubCategoryRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PushProductCategoryRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PushProductSubCategoryrecyclerviewAdapter;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.clients.PushClient;
import com.example.meseret.niqugebere.clients.UsersClient;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.Woreda;
import com.example.meseret.niqugebere.model.MarketLists;
import com.example.meseret.niqugebere.model.Markets;
import com.example.meseret.niqugebere.model.ProductCategory;
import com.example.meseret.niqugebere.model.ProductSubCategory;
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
    private ViewFlipper flipper;
    private View firstView,secondView,thirdView;
    private ProgressBar pr;
    private int woreda_id;
    private int sub_category_id;
    private String tinNo;


    private TextView pushIdentifierErrorShower;
    private EditText tinNoEdit;
    private Button checkButton;

    private String productName;
    private RecyclerView product_cateogry_recyclerview;
    private List<ProductCategory> product_category_list;
    private PushProductCategoryRecyclerviewAdapter product_recyclerview_adapter;
    private TextView product_type_textview;



    private String productSubName;
    private TextView product_sub_category_textview;
    private List<ProductSubCategory> productSubCategoryList;
    private PushProductSubCategoryrecyclerviewAdapter product_subcategory_adapter;
    private RecyclerView product_subcategory_recyclerview;


    private TextView current_location_textview;
    private List<Woreda> currentLocationList;
    private PullCurrentLocationAdapter currentLocationAdapter;
    private RecyclerView currentLocationRecyclerview;
    private EditText search_woreda_edt;
    private String woredaName;
    private LinearLayout leftLayout;
    private EditText product_name,quantity,description;
    private Button push;

    private TextView pushErrorShower;
    private TextView pushSuccess;
    public PushFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_push, container, false);

        flipper=(ViewFlipper)view.findViewById(R.id.push_flipper);
        pr=(ProgressBar)view.findViewById(R.id.push_pr);

        firstView=LayoutInflater.from(getActivity()).inflate(R.layout.push_firstview_layout,null);
        secondView=LayoutInflater.from(getActivity()).inflate(R.layout.push_second_layout,null);
        thirdView=LayoutInflater.from(getActivity()).inflate(R.layout.push_third_layout,null);

        tinNoEdit=(EditText)firstView.findViewById(R.id.push_tinno_edt);
        pushIdentifierErrorShower=(TextView)firstView.findViewById(R.id.push_error_shower);
        checkButton=(Button)firstView.findViewById(R.id.pushCheckUsers);
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String tinNoValue=tinNoEdit.getText().toString();
                if(tinNoValue.equals("")){
                 pushIdentifierErrorShower.setText("Please enter your tin no");
                }else {
                    pr.setVisibility(View.VISIBLE);
                   Retrofit retrofit=getUserAPIretrofit();
                    PushClient client=retrofit.create(PushClient.class);
                    Call<ResponseToken> call=client.identifyCFSC(tinNoValue);
                    call.enqueue(new Callback<ResponseToken>() {
                        @Override
                        public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                            if(response.isSuccessful()){
                                if(response.body().getStatus().equals("no")){
                                    pushIdentifierErrorShower.setText("No one is registered in this tin no");
                                    pr.setVisibility(View.GONE);
                                }else {
                                    setTinNo(tinNoValue);
                                    pr.setVisibility(View.GONE);
                                    flipper.removeAllViews();
                                    flipper.addView(secondView);
                                    flipper.showNext();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseToken> call, Throwable throwable) {

                        }
                    });
                }
            }
        });

        flipper.addView(firstView);
        flipper.showNext();

        product_category_list = new ArrayList<>();
        product_recyclerview_adapter = new PushProductCategoryRecyclerviewAdapter(getActivity(), product_category_list,this);
        product_type_textview=(TextView)secondView.findViewById(R.id.produc_type_textview);
        product_cateogry_recyclerview = (RecyclerView) secondView.findViewById(R.id.post_product_category);
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

        leftLayout=(LinearLayout)secondView.findViewById(R.id.push_left_layout);
        pushSuccess=(TextView)thirdView.findViewById(R.id.push_success);
        pushErrorShower=(TextView)secondView.findViewById(R.id.push_error);
        product_name=(EditText)secondView.findViewById(R.id.push_title);
        quantity=(EditText)secondView.findViewById(R.id.push_quantity);
        description=(EditText)secondView.findViewById(R.id.push_description);
        push=(Button)secondView.findViewById(R.id.push_btn);
        push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              String productNameValue=product_name.getText().toString();
                String totalQuantity=quantity.getText().toString();
                String descriptionValue=description.getText().toString();
                //PushModel pushModel=new PushModel(getTinNo(),getSub_category_id(),getWoreda_id(),productNameValue,totalQuantity,descriptionValue);

                if(productNameValue.equals("")){
                    pushErrorShower.setVisibility(View.VISIBLE);
                  pushErrorShower.setText("Please enter product name");
                }else if(totalQuantity.equals("")){
                    pushErrorShower.setVisibility(View.VISIBLE);
                    pushErrorShower.setText("Please enter how many "+productNameValue+" you are going to post");
                }else{
                    flipper.removeAllViews();
                    flipper.addView(thirdView);
                    flipper.showNext();
                    pushSuccess.setText("Congratulations. you have post your product successfully");
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

        return  view;
    }
    public void displayProductSubCategory(String id,String product_name){
        pr.setVisibility(View.VISIBLE);
        setProductName(product_name);
        productSubCategoryList=new ArrayList<>();
        product_subcategory_adapter=new PushProductSubCategoryrecyclerviewAdapter(getActivity(),productSubCategoryList,this);
        product_subcategory_recyclerview=(RecyclerView)secondView.findViewById(R.id.product_sub_category_recyclerview);
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
                    product_type_textview.setTextColor(Color.GREEN);
                    product_type_textview.setText("You have Selected a product type of "+getProductName()+"");
                    product_cateogry_recyclerview.setVisibility(View.GONE);

                    product_sub_category_textview=(TextView)secondView.findViewById(R.id.product_sub_category_textview);
                    product_sub_category_textview.setVisibility(View.VISIBLE);
                    product_sub_category_textview.setText("Select what type of "+getProductName()+" you need to send");

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

    public void displayWoredas(String id,String product_sub_name){
        pr.setVisibility(View.VISIBLE);
        setProductSubName(product_sub_name);
        setSub_category_id(Integer.parseInt(id));
        currentLocationList=new ArrayList<>();
        currentLocationAdapter=new PullCurrentLocationAdapter(getActivity(),currentLocationList,this);
        currentLocationRecyclerview=(RecyclerView)secondView.findViewById(R.id.current_location_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        currentLocationRecyclerview.setLayoutManager(mLayoutManager);
        currentLocationRecyclerview.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        currentLocationRecyclerview.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<Woreda>> call=client.getWoreda();
        call.enqueue(new Callback<List<Woreda>>() {
            @Override
            public void onResponse(Call<List<Woreda>> call, Response<List<Woreda>> response) {
                if(response.isSuccessful()){
                    pr.setVisibility(View.GONE);
                    product_sub_category_textview.setTextColor(Color.GREEN);
                    product_sub_category_textview.setText("You have selected "+getProductSubName()+" a type of "+getProductName());
                    product_subcategory_recyclerview.setVisibility(View.GONE);
                    current_location_textview=(TextView)secondView.findViewById(R.id.current_location_textview);
                    current_location_textview.setVisibility(View.VISIBLE);
                    for (int i=0;i<response.body().size();i++){
                        Woreda model=new Woreda();
                        model.setId(response.body().get(i).getId());
                        model.setName(response.body().get(i).getName());
                        currentLocationList.add(model);
                    }
                    currentLocationAdapter.notifyDataSetChanged();
                    currentLocationRecyclerview.setAdapter(currentLocationAdapter);
                }
            }

            @Override
            public void onFailure(Call<List<Woreda>> call, Throwable throwable) {

            }
        });

    }
    public void displayPullButton(String locationId,String locationName){
        setWoredaName(locationName);
        current_location_textview.setTextColor(Color.GREEN);
        current_location_textview.setText("Your Current Location is "+locationName);
        setWoreda_id(Integer.parseInt(locationId));
        currentLocationRecyclerview.setVisibility(View.GONE);
        leftLayout.setVisibility(View.VISIBLE);

    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
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

    public String getWoredaName() {
        return woredaName;
    }

    public void setWoredaName(String woredaName) {
        this.woredaName = woredaName;
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

    public int getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(int sub_category_id) {
        this.sub_category_id = sub_category_id;
    }

    public int getWoreda_id() {
        return woreda_id;
    }

    public void setWoreda_id(int woreda_id) {
        this.woreda_id = woreda_id;
    }

    public String getTinNo() {
        return tinNo;
    }

    public void setTinNo(String tinNo) {
        this.tinNo = tinNo;
    }
}
