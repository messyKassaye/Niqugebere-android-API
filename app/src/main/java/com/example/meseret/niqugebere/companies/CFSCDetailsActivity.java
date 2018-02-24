package com.example.meseret.niqugebere.companies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.CompaniesViewpagerAdapter;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.companyModels.CompanyDetail;
import com.example.meseret.niqugebere.companies.fragments.CompanyAboutusFragment;
import com.example.meseret.niqugebere.companies.fragments.CompanyContactusFragment;
import com.example.meseret.niqugebere.companies.fragments.CompanyProductFragment;
import com.example.meseret.niqugebere.companies.fragments.CompanyServiceFragment;
import com.example.meseret.niqugebere.companies.fragments.NewsAndEventsFragment;
import com.example.meseret.niqugebere.companies.fragments.WorkingHoursFragment;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.lang.reflect.Field;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CFSCDetailsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout htab_layout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ImageView header_image;
    private ViewPager viewPager;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private TextView top_sellers_name;
    private String fontPath = "fonts/Allura-Regular.otf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfscdetails);

        toolbar=(Toolbar)findViewById(R.id.topsellers_htab_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        try {
            Field declaredField = toolbar.getClass().getDeclaredField("mTitleTextView");
            declaredField.setAccessible(true);
            TextView titleTextView = (TextView) declaredField.get(toolbar);
            ViewGroup.LayoutParams layoutParams = titleTextView.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            titleTextView.setLayoutParams(layoutParams);
            titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        } catch (Exception e) {
            //"Error!"
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        header_image=(ImageView)findViewById(R.id.topsellers_htab_header);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.topsellers_htab_collapse_toolbar);
        htab_layout=(TabLayout)findViewById(R.id.topsellers_htab_tabs);

        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);
        top_sellers_name=(TextView)findViewById(R.id.top_sellers_company_name);
        top_sellers_name.setTypeface(tf);
        final Intent intent=getIntent();
        preferences=getSharedPreferences("company_data",0);
        editor= preferences.edit();
        editor.putString("id",intent.getStringExtra("id"));
        editor.commit();

        Retrofit retrofit=getUserAPIretrofit();
        CompanyClient client=retrofit.create(CompanyClient.class);
        Call<List<CompanyDetail>> call=client.getCompany(intent.getStringExtra("id"));
        call.enqueue(new Callback<List<CompanyDetail>>() {
            @Override
            public void onResponse(Call<List<CompanyDetail>> call, Response<List<CompanyDetail>> response) {
                toolbar.setTitle(response.body().get(0).getName());
                top_sellers_name.setText(response.body().get(0).getName());
                Glide.with(getApplicationContext()).load(Projectstatics.IMAPGE_PATH+response.body().get(0).getPhoto()).into(header_image);
            }
            @Override
            public void onFailure(Call<List<CompanyDetail>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();

            }
        });


        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.header);
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @SuppressWarnings("ResourceType")
                @Override
                public void onGenerated(Palette palette) {
                    collapsingToolbarLayout.setContentScrimColor(R.color.primary_500);
                    collapsingToolbarLayout.setStatusBarScrimColor(R.color.primary_700);
                }
            });

        } catch (Exception e) {
            // if Bitmap fetch fails, fallback to primary colors
            collapsingToolbarLayout.setContentScrimColor(
                    ContextCompat.getColor(this, R.color.primary_500)
            );
            collapsingToolbarLayout.setStatusBarScrimColor(
                    ContextCompat.getColor(this, R.color.primary_700)
            );
        }
        viewPager=(ViewPager)findViewById(R.id.top_sellers_viewpager);
        setUpViewPager(viewPager);

        htab_layout=(TabLayout)findViewById(R.id.topsellers_htab_tabs);
        htab_layout.setupWithViewPager(viewPager);


    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    public void  setUpViewPager(ViewPager viewPager){
        CompaniesViewpagerAdapter adapter=new CompaniesViewpagerAdapter(getSupportFragmentManager());

        CompanyProductFragment ourProduct=new CompanyProductFragment();
        CompanyAboutusFragment aboutus=new CompanyAboutusFragment();
        CompanyContactusFragment contactus=new CompanyContactusFragment();
        CompanyServiceFragment service=new CompanyServiceFragment();
        NewsAndEventsFragment news_and_events=new NewsAndEventsFragment();
        WorkingHoursFragment hoursFragment=new WorkingHoursFragment();
        adapter.addFrag(ourProduct,"Our product");
        adapter.addFrag(service,"Services");
        adapter.addFrag(aboutus,"About us");
        adapter.addFrag(contactus,"Contact us");
        adapter.addFrag(news_and_events,"News and events");
        adapter.addFrag(hoursFragment,"Hours");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
