package com.example.meseret.niqugebere.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.adaptermodel.SponsorAdapterModel;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Meseret on 1/20/2018.
 */

public class SponsorAdapter extends PagerAdapter {
    private ArrayList<SponsorAdapterModel> images;
    private LayoutInflater inflater;
    private Context context;

    public SponsorAdapter(Context context, ArrayList<SponsorAdapterModel> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        SponsorAdapterModel model=images.get(position);
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        TextView sponsorname=(TextView)myImageLayout.findViewById(R.id.sponsor_name);
        //myImage.setImageResource(images.get(position));
        Picasso.with(context).load(Projectstatics.IMAPGE_PATH+model.getPhoto()).placeholder(R.drawable.background_tile2_small).into(myImage);
        sponsorname.setText(context.getResources().getString(R.string.partners)+" "+model.getName());

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}