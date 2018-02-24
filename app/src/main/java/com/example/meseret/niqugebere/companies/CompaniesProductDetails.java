package com.example.meseret.niqugebere.companies;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.PostedProductsRecyclerviewAdapter;
import com.example.meseret.niqugebere.companies.companyAdapterModel.PostedProductsAdapterModel;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.CompanyName;
import com.example.meseret.niqugebere.companies.companyModels.PostedProducts;
import com.example.meseret.niqugebere.companies.fragments.CompanyProductFragment;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CompaniesProductDetails extends AppCompatActivity {
    private Toolbar toolbar;
    private String company_id;
    private TextView product_info;
    private RecyclerView recyclerView;
    private List<PostedProductsAdapterModel> list;
    private PostedProductsRecyclerviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_companies_product_details);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        company_id=getIntent().getStringExtra("company_id");
        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<CompanyName>> call=client.getCompanyName(company_id);
        call.enqueue(new Callback<List<CompanyName>>() {
            @Override
            public void onResponse(Call<List<CompanyName>> call, Response<List<CompanyName>> response) {
                toolbar.setTitle(response.body().get(0).getName());
            }

            @Override
            public void onFailure(Call<List<CompanyName>> call, Throwable t) {

            }
        });
        product_info=(TextView)findViewById(R.id.selected_product_info);
        product_info.setText("List of posted "+getIntent().getStringExtra("product_name")+" for sell");

        list=new ArrayList<>();
        adapter=new PostedProductsRecyclerviewAdapter(this,list);
        recyclerView=(RecyclerView)findViewById(R.id.selected_product_recycleview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Call<List<PostedProducts>> postCall=client.getCompanyPostedProducts(company_id,getIntent().getStringExtra("product_id"));
        postCall.enqueue(new Callback<List<PostedProducts>>() {
           @Override
           public void onResponse(Call<List<PostedProducts>> call, Response<List<PostedProducts>> response) {
               for (int i=0;i<response.body().size();i++){
                 PostedProductsAdapterModel model=new PostedProductsAdapterModel();

                   model.setId(response.body().get(i).getId());
                 model.setProduct_name(response.body().get(i).getProduct_name());
                 model.setProduct_photo(response.body().get(i).getProduct_photo());
                 model.setQuantity(response.body().get(i).getQuantity());
                 model.setSub_category_name(response.body().get(i).getSub_category_name());
                 model.setUnit(response.body().get(i).getUnit());
                 model.setUnit_price(response.body().get(i).getUnit_price());
                 list.add(model);
             }
               adapter.notifyDataSetChanged();
               recyclerView.setAdapter(adapter);
           }

           @Override
           public void onFailure(Call<List<PostedProducts>> call, Throwable t) {

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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


}
