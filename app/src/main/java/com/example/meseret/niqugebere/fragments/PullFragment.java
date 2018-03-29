package com.example.meseret.niqugebere.fragments;


import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adapter.CurrentLocationRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PullProductRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PullProductSubCategoryRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.PullRecyclerviewAdapter;
import com.example.meseret.niqugebere.adaptermodel.PullAdapterModel;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.Woreda;
import com.example.meseret.niqugebere.model.MarketLists;
import com.example.meseret.niqugebere.model.Markets;
import com.example.meseret.niqugebere.model.ProductCategory;
import com.example.meseret.niqugebere.model.ProductSubCategory;
import com.example.meseret.niqugebere.model.Pull;
import com.example.meseret.niqugebere.profile.cfsc.UnfinishedActivity;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.CFSCProductsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
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
public class PullFragment extends Fragment {
    private ArrayList<String> category_list, sub_category_list, availability_list;
    private ArrayList<Integer> category_id_holder, sub_category_id_holder;
    private CustomSpinnerAdapter registration_type_adapter, sub_category_adapter, availability_adapter;
    private Resources res;

    private View first_view, second_view;

    private ArrayList<String> woreda_list;
    private CustomSpinnerAdapter adapter;
    private ArrayList<Integer> id_holder;
    private int woreda_id;
    private int category_id, sub_category_id;
    private Button pull;
    private ViewFlipper flipper;
    private ProgressBar pr;

    private RecyclerView pull_recyclerView;
    private List<PullAdapterModel> pull_list;
    private PullRecyclerviewAdapter pull_adapter;
    private TextView pull_info;

    private String productName;
    private RecyclerView product_cateogry_recyclerview;
    private List<ProductCategory> product_category_list;
    private PullProductRecyclerviewAdapter product_recyclerview_adapter;
    private TextView product_type_textview;


    private String productSubName;
    private TextView product_sub_category_textview;
    private List<ProductSubCategory> productSubCategoryList;
    private PullProductSubCategoryRecyclerviewAdapter product_subcategory_adapter;
    private RecyclerView product_subcategory_recyclerview;


    private TextView current_location_textview;
    private List<Woreda> currentLocationList;
    private CurrentLocationRecyclerviewAdapter currentLocationAdapter;
    private RecyclerView currentLocationRecyclerview;
    private EditText search_woreda_edt;
    private String woredaName;


