package com.example.meseret.niqugebere.profile.cfsc;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.meseret.niqugebere.R;
import com.example.meseret.niqugebere.companies.companyAdapter.ProductRecyclerviewAdapter;
import com.example.meseret.niqugebere.companies.companyAdapterModel.ProductAdapterModel;
import com.example.meseret.niqugebere.companies.companyClients.CompanyClient;
import com.example.meseret.niqugebere.companies.fragments.CompanyProductFragment;
import com.example.meseret.niqugebere.model.ResponseToken;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapter.CFSCProductReyclerviewAdapter;
import com.example.meseret.niqugebere.profile.cfsc.cfscAdapterModel.CFSCProductsAdapterModel;
import com.example.meseret.niqugebere.profile.cfsc.cfscClient.CFSCClient;
import com.example.meseret.niqugebere.profile.cfsc.cfscModel.Products;
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

public class UnfinishedActivity extends AppCompatActivity {
    private String token;
    private Toolbar toolbar;
    private String unfinish_type;
    private ViewFlipper flipper;
    private View upload_view,description_view,company_delivering_view;
    public static int PICK_IMAGE_FROM_GALLERY_REQUEST=1;
    private Uri uri;
    private SharedPreferences permission_preference;
    private SharedPreferences.Editor editor;
    private ProgressBar pr,de_pr,un_pr;

    private List<CFSCProductsAdapterModel> list;
    private CFSCProductReyclerviewAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unfinished);

        toolbar=(Toolbar)findViewById(R.id.un_toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        permission_preference=getSharedPreferences("permission",0);
        editor= permission_preference.edit();
        editor.putString("read_images","");
        editor.commit();

        editor.putString("token",getIntent().getStringExtra("token"));
        editor.commit();

        setToken(getIntent().getStringExtra("token"));
        setUnfinish_type(getIntent().getStringExtra("type"));

        flipper=(ViewFlipper)findViewById(R.id.un_flipper);
        un_pr=(ProgressBar)findViewById(R.id.un_pr);

        upload_view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.upload_company_profile_image_layout,null);
        description_view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.company_description_layout,null);
        company_delivering_view=LayoutInflater.from(getApplicationContext()).inflate(R.layout.product_delivering_layout,null);

        switch (getUnfinish_type()){
            case "profile_image":
                getSupportActionBar().setTitle(getResources().getString(R.string.upload_your_image));
                uploadImage();
                break;
            case "description":
                getSupportActionBar().setTitle(getResources().getString(R.string.describe_your_company));
                description();
                break;
        }

        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_left);
        // set the animation type's to ViewFlipperActivity
        flipper.setInAnimation(in);
        flipper.setOutAnimation(out);
        // set interval time for flipping between views
        flipper.setFlipInterval(3000);
        flipper.setDisplayedChild(6);
    }

    public void uploadImage(){
        TextView upload_info=(TextView)upload_view.findViewById(R.id.upload_info);
        Button upload_btn=(Button)upload_view.findViewById(R.id.upload_btn);
        upload_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (ActivityCompat.checkSelfPermission(UnfinishedActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(UnfinishedActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_IMAGE_FROM_GALLERY_REQUEST);
                    } else {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY_REQUEST);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                /*
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,PICK_IMAGE_FROM_GALLERY_REQUEST);*/
            }
        });
        flipper.removeAllViews();
        flipper.addView(upload_view);
        flipper.showNext();
    }
public void description(){
    TextView description_info=(TextView)description_view.findViewById(R.id.decription_info);
    final EditText description_edt=(EditText) description_view.findViewById(R.id.describe_your_company);
    Button update_description=(Button)description_view.findViewById(R.id.description_btn);
    pr=(ProgressBar)description_view.findViewById(R.id.dercibe_pr);
    update_description.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            pr.setVisibility(View.VISIBLE);
           String description_value=description_edt.getText().toString();
            Retrofit retrofit=getUserAPIretrofit();
            CFSCClient client=retrofit.create(CFSCClient.class);
            Call<ResponseToken> call=client.updateDescription(getToken(),description_value);
            call.enqueue(new Callback<ResponseToken>() {
                @Override
                public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
                    if (response.body().getStatus().equals("ok")){
                        deliveringProduct();
                    }else {

                    }
                }

                @Override
                public void onFailure(Call<ResponseToken> call, Throwable t) {

                }
            });

        }
    });
    flipper.removeAllViews();
    flipper.addView(description_view);
    flipper.showNext();

}

public void deliveringProduct(){
    TextView description_info=(TextView)company_delivering_view.findViewById(R.id.company_delivering_info);
    description_info.setText("Select what type of product your company will provide for your customer. This make your customer can find any product of your company");
    list=new ArrayList<>();
    adapter=new CFSCProductReyclerviewAdapter(this,list);
    recyclerView=(RecyclerView) company_delivering_view.findViewById(R.id.product_delivering_recyclerview);
    RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
    recyclerView.setLayoutManager(mLayoutManager);
    recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    Retrofit retrofit=getUserAPIretrofit();
    CFSCClient client=retrofit.create(CFSCClient.class);
    Call<List<Products>> call=client.getProducts(getToken());
    call.enqueue(new Callback<List<Products>>() {
        @Override
        public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
            for (int i=0;i<response.body().size();i++){
                CFSCProductsAdapterModel model=new CFSCProductsAdapterModel();
                model.setName(response.body().get(i).getName());
                model.setId(response.body().get(i).getId());
                model.setToken(getToken());
                list.add(model);
            }

            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onFailure(Call<List<Products>> call, Throwable t) {

        }
    });

    de_pr=(ProgressBar)company_delivering_view.findViewById(R.id.delivering_pr);
    flipper.removeAllViews();
    flipper.addView(company_delivering_view);
    flipper.showNext();
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
    public void uploadProfile(Uri fileUri){
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
        un_pr.setVisibility(View.VISIBLE);
        Retrofit retrofit=getUserAPIretrofit();
        CFSCClient client=retrofit.create(CFSCClient.class);
        Call<ResponseToken> call=client.upload(getToken(),body);
        call.enqueue(new Callback<ResponseToken>() {
            @Override
            public void onResponse(Call<ResponseToken> call, Response<ResponseToken> response) {
               if (response.body().getStatus().equals("ok")){
                   un_pr.setVisibility(View.GONE);
                   Intent intent=new Intent(UnfinishedActivity.this,CFSCProfile.class);
                   intent.putExtra("token",getToken());
                   startActivity(intent);
               }
            }

            @Override
            public void onFailure(Call<ResponseToken> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
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
    public String getUnfinish_type() {
        return unfinish_type;
    }

    public void setUnfinish_type(String unfinish_type) {
        this.unfinish_type = unfinish_type;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
