package com.example.meseret.niqugebere.profile.cfsc.cfscFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseret.niqugebere.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowPostedProductFragment extends Fragment {


    public ShowPostedProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_posted_product, container, false);
    }

}
