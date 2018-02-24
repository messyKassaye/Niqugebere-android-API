package com.example.meseret.niqugebere;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
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

import com.example.meseret.niqugebere.fragments.HomeFragment;
import com.example.meseret.niqugebere.fragments.LoginFragment;
import com.example.meseret.niqugebere.fragments.PullFragment;
import com.example.meseret.niqugebere.fragments.PushFragment;
import com.example.meseret.niqugebere.fragments.SearchFragment;
import com.example.meseret.niqugebere.fragments.SignUpFragment;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {
    public String[] item = new String[] {"Please search..."};
    private Menu menus;
    private TextView mTextMessage;
    private Toolbar toolbar;
    private SharedPreferences permission_preference;
    private SharedPreferences.Editor editor;

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
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0944181080"));
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
                startActivity(callIntent);
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
}
