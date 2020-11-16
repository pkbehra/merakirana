package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Utility.SessionManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class WelcomeActivity extends AppCompatActivity {

    TextView tvCreateAccount;
    Button btnStartShopping;
    ImageView ivSplashLogo;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);


        init();

    }

    private void init() {

        sessionManager=new SessionManager(this);
        btnStartShopping=(Button)findViewById(R.id.btnStartShopping);
        tvCreateAccount=(TextView)findViewById(R.id.tvCreateAccount);
        ivSplashLogo=(ImageView)findViewById(R.id.ivSplashLogo);

        setLogo();


        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnStartShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void setLogo() {

        String newstr=sessionManager.getFeatureLogo();
        String newurl=getResources().getString(R.string.base_url_logo)+newstr;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(newurl);
            ivSplashLogo.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }

    }
}
