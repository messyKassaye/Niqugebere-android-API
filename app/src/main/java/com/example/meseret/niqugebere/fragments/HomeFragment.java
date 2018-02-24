package com.example.meseret.niqugebere.fragments;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adapter.CommercialFarmCenterAdapter;
import com.example.meseret.niqugebere.adapter.MarketRecyclerviewAdapter;
import com.example.meseret.niqugebere.adapter.ServicesRecyclerViewAdapter;
import com.example.meseret.niqugebere.adapter.SponsorAdapter;
import com.example.meseret.niqugebere.adaptermodel.CommercialFarmCenterAdapterModel;
import com.example.meseret.niqugebere.adaptermodel.MarketsModelAdapter;
import com.example.meseret.niqugebere.adaptermodel.ServicesAdapterModel;
import com.example.meseret.niqugebere.adaptermodel.SponsorAdapterModel;
import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.model.CommercialFarmCenter;
import com.example.meseret.niqugebere.model.Markets;
import com.example.meseret.niqugebere.model.Services;
import com.example.meseret.niqugebere.model.Sponsors;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
   private View view;
    private ViewPager viewPager;
    private static int currentPage = 0;
    private ArrayList<SponsorAdapterModel> sponsorslist;

    private RecyclerView topmalls_recyclerview;
    private List<CommercialFarmCenterAdapterModel> topmalls_list;
    private CommercialFarmCenterAdapter topMallAdapter;

    private RecyclerView services_recyclerview;
    private List<ServicesAdapterModel> services_list;
    private ServicesRecyclerViewAdapter services_adapter;

    private RecyclerView market_recyclerview;
    private List<MarketsModelAdapter> market_list;
    private MarketRecyclerviewAdapter market_adapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_home, container, false);


        partners();
        getCfsc();
        getMarkets();
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

    public void partners(){
        //viewPager=(ViewPager)view.findViewById(R.id.sponsors_viewpager);
        sponsorslist=new ArrayList<>();

        Retrofit retrofit=getUserAPIretrofit();
        MainClient client=retrofit.create(MainClient.class);
        Call<List<Sponsors>> call=client.getSponsors();
        call.enqueue(new Callback<List<Sponsors>>() {
            @Override
            public void onResponse(Call<List<Sponsors>> call, Response<List<Sponsors>> response) {
                if(response.isSuccessful()){
                    if(response.body().size()>0){
                        for (int i=0;i<response.body().size();i++){
                            SponsorAdapterModel model=new SponsorAdapterModel();
                            model.setName(response.body().get(i).getName());
                            model.setPhoto(response.body().get(i).getPhoto());
                            sponsorslist.add(model);
                        }

                        for(int i=0;i<sponsorslist.size();i++)
                            viewPager = (ViewPager)view.findViewById(R.id.sponsors_viewpager);
                        viewPager.setAdapter(new SponsorAdapter(getActivity(),sponsorslist));

                        // Auto start of viewpager
                        final Handler handler = new Handler();
                        final Runnable Update = new Runnable() {
                            public void run() {
                                if (currentPage == sponsorslist.size()) {
                                    currentPage = 0;
                                }
                                viewPager.setCurrentItem(currentPage++, true);
                            }
                        };
                        Timer swipeTimer = new Timer();
                        swipeTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, 2500, 2500);
                    }else{
                        viewPager = (ViewPager)view.findViewById(R.id.sponsors_viewpager);
                        view.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onFailure(Call<List<Sponsors>> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }


    public void getCfsc(){
        topmalls_list=new ArrayList<>();
        topMallAdapter=new CommercialFarmCenterAdapter(getActivity(),topmalls_list);
        topmalls_recyclerview=(RecyclerView)view.findViewById(R.id.top_malls_recyclverview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        topmalls_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        topmalls_recyclerview.setItemAnimator(new DefaultItemAnimator());


        Retrofit retrofit=getUserAPIretrofit();
        MainClient cfsc_client=retrofit.create(MainClient.class);
        Call<List<CommercialFarmCenter>> cfsc_call=cfsc_client.getCfsc();
        cfsc_call.enqueue(new Callback<List<CommercialFarmCenter>>() {
            @Override
            public void onResponse(Call<List<CommercialFarmCenter>> call, Response<List<CommercialFarmCenter>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        CommercialFarmCenterAdapterModel model=new CommercialFarmCenterAdapterModel();
                        model.setName(response.body().get(i).getName());
                        model.setPhoto(response.body().get(i).getPhoto_path());
                        model.setId(response.body().get(i).getId());
                        topmalls_list.add(model);
                    }
                    topmalls_recyclerview.setAdapter(topMallAdapter);
                    topMallAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<List<CommercialFarmCenter>> call, Throwable t) {

            }
        });
    }

    public void getServices(){
        services_list=new ArrayList<>();
        services_adapter=new ServicesRecyclerViewAdapter(getActivity(),services_list);
        services_recyclerview=(RecyclerView)view.findViewById(R.id.service_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        services_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        services_recyclerview.setItemAnimator(new DefaultItemAnimator());


        Retrofit retrofit=getUserAPIretrofit();
        MainClient cfsc_client=retrofit.create(MainClient.class);
        Call<List<Services>> cfsc_call=cfsc_client.geServices();
        cfsc_call.enqueue(new Callback<List<Services>>() {
            @Override
            public void onResponse(Call<List<Services>> call, Response<List<Services>> response) {
                if(response.isSuccessful()){
                    for (int i=0;i<response.body().size();i++){
                        ServicesAdapterModel model=new ServicesAdapterModel();
                        model.setName(response.body().get(i).getName());
                        model.setPhoto_path(response.body().get(i).getPhoto_path());
                        model.setId(response.body().get(i).getId());
                        services_list.add(model);
                    }
                    services_adapter.notifyDataSetChanged();
                    services_recyclerview.setAdapter(services_adapter);
                }

            }

            @Override
            public void onFailure(Call<List<Services>> call, Throwable t) {

            }
        });
    }

    public void  getMarkets(){
        market_list=new ArrayList<>();
        market_adapter=new MarketRecyclerviewAdapter(getActivity(),market_list);
        market_recyclerview=(RecyclerView)view.findViewById(R.id.market_recyclerview);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        market_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        market_recyclerview.setItemAnimator(new DefaultItemAnimator());


        Retrofit retrofit=getUserAPIretrofit();
        MainClient cfsc_client=retrofit.create(MainClient.class);
        Call<List<Markets>> cfsc_call=cfsc_client.getMarkets();
        cfsc_call.enqueue(new Callback<List<Markets>>() {
            @Override
            public void onResponse(Call<List<Markets>> call, Response<List<Markets>> response) {
               if(response.isSuccessful()){
                   for (int i=0;i<response.body().size();i++){
                       MarketsModelAdapter model=new MarketsModelAdapter();
                       model.setName(response.body().get(i).getName());
                       model.setPhoto_path(response.body().get(i).getPhoto_path());
                       model.setId(response.body().get(i).getId());
                       market_list.add(model);
                   }
                   market_adapter.notifyDataSetChanged();
                   market_recyclerview.setAdapter(market_adapter);
               }

            }

            @Override
            public void onFailure(Call<List<Markets>> call, Throwable t) {

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


}
