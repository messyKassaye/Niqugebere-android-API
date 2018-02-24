package com.example.meseret.niqugebere.profile.transporter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.helpers.CircleTransform;
import com.example.meseret.niqugebere.profile.cfsc.CFSCProfile;
import com.example.meseret.niqugebere.profile.cfsc.FloatingButtonActivity;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.Fragment_cfsc_profile_home;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.NotificationFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CfscProfileModel;
import com.example.meseret.niqugebere.profile.transporter.transporterFragments.AddVehiclesFragment;
import com.example.meseret.niqugebere.profile.transporter.transporterFragments.MyPathFragment;
import com.example.meseret.niqugebere.profile.transporter.transporterFragments.MyVehiclesFragment;
import com.example.meseret.niqugebere.profile.transporter.transporterFragments.SetPathFragment;
import com.example.meseret.niqugebere.profile.transporter.transporterFragments.TransporterHomeFragment;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TransporterProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView company_name;
    private ImageView company_profile_logo;
    private String token;
    private Resources res;
    private Fragment fragment=null;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String company_name_value;
    private Menu main_menu;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transporter_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Farmer");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        token=getIntent().getStringExtra("token");
        preferences=getSharedPreferences("token",0);
        editor=preferences.edit();
        editor.putString("token",token);
        editor.commit();
        setToken(token);




        NavigationView navigationView = (NavigationView) findViewById(R.id.transport_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header = navigationView.getHeaderView(0);
        company_name = (TextView) header.findViewById(R.id.company_name);
        company_profile_logo=(ImageView)header.findViewById(R.id.company_profile);
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<List<CfscProfileModel>> call=client.getCfscProfile(token);
        call.enqueue(new Callback<List<CfscProfileModel>>() {
            @Override
            public void onResponse(Call<List<CfscProfileModel>> call, Response<List<CfscProfileModel>> response) {
                company_name.setText(response.body().get(0).getName());
                setCompany_name_value(response.body().get(0).getName());
                toolbar.setTitle(response.body().get(0).getName());

                Glide.with(TransporterProfileActivity.this)
                        .load(Uri.parse(Projectstatics.IMAPGE_PATH+response.body().get(0).getLogo())) // add your image url
                        .transform(new CircleTransform(TransporterProfileActivity.this)) // applying the image transformer
                        .into(company_profile_logo);
                // Picasso.with(getApplicationContext()).load(Projectstatics.IMAPGE_PATH+response.body().get(0).getLogo()).placeholder(R.drawable.background_tile2_small).into(company_profile_logo);




            }

            @Override
            public void onFailure(Call<List<CfscProfileModel>> call, Throwable t) {

            }
        });
        showHome();


    }
    public void showHome(){
        toolbar.setTitle(getCompany_name_value());
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment = new TransporterHomeFragment();
        ft.replace(R.id.placeholder, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCompany_name_value() {
        return company_name_value;
    }

    public void setCompany_name_value(String company_name_value) {
        this.company_name_value = company_name_value;
    }

    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cfscprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            showHome();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.set_path) {
            toolbar.setTitle(getCompany_name_value());
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new SetPathFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.my_path) {
            toolbar.setTitle(getCompany_name_value());
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new MyPathFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.add_vehicles) {
            toolbar.setTitle(getCompany_name_value());
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new AddVehiclesFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();

        } else if (id == R.id.show_vehicles) {
            toolbar.setTitle(getCompany_name_value());
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new MyVehiclesFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
