package com.example.meseret.niqugebere.profile.cfsc;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.CustomSpinnerAdapter;
import com.example.meseret.niqugebere.helpers.CircleTransform;
import com.example.meseret.niqugebere.helpers.CountDrawable;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.SuppliesRecyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.SuppliesAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.Fragment_cfsc_profile_home;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.NotificationFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.PostInventoryFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.PostSupplyFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.PostdDemandFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.ShowDemandFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.ShowMyInventoryFragment;
import com.example.meseret.niqugebere.profile.cfsc.cfscFragments.ShowSupply;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.CfscProfileModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Supplies;
import com.example.meseret.niqugebere.profile.uploaders.FileUtils;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CFSCProfile extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private Toolbar toolbar;
    private TextView company_name;
    private ImageView company_profile_logo;
    private String token;
    private LinearLayout unfinished_layout;
    private TextView unfinish_message;
    private Button unfinish_action_btn;
    private String unfinished_type;

    private RecyclerView supply_recyclerview;
    private List<SuppliesAdapterModel> supply_list;
    private SuppliesRecyclerviewAdapter supply_adapter;

    private ArrayList<String> category_list;
    private ArrayList<Integer> category_id_holder;
    private int category_id;
    private CustomSpinnerAdapter registration_type_adapter;
    private Spinner show_only_spinner;
    private Resources res;
    private Fragment fragment=null;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String company_name_value;
    private Menu main_menu;
    private String response_message;
    private LinearLayout upload_cover_layout;
    private TextView upload_textview;
    private Button upload_cover_btn;
    private int PICK_IMAGE_FROM_GALLERY_REQUEST=1;
    private Uri uri;
    private ProgressBar pr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cfscprofile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Smart Farmer");
        setSupportActionBar(toolbar);

        token=getIntent().getStringExtra("token");
        preferences=getSharedPreferences("token",0);
        editor=preferences.edit();
        editor.putString("token",token);
        editor.commit();
       setToken(token);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),FloatingButtonActivity.class);
                intent.putExtra("token",getToken());
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
                if(response.isSuccessful()){
                    if(response.body().size()>0){
                        company_name.setText(response.body().get(0).getName());
                        setCompany_name_value(response.body().get(0).getName());
                        toolbar.setTitle(response.body().get(0).getName());

                        Glide.with(CFSCProfile.this)
                                .load(Uri.parse(Projectstatics.IMAPGE_PATH+response.body().get(0).getLogo())) // add your image url
                                .transform(new CircleTransform(CFSCProfile.this)) // applying the image transformer
                                .into(company_profile_logo);
                    }else{
                       Toast.makeText(getApplicationContext(),"alllo",Toast.LENGTH_LONG).show();
                        upload_cover_layout=(LinearLayout)findViewById(R.id.profile_uploads_cover_layout);
                        upload_cover_btn=(Button)findViewById(R.id.upload_cover_btn);
                        pr=(ProgressBar)findViewById(R.id.profile_center_pr);
                        upload_cover_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                pr.setVisibility(View.VISIBLE);
                                try {
                                    if (ActivityCompat.checkSelfPermission(CFSCProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                        ActivityCompat.requestPermissions(CFSCProfile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_FROM_GALLERY_REQUEST);
                                    } else {
                                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY_REQUEST);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        upload_cover_layout.setVisibility(View.VISIBLE);

                    }
                }
                   // Picasso.with(getApplicationContext()).load(Projectstatics.IMAPGE_PATH+response.body().get(0).getLogo()).placeholder(R.drawable.background_tile2_small).into(company_profile_logo);




            }

            @Override
            public void onFailure(Call<List<CfscProfileModel>> call, Throwable t) {

            }
        });
        showHome();
         //Toast.makeText(getApplicationContext(),getNotifications(),Toast.LENGTH_LONG).show();
        getNotifications();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==PICK_IMAGE_FROM_GALLERY_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            uri=data.getData();
            uploadProfile(uri);

        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY_REQUEST);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public void uploadProfile(Uri fileUri) {
        RequestBody token_data = RequestBody.create(MultipartBody.FORM, getToken());

        File file = FileUtils.getFile(this, fileUri);
        final RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"),
                        file
                );
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("photo", file.getName(), requestFile);

        // MultipartBody.Part is used to send also the actual file name
        Retrofit retrofit = getUserAPIretrofit();
        CFSCClient client = retrofit.create(CFSCClient.class);
        Call<ResponseToken> call = client.upload(getToken(), body);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if (response.body().getStatus().equals("ok")) {
                    pr.setVisibility(View.GONE);
                    upload_cover_layout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        main_menu=menu;
        //String notification_no=getNotifications();
        getNotifications();
        return super.onPrepareOptionsMenu(menu);
    }

    public void setCount(Context context, String count) {
        MenuItem menuItem = main_menu.findItem(R.id.action_notification);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
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
        }else if(id==R.id.action_notification){
            toolbar.setTitle(getCompany_name_value());
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new NotificationFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    public void getNotifications(){
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<ResponseToken> call=client.getNotifications(getToken());
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                if(response.isSuccessful()){
                    setCount(getApplicationContext(),response.body().getStatus());
                }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {

            }
        });

       // return  response_message;
    }

    public void showHome(){
        toolbar.setTitle(getCompany_name_value());
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fragment = new Fragment_cfsc_profile_home();
        ft.replace(R.id.placeholder, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();

        if (id == R.id.cfsc_post_supply) {
            getSupportActionBar().setTitle("Post Supply");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new PostSupplyFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.cfsc_show_post_supply) {
            getSupportActionBar().setTitle("Show Supply");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new ShowSupply();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.cfsc_post_demand) {
            getSupportActionBar().setTitle("Post demand");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new PostdDemandFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.cfsc_show_demand) {
            getSupportActionBar().setTitle("Show demand");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new ShowDemandFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.send_inventory) {
            getSupportActionBar().setTitle("Post inventory");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new PostInventoryFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        } else if (id == R.id.inventory_payment) {
            getSupportActionBar().setTitle("Inventory payment");
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new ShowMyInventoryFragment();
            ft.replace(R.id.placeholder, fragment);
            ft.addToBackStack(null);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

    public String getCompany_name_value() {
        return company_name_value;
    }

    public void setCompany_name_value(String company_name_value) {
        this.company_name_value = company_name_value;
    }
}
