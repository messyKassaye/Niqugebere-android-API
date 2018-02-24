package com.example.meseret.niqugebere.companies.companyAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.example.meseret.niqugebere.R;

import java.util.ArrayList;

/**
 * Created by Meseret on 11/26/2017.
 */

public class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Activity activity;
    private ArrayList data;
    public Resources res;
    String tempValues = null;
    LayoutInflater inflater;

    public CustomSpinnerAdapter(
            Activity activitySpinner,
            int textViewResourceId,
            ArrayList objects,
            Resources resLocal
    ) {
        super(activitySpinner, textViewResourceId, objects);

        activity = activitySpinner;
        data = objects;
        res = resLocal;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = inflater.inflate(R.layout.spinner_dropdown, parent, false);
        tempValues = null;
        tempValues = data.get(position).toString();

        TextView txt = (TextView) row.findViewById(R.id.dropdown_txt);
        txt.setGravity(Gravity.LEFT);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(16);
        txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_downarrow, 0);
        txt.setText(tempValues);
        txt.setTextColor(Color.WHITE);
        txt.setSingleLine(true);
        txt.setEllipsize(TextUtils.TruncateAt.END);
        txt.setSingleLine(true);

        return row;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(R.layout.spinner_dropdown, parent, false);
        tempValues = null;
        tempValues = data.get(position).toString();

        TextView txt = (TextView) row.findViewById(R.id.dropdown_txt);
        txt.setText(tempValues);
        txt.setTextSize(17);
        txt.setPadding(10, 30, 0, 30);
        txt.setGravity(Gravity.LEFT);
        txt.setTextColor(Color.parseColor("#1171d0"));
        txt.setBackgroundColor(Color.parseColor("#FFFFFF"));

        return row;
    }



}
