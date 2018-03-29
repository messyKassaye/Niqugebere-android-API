package com.example.meseret.niqugebere;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.meseret.niqugebere.clients.MainClient;
import com.example.meseret.niqugebere.fragments.HomeFragment;
import com.example.meseret.niqugebere.fragments.LoginFragment;
import com.example.meseret.niqugebere.fragments.PullFragment;
import com.example.meseret.niqugebere.fragments.PushFragment;
import com.example.meseret.niqugebere.fragments.SearchFragment;
import com.example.meseret.niqugebere.fragments.SignUpFragment;
import com.example.meseret.niqugebere.model.Version;
import com.example.meseret.niqugebere.projectstatics.Projectstatics;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public String[] item = new String[] {"Please search..."};
    private Menu menus;
    private TextView mTextMessage;
    private Toolbar toolbar;
    private SharedPreferences permission_preference;
    private SharedPreferences.Editor editor;

    private ProgressDialog bar;
    private static String TAG = "MainActivity";
    private int AppVersion = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    FragmentManager fm = getFragmentManager();
                    android.app.FragmentTransaction ft = fm.beginTransaction();
                    selectedFragment = new HomeFragment();
                    ft.replace(R.id.content, selectedFragment);
                    ft.addToBackStack(null);
                    ft.commit();
                    return true;
                case R.id.navigation_pull:
                    FragmentManager fmsearch = getFragmentManager();
                    android.app.FragmentTransaction ftsearch = fmsearch.beginTransaction();
                    selectedFragment = new PullFragment();
                    ftsearch.replace(R.id.content, selectedFragment);
                    ftsearch.addToBackStack(null);
                    ftsearch.commit();
                    return true;
                case R.id.navigation_push:
                    FragmentManager fmpush = getFragmentManager();
                    android.app.FragmentTransaction push = fmpush.beginTransaction();
                    selectedFragment = new PushFragment();
                    push.replace(R.id.content, selectedFragment);
                    push.addToBackStack(null);
                    push.commit();
                    return true;
                case R.id.navigation_login:
                    FragmentManager fmlog = getFragmentManager();
                    android.app.FragmentTransaction ftlogin = fmlog.beginTransaction();
                    selectedFragment = new LoginFragment();
                    ftlogin.replace(R.id.content, selectedFragment);
                    ftlogin.addToBackStack(null);
                    ftlogin.commit();
                    return true;
                case R.id.navigation_signup:
                    FragmentManager fmsign = getFragmentManager();
                    android.app.FragmentTransaction ftsingup = fmsign.beginTransaction();
                    selectedFragment = new SignUpFragment();
                    ftsingup.replace(R.id.content, selectedFragment);
                    ftsingup.addToBackStack(null);
                    ftsingup.commit();
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Smart Farmer");
        setSupportActionBar(toolbar);

        permission_preference=getSharedPreferences("permission",0);
        editor= permission_preference.edit();
        editor.putString("read_images","");
        editor.commit();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        disableShiftMode(navigation);
        FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction ft = fm.beginTransaction();
        Fragment selectedFragment = new HomeFragment();
        ft.replace(R.id.content, selectedFragment);
        ft.addToBackStack(null);
        ft.commit();
        call();

        Retrofit retrofit=getUserAPIretrofit();
        MainClient client=retrofit.create(MainClient.class);
        Call<Version> call=client.getVersions();
        call.enqueue(new Callback<Version>() {
            @Override
            public void onResponse(Call<Version> call, Response<Version> response) {
                if(response.isSuccessful()){
                    PackageInfo info=null;
                    try{
                        info =getPackageManager().getPackageInfo(getPackageName(), 0);
                    }catch (Exception e){

                    }

                   String app_version= String.valueOf(info.versionCode);
                    if(app_version.equals(response.body().getVersion_no())){
                         //open();
                    }
                }
            }

            @Override
            public void onFailure(Call<Version> call, Throwable throwable) {

            }
        });
    }

    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("New version of smart farmer is available");

        alertDialogBuilder.setNegativeButton("Remind me later",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setPositiveButton("Upgrade",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        new DownloadNewVersion().execute();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public Retrofit getUserAPIretrofit(){
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Projectstatics.URL)
                .addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menus=menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    private void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("BNVHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BNVHelper", "Unable to change value of shift mode", e);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.call:
                try
                {
                    if(Build.VERSION.SDK_INT > 22)
                    {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                            return true;
                        }

                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:0944181080"));
                        startActivity(callIntent);

                    }
                    else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:0944181080"));
                        startActivity(callIntent);
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }

                break;
            case R.id.language_selecter:
                View language_menu=findViewById(R.id.language_selecter);
                PopupMenu popup = new PopupMenu(MainActivity.this, language_menu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.language_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(MainActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void call(){
        EndCallListener callListener = new EndCallListener();
        TelephonyManager mTM = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        mTM.listen(callListener, PhoneStateListener.LISTEN_CALL_STATE);
    }


    private class EndCallListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if(TelephonyManager.CALL_STATE_RINGING == state) {
                Log.i("", "RINGING, number: " + incomingNumber);
            }
            if(TelephonyManager.CALL_STATE_OFFHOOK == state) {
                //wait for phone to go offhook (probably set a boolean flag) so you know your app initiated the call.
                Log.i("", "OFFHOOK");
            }
            if(TelephonyManager.CALL_STATE_IDLE == state) {
                //when this state occurs, and your flag is set, restart your app
                Log.i("", "IDLE");
            }
        }
    }

    class DownloadNewVersion extends AsyncTask<String,Integer,Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            bar = new ProgressDialog(MainActivity.this);
            bar.setCancelable(false);

            bar.setMessage("Downloading...");

            bar.setIndeterminate(true);
            bar.setCanceledOnTouchOutside(false);
            bar.show();

        }

        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);

            bar.setIndeterminate(false);
            bar.setMax(100);
            bar.setProgress(progress[0]);
            String msg = "";
            if(progress[0]>99){

                msg="Finishing... ";

            }else {

                msg="Downloading... "+progress[0]+"%";
            }
            bar.setMessage(msg);

        }
        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);

            bar.dismiss();

            if(result){

                Toast.makeText(getApplicationContext(),"Update Done",
                        Toast.LENGTH_SHORT).show();

            }else{

                Toast.makeText(getApplicationContext(),"Error: Try Again",
                        Toast.LENGTH_SHORT).show();

            }

        }

        @Override
        protected Boolean doInBackground(String... arg0) {
            Boolean flag = false;

            try {


                URL url = new URL("http://niqugebere.malladdis.com/niqugebere.apk");

                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();


                String PATH = Environment.getExternalStorageDirectory()+"/Download/";
                File file = new File(PATH);
                file.mkdirs();

                File outputFile = new File(file,"niqugebere.apk");

                if(outputFile.exists()){
                    outputFile.delete();
                }

                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                int total_size = 1431692;//size of apk

                byte[] buffer = new byte[1024];
                int len1 = 0;
                int per = 0;
                int downloaded=0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1);
                    downloaded +=len1;
                    per = (int) (downloaded * 100 / total_size);
                    publishProgress(per);
                }
                fos.close();
                is.close();

                OpenNewVersion(PATH);

                flag = true;
            } catch (Exception e) {
                Log.e(TAG, "Update Error: " + e.getMessage());
                flag = false;
                Log.d("Version_error",e.getMessage());
            }
            return flag;

        }

    }

    void OpenNewVersion(String location) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(location + "niqugebere.apk")),
                "application/vnd.android.package-archive");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }



}
