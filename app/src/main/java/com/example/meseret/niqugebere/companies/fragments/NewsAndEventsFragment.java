package com.example.meseret.niqugebere.companies.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.meseret.niqugebere.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsAndEventsFragment extends Fragment {


    public NewsAndEventsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_and_events, container, false);
    }

}
