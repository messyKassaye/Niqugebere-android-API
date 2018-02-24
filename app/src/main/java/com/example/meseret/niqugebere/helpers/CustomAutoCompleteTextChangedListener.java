package com.example.meseret.niqugebere.helpers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;


import com.example.meseret.niqugebere.MainActivity;

import java.util.ArrayList;

/**
 * Created by Meseret on 11/27/2017.
 */

public class CustomAutoCompleteTextChangedListener implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteTextChangedListener.java";
    Context context;
    private ArrayList<String> list;
    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;

    public CustomAutoCompleteTextChangedListener(Context context,AutoCompleteTextView textView){
        this.context = context;
        list= new ArrayList<>();
        autoCompleteTextView=textView;
        adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, list);

    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {

        // if you want to see in the logcat what the user types
        //Log.e(TAG, "User input: " + userInput);

        MainActivity mainActivity = ((MainActivity) context);

      /*  // query the database based on the user input
        mainActivity.item = MainActivity.getItemsFromDb(userInput.toString());

        // update the adapater*/
        adapter.notifyDataSetChanged();
        adapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_dropdown_item_1line, mainActivity.item);
        autoCompleteTextView.setAdapter(adapter);

    }

}