package com.example.meseret.niqugebere.companies.fragments;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.companies.companyAdapter.ServiceCategoryRecyclerviewAdapter;
import com.example.meseret.niqugebere.companies.companyAdapter.ServiceDetailrecyclerviewAdapter;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.CompanyServices;
import com.example.meseret.niqugebere.companies.companyModels.ServiceDetail;
import com.example.meseret.niqugebere.model.Services;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.NotificationFragment;
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
public class CompanyServiceFragment extends Fragment {

    private View view;
    private List<CompanyServices> list;
    private ServiceCategoryRecyclerviewAdapter adapter;
    private SharedPreferences preferences;
    private String company_id;

    private List<ServiceDetail> detailList;
    private ServiceDetailrecyclerviewAdapter detialadapter;
    private RecyclerView recyclerView,recyclerViewDetail;

    private RelativeLayout mainLayout;
    private ProgressBar pr;
    public CompanyServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view=inflater.inflate(R.layout.fragment_company_services, container, false);
         preferences=getActivity().getSharedPreferences("company_data",0);
        setCompany_id(preferences.getString("id",""));
        mainLayout=(RelativeLayout) view.findViewById(R.id.service_layout);
        pr=(ProgressBar)view.findViewById(R.id.service_pr);

        list=new ArrayList<>();
        adapter=new ServiceCategoryRecyclerviewAdapter(getActivity(),this,list);
        recyclerView=(RecyclerView)view.findViewById(R.id.services_recyclerview);
        getServices();

        return view;
    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public void getServices(){
        pr.setVisibility(View.VISIBLE);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<CompanyServices>> call=client.getServices(preferences.getString("id",""));
        call.enqueue(new Callback<List<CompanyServices>>() {
            @Override
            public void onResponse(Call<List<CompanyServices>> call, Response<List<CompanyServices>> response) {
                if(response.isSuccessful()){
                    pr.setVisibility(View.GONE);
                    for (int i=0;i<response.body().size();i++){
                        CompanyServices model=new CompanyServices();
                        model.setName(response.body().get(i).getName());
                        model.setId(response.body().get(i).getId());
                        model.setPhoto_path(response.body().get(i).getPhoto_path());
                        model.setCompany_id(response.body().get(i).getCompany_id());
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<CompanyServices>> call, Throwable throwable) {

            }
        });
    }

    public void getMyService(String id){
        pr.setVisibility(View.VISIBLE);
          recyclerView.setVisibility(View.GONE);
        mainLayout.setBackgroundColor(getResources().getColor(R.color.navtextcolor));
        detailList=new ArrayList<>();
        detialadapter=new ServiceDetailrecyclerviewAdapter(getActivity(),this,detailList);
        recyclerViewDetail=(RecyclerView)view.findViewById(R.id.service_detail_recyclerview);
        recyclerViewDetail.setVisibility(View.VISIBLE);
        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        recyclerViewDetail.setItemAnimator(new DefaultItemAnimator());

        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<ServiceDetail>> call=client.showServices(id,getCompany_id());
        call.enqueue(new Callback<List<ServiceDetail>>() {
            @Override
            public void onResponse(Call<List<ServiceDetail>> call, Response<List<ServiceDetail>> response) {
                if(response.isSuccessful()){
                    pr.setVisibility(View.GONE);
                  for (int i=0;i<response.body().size();i++){
                      ServiceDetail model=new ServiceDetail();
                      model.setTitle(response.body().get(i).getTitle());
                      model.setDescription(response.body().get(i).getDescription());
                      detailList.add(model);

                  }
                  detialadapter.notifyDataSetChanged();
                    recyclerViewDetail.setAdapter(detialadapter);
                }
            }

            @Override
            public void onFailure(Call<List<ServiceDetail>> call, Throwable throwable) {

            }
        });

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

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }
}
