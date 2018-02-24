package com.example.meseret.niqugebere.profile.cfsc.cfscAdapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Meseret on 2/13/2018.
 */

public class ProfileSupplyAndDemandViewpagerAdapter extends FragmentPagerAdapter {
    private List<android.support.v4.app.Fragment> fragmentList=new ArrayList<>();
    private List<String> fragment_name=new ArrayList<>();

    public ProfileSupplyAndDemandViewpagerAdapter(FragmentManager manager){
        super(manager);

    }

    public void  addFrag(android.support.v4.app.Fragment fragment, String title){
        fragmentList.add(fragment);
        fragment_name.add(title);
    }
    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return fragment_name.get(position);
    }
}
