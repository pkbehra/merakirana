package com.example.merakirana;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toolbar;

import com.example.Fragment.ChangePass;
import com.example.Fragment.ProfileFragment;
import com.example.Utility.SessionManager;
import com.example.broadcastRecivers.CheckConnectivity;
import com.example.service.BlockStatusService;

import java.util.Arrays;
import java.util.List;

public class AccountActivity extends AppCompatActivity {


    ImageButton ibProfile, ibResult, ibWallet, ibResetPass;
    RelativeLayout rlwallet;
    String newfeaturesList;
    String[] featureslist;
    List<String> listfeature;

    SessionManager sessionManager;
    private CheckConnectivity checkConnectivity;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.skintool);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        checkConnectivity=new CheckConnectivity();

        ibProfile = (ImageButton) findViewById(R.id.ibProfile);
        ibWallet = (ImageButton) findViewById(R.id.ibWallet);
        ibResetPass = (ImageButton) findViewById(R.id.ibResetPass);
        rlwallet=(RelativeLayout)findViewById(R.id.rlwallet);

        sessionManager=new SessionManager(this);
        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listfeature = Arrays.asList(featureslist);



        if(sessionManager.isLoggedIn())
        {
            if(listfeature.contains("Wallet"))
            {
                rlwallet.setVisibility(View.VISIBLE);

            }else {
                rlwallet.setVisibility(View.GONE);
            }
        }else {
            rlwallet.setVisibility(View.GONE);
        }

        ibProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ProfileFragment();
                FragmentManager fm = getSupportFragmentManager();
                if (fragment != null) {
                    fm.beginTransaction()
                            .replace(R.id.frameAccount, fragment)
                            .addToBackStack(null)
                            .commit();
                }


            }
        });

        ibWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*
                Fragment fragment = new WalletFragment();
                FragmentManager fm = getSupportFragmentManager();
                if (fragment != null) {
                    fm.beginTransaction()
                            .add(R.id.frameAccount, fragment)
                            .addToBackStack(null)
                            .commit();
                }*/

                startActivity(new Intent(AccountActivity.this,WalletActivity.class));


            }
        });

        ibResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ChangePass();
                FragmentManager fm = getSupportFragmentManager();
                if (fragment != null) {
                    fm.beginTransaction()
                            .replace(R.id.frameAccount, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(checkConnectivity, intentFilter);

        //to check block status register broadcast
        Intent background = new Intent(AccountActivity.this, BlockStatusService.class);
        startService(background);
        registerReceiver(broadcastReceiver, new IntentFilter("efunhub.com.automobile.closeactivity"));

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (checkConnectivity != null) {
            this.unregisterReceiver(checkConnectivity);
        }

        //to check block status Unregister broadcast
        Intent background = new Intent(AccountActivity.this, BlockStatusService.class);
        stopService(background);
        unregisterReceiver(broadcastReceiver);

    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("efunhub.com.automobile.closeactivity")) {
                finish();
            }
        }
    };



    /*
    public void pushFragment(Fragment fragment, boolean clearBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        if (clearBackStack && manager.getBackStackEntryCount() > 0) {
            try {
                manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String backStateName = fragment.getClass().getName();
        boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

        if (!fragmentPopped && manager.findFragmentByTag(backStateName) == null) {
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.frameHome, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }
*/


}
