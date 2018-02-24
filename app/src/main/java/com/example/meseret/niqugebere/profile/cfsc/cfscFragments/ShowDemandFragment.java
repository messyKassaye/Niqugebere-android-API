package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.ProfileSupplyAndDemandViewpagerAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowDemandFragment extends Fragment {

    private TabLayout tabLayout;
    private ArrayList<String> id_holder;
    private ProgressBar pr;
    private ViewPager viewPager;

    private SharedPreferences preferences;
    private String token;
    public ShowDemandFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_show_demand, container, false);
        preferences=getActivity().getSharedPreferences("token",0);
        setToken(preferences.getString("token",null));

        viewPager=(ViewPager)view.findViewById(R.id.show_demand_viewpager);
        setUpViewPager(viewPager);

        tabLayout=(TabLayout)view.findViewById(R.id.show_demand_tabs);
        tabLayout.setupWithViewPager(viewPager);
        pr=(ProgressBar)view.findViewById(R.id.supplier_pr);
        return view;
    }
    public void  setUpViewPager(ViewPager viewPager){
        ProfileSupplyAndDemandViewpagerAdapter adapter=new ProfileSupplyAndDemandViewpagerAdapter(((AppCompatActivity)viewPager.getContext()).getSupportFragmentManager());
        MyDemandRequestFragment mydemand=new MyDemandRequestFragment();
        DemandHistoryFragment demand=new DemandHistoryFragment();
        adapter.addFrag(mydemand,"My demand Request");
        adapter.addFrag(demand,"Demand history");
        viewPager.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
