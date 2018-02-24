package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.CompaniesViewpagerAdapter;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.companies.fragments.CompanyAboutusFragment;
import com.example.meseret.niqugebere.companies.fragments.CompanyContactusFragment;
import com.example.meseret.niqugebere.companies.fragments.CompanyProductFragment;
import com.example.meseret.niqugebere.companies.fragments.CompanyServiceFragment;
import com.example.meseret.niqugebere.companies.fragments.NewsAndEventsFragment;
import com.example.meseret.niqugebere.companies.fragments.WorkingHoursFragment;
import com.example.meseret.niqugebere.helpers.CircleTransform;
import com.example.meseret.niqugebere.profile.cfsc.CFSCProfile;
import com.example.meseret.niqugebere.profile.cfsc.UnfinishedActivity;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.DemandsRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.ProfileSupplyAndDemandViewpagerAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.SuppliesRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.DemandsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.SuppliesAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CfscProfileModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Demands;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Supplies;
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
public class Fragment_cfsc_profile_home extends Fragment {

    private String token;
    private LinearLayout unfinished_layout;
    private TextView unfinish_message;
    private Button unfinish_action_btn;
    private String unfinished_type;

    private RecyclerView supply_recyclerview;
    private List<SuppliesAdapterModel> supply_list;
    private SuppliesRecyclerviewAdapter supply_adapter;

    private RecyclerView demand_recyclerview;
    private List<DemandsAdapterModel> demand_list;
    private DemandsRecyclerviewAdapter demand_adapter;

    private ArrayList<String> category_list;
    private ArrayList<Integer> category_id_holder;
    private int category_id;
    private CustomSpinnerAdapter registration_type_adapter;
    private Spinner show_only_spinner;
    private Resources res;
    private SharedPreferences preferences;
    private String company_name;

    private TabLayout tabLayout;
    private ArrayList<String> id_holder;
    private ProgressBar pr;
    private ViewPager viewPager;
    public Fragment_cfsc_profile_home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_fragment_cfsc_profile_home, container, false);

        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));



        viewPager=(ViewPager)view.findViewById(R.id.cfsc_profile_viewpager);
        setUpViewPager(viewPager);

        tabLayout=(TabLayout)view.findViewById(R.id.cfsc_profile_tabs);
        tabLayout.setupWithViewPager(viewPager);
        pr=(ProgressBar)view.findViewById(R.id.supplier_pr);
        // pr.setVisibility(View.VISIBLE);

        return  view;
    }

    public void  setUpViewPager(ViewPager viewPager){
        ProfileSupplyAndDemandViewpagerAdapter adapter=new ProfileSupplyAndDemandViewpagerAdapter(((AppCompatActivity)getActivity()).getSupportFragmentManager());

        ProfileSupplyFragement supply=new ProfileSupplyFragement();
        ProfileDemandFragment demand=new ProfileDemandFragment();
        adapter.addFrag(supply,"Supplies");
        adapter.addFrag(demand,"Demands");
        viewPager.setAdapter(adapter);
    }

    public void  getSupplies(View view){
        supply_list=new ArrayList<>();
        supply_adapter=new SuppliesRecyclerviewAdapter(getActivity(),supply_list);
       // supply_recyclerview=(RecyclerView)view.findViewById(R.id.supply_recyclerview);
        supply_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        supply_recyclerview.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Supplies>> call=client.getSupplies(getToken());
        call.enqueue(new Callback<List<Supplies>>() {
            @Override
            public void onResponse(Call<List<Supplies>> call, Response<List<Supplies>> response) {
                if (response.body().size()>0){
                    for (int i=0;i<response.body().size();i++){
                        SuppliesAdapterModel model=new SuppliesAdapterModel();
                        model.setProduct_photo(response.body().get(i).getProduct_photo());
                        model.setCompany_photo(response.body().get(i).getCompany_photo());
                        model.setCompany_name(response.body().get(i).getCompany_name());
                        model.setCategory_name(response.body().get(i).getCategory_name());
                        model.setSub_category_name(response.body().get(i).getSub_category_name());
                        model.setTitle(response.body().get(i).getTitle());
                        model.setPrice(response.body().get(i).getPrice());
                        model.setAvailability(response.body().get(i).getAvailability());
                        model.setId(response.body().get(i).getId());
                        model.setToken(getToken());
                        supply_list.add(model);
                    }
                }
                supply_adapter.notifyDataSetChanged();
                supply_recyclerview.setAdapter(supply_adapter);
            }

            @Override
            public void onFailure(Call<List<Supplies>> call, Throwable t) {

            }
        });
    }

    public void getDemands(View view){

        demand_list=new ArrayList<>();
        demand_adapter=new DemandsRecyclerviewAdapter(getActivity(),demand_list);
        //demand_recyclerview=(RecyclerView)view.findViewById(R.id.demand_recyclerview);
        demand_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        demand_recyclerview.setItemAnimator(new DefaultItemAnimator());
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<Demands>> call=client.getDemands(getToken());
        call.enqueue(new Callback<List<Demands>>() {
            @Override
            public void onResponse(Call<List<Demands>> call, Response<List<Demands>> response) {
                if (response.body().size()>0){
                    for (int i=0;i<response.body().size();i++){
                        DemandsAdapterModel model=new DemandsAdapterModel();
                        model.setProduct_photo(response.body().get(i).getProduct_photo());
                        model.setCompany_photo(response.body().get(i).getCompany_photo());
                        model.setCompany_name(response.body().get(i).getCompany_name());
                        model.setCategory_name(response.body().get(i).getCategory_name());
                        model.setSub_category_name(response.body().get(i).getSub_category_name());
                        model.setTitle(response.body().get(i).getTitle());
                        model.setPrice(response.body().get(i).getPrice());
                        model.setAvailability(response.body().get(i).getAvailability());
                        model.setId(response.body().get(i).getId());
                        model.setToken(getToken());
                        demand_list.add(model);
                    }
                }
                demand_adapter.notifyDataSetChanged();
                demand_recyclerview.setAdapter(demand_adapter);
            }

            @Override
            public void onFailure(Call<List<Demands>> call, Throwable t) {

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUnfinished_type() {
        return unfinished_type;
    }

    public void setUnfinished_type(String unfinished_type) {
        this.unfinished_type = unfinished_type;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
