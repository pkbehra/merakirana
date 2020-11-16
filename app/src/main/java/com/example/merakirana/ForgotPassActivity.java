package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.Interface.IResult;
import com.example.Utility.InternetConnection;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.broadcastRecivers.CheckConnectivity;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.example.Utility.ConstantVariables.USER_FORGOT_PASSWORD;

public class ForgotPassActivity extends AppCompatActivity implements View.OnClickListener {

    TextView btnresetpass;
    EditText emailEt;
    ProgressDialog progressDialog;
    ImageView imgLogo;

    private IResult mResultCallback;
    private VolleyService mVolleyService;

    String forgot_pass_url="Forgot-Password-Customer";
    AlertDialog alertDialog;
    SessionManager sessionManager;
    CheckConnectivity checkConnectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_forgot_pass);

        init();

    }

    private void init() {
        btnresetpass=findViewById(R.id.change_pass_btn);
        emailEt=findViewById(R.id.edtEmail);
        imgLogo=findViewById(R.id.imgLogo);

        btnresetpass.setOnClickListener(this);

        sessionManager=new SessionManager(this);
        checkConnectivity=new CheckConnectivity();
        //set Logo
        setLogo();
    }

    private void resetPassword() {
        if (checkValid()){
            progressDialog = ProgressDialog.show(this, "Please Wait", null, false, true);

            initVolleyCallback();

            mVolleyService = new VolleyService(mResultCallback,this);

            Map<String, String> params = new HashMap<>();
            params.put("email",emailEt.getText().toString() );

            String url =getResources().getString(R.string.BASE_URL) + forgot_pass_url;

            mVolleyService.postDataVolleyParameters(USER_FORGOT_PASSWORD, url, params);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.change_pass_btn:

                if (InternetConnection.isConnectedToNetwork(this)){
                    resetPassword();
                }
                else {
                    showDialog("No internet available...!!",0);
                }
                break;

        }
    }

    private void showDialog(String message, final int status){
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Forgot Password")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (status==1){
                            startActivity(new Intent(ForgotPassActivity.this,LoginActivity.class));
                            finish();
                            alertDialog.dismiss();
                        }
                        else {
                            alertDialog.dismiss();
                        }
                    }
                }).show(); }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {

                    case USER_FORGOT_PASSWORD:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("forget pass response",response.toString());
                            //   Toast.makeText(Forgetpass.this,response.toString(),Toast.LENGTH_SHORT).show();
                            int status = jsonObj.getInt("status");

                            if (status == 0) {
                                showDialog(jsonObj.getString("msg"),status);
                                //  toastClass.makeToast(getApplicationContext(), "Please enter valid email id");
                            }
                            else if (status == 1) {
                                showDialog(jsonObj.getString("msg"),status);
                            }
                        }
                        catch (Exception e) {

                            Log.v("Forget Password", e.toString());
                        }
                        // progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();
                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                progressDialog.dismiss();
                showDialog("Something went wrong. Please try again !!!",0);
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    public boolean checkValid() {

        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        if (emailEt.getText().toString().equalsIgnoreCase("")) {
            emailEt.setError("Please enter email id");
            return false;
        }
        else if (!emailEt.getText().toString().matches(emailPattern)) {
            emailEt.setError("Please enter valid email id");
            return false;
        }

        return true;
    }

    private void setLogo() {

        String newstr=sessionManager.getFeatureLogo();
        String newurl=getResources().getString(R.string.base_url_logo)+newstr;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(newurl);
            imgLogo.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(checkConnectivity, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (checkConnectivity != null) {
            this.unregisterReceiver(checkConnectivity);
        }
    }
}