    public PullFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pull, container, false);

        flipper = (ViewFlipper) view.findViewById(R.id.flipper);
        pr = (ProgressBar) view.findViewById(R.id.pull_pr);

        first_view = LayoutInflater.from(getActivity()).inflate(R.layout.pull_first_layout, null);
        second_view = LayoutInflater.from(getActivity()).inflate(R.layout.pull_second_view, null);

        product_category_list = new ArrayList<>();
        product_recyclerview_adapter = new PullProductRecyclerviewAdapter(getActivity(), product_category_list,this);
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


        pull_info = (TextView) second_view.findViewById(R.id.pull_info);

        pull_list = new ArrayList<>();
        pull_adapter = new PullRecyclerviewAdapter(getActivity(), pull_list);
        pull_recyclerView = (RecyclerView) second_view.findViewById(R.id.pull_recyclerview);
        pull_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        pull_recyclerView.setItemAnimator(new DefaultItemAnimator());

        search_woreda_edt=(EditText)first_view.findViewById(R.id.search_woreda);
        search_woreda_edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                currentLocationAdapter.getfFilter().filter(charSequence.toString());
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pull = (Button) first_view.findViewById(R.id.pull_btn);
        pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int product_sub_category_id = getSub_category_id();
                int woreda_id_value = getWoreda_id();

                if (product_sub_category_id > 0 && woreda_id_value > 0) {
                    pr.setVisibility(View.VISIBLE);
                    Retrofit retrofit1 = getUserAPIretrofit();
                    MainClient client1 = retrofit1.create(MainClient.class);
                    Call<List<PullAdapterModel>> call1 = client1.getPull(woreda_id_value, product_sub_category_id);
                    call1.enqueue(new Callback<List<PullAdapterModel>>() {
                        @Override
                        public void onResponse(Call<List<PullAdapterModel>> call, Response<List<PullAdapterModel>> response) {
                            if (response.isSuccessful()) {
                                pr.setVisibility(View.GONE);
                                if (response.body().size() <= 0) {
                                    pull_info.setText("We can't find " + getProductSubName()+" around "+getWoredaName()+" area");
                                    flipper.removeAllViews();
                                    flipper.addView(second_view);
                                    flipper.showNext();
                                } else {
                                    pull_info.setText(getActivity().getResources().getString(R.string.pull_info) + " " + response.body().get(0).getSub_category_name() + " " + getActivity().getResources().getString(R.string.pull_info_sub));
                                    for (int i = 0; i < response.body().size(); i++) {
                                        PullAdapterModel model = new PullAdapterModel();
                                        model.setCompany_name(response.body().get(i).getCompany_name());
                                        model.setProduct_name(response.body().get(i).getProduct_name());
                                        model.setSpecial_name(response.body().get(i).getSpecial_name());
                                        model.setTotal_quantity(response.body().get(i).getTotal_quantity());
                                        model.setWoreda(response.body().get(i).getWoreda());
                                        model.setCategory_name(response.body().get(i).getCategory_name());
                                        model.setSub_category_name(response.body().get(i).getSub_category_name());
                                        model.setProduct_photo(response.body().get(i).getProduct_photo());
                                        model.setCompany_photo(response.body().get(i).getCompany_photo());
                                        model.setId(response.body().get(i).getId());
                                        model.setPrice(response.body().get(i).getPrice());
                                        pull_list.add(model);
                                    }
                                    flipper.removeAllViews();
                                    flipper.addView(second_view);
                                    flipper.showNext();
                                    pull_adapter.notifyDataSetChanged();
                                    pull_recyclerView.setAdapter(pull_adapter);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<List<PullAdapterModel>> call, Throwable t) {

                        }
                    });


                } else {
                    Toast.makeText(getActivity(), "Please select product type you need to find", Toast.LENGTH_LONG).show();
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

    public void displayProductSubCategory(String id,String product_name){
        pr.setVisibility(View.VISIBLE);
        setProductName(product_name);
        productSubCategoryList=new ArrayList<>();
        product_subcategory_adapter=new PullProductSubCategoryRecyclerviewAdapter(getActivity(),productSubCategoryList,this);
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
                    product_type_textview.setTextColor(Color.GREEN);
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

    public void displayWoredas(String id,String product_sub_name){
        pr.setVisibility(View.VISIBLE);
         setProductSubName(product_sub_name);
        setSub_category_id(Integer.parseInt(id));
        currentLocationList=new ArrayList<>();
        currentLocationAdapter=new CurrentLocationRecyclerviewAdapter(getActivity(),currentLocationList,this);
        currentLocationRecyclerview=(RecyclerView)first_view.findViewById(R.id.current_location_recyclerview);
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
                    current_location_textview=(TextView)first_view.findViewById(R.id.current_location_textview);
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

    public void displayProgressBar(boolean result){
        if(result){
            pr.setVisibility(View.VISIBLE);
        }else {
            pr.setVisibility(View.GONE);
        }
    }

    public void displayPullButton(String locationId,String locationName){
        setWoredaName(locationName);
        current_location_textview.setTextColor(Color.GREEN);
        current_location_textview.setText("Your Current Location is "+locationName);
        setWoreda_id(Integer.parseInt(locationId));
        pull.setVisibility(View.VISIBLE);
        currentLocationRecyclerview.setVisibility(View.GONE);

    }

    public Retrofit getUserAPIretrofit() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public String getWoredaName() {
        return woredaName;
    }

    public void setWoredaName(String woredaName) {
        this.woredaName = woredaName;
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

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductSubName() {
        return productSubName;
    }

    public void setProductSubName(String productSubName) {
        this.productSubName = productSubName;
    }
}
