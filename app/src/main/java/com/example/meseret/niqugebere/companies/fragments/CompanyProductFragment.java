package com.example.meseret.niqugebere.companies.fragments;


import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.ProductRecyclerviewAdapter;
import com.example.meseret.niqugebere.companies.companyAdapterModel.ProductAdapterModel;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.Prodcuts;
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
public class CompanyProductFragment extends Fragment {
    private SharedPreferences preferences;

    private View view;


    private RecyclerView recyclerView;
    private List<ProductAdapterModel>list;
    private ProductRecyclerviewAdapter adapter;
    public CompanyProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_company_product, container, false);

        preferences=getActivity().getSharedPreferences("company_data",0);
        list=new ArrayList<>();
        adapter=new ProductRecyclerviewAdapter(getActivity(),list);
        recyclerView=(RecyclerView)view.findViewById(R.id.product_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<Prodcuts>> call=client.getProducts(preferences.getString("id",null));
        call.enqueue(new Callback<List<Prodcuts>>() {
            @Override
            public void onResponse(Call<List<Prodcuts>> call, Response<List<Prodcuts>> response) {
                if (response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        ProductAdapterModel model=new ProductAdapterModel();
                        model.setId(response.body().get(i).getId());
                        model.setCategory_name(response.body().get(i).getCategory_name());
                        model.setName(response.body().get(i).getName());
                        model.setPhoto(response.body().get(i).getPhoto());
                        model.setPrice(response.body().get(i).getPrice());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Prodcuts>> call, Throwable t) {

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
