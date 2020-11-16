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
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.Interface.IResult;
import com.example.Model.UserLoginProfileModel;
import com.example.Utility.InternetConnection;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.broadcastRecivers.CheckConnectivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.example.Utility.ConstantVariables.USER_LOGIN;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText etMobile, etPassword;
    Button login;
    ImageView imgLogo;

    TextView tvSignup, tvForgotPass;

    AlertDialog alertDialog;
    ProgressDialog progressDialog;

    UserLoginProfileModel userLoginProfile;

    SessionManager sessionManager;
    String login_url = "Login-Customer";
    String getDetailsUrl = "show_buyer_profile";
    private IResult mResultCallback;
    private VolleyService mVolleyService;

    String action = "";

    CountryCodePicker ccp;
    CheckConnectivity checkConnectivity;
    private String mCountryCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* //remove status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        setContentView(R.layout.activity_login);
        init();
    }



    private void init() {
        etMobile = findViewById(R.id.phone);
        etPassword = findViewById(R.id.password);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        ccp=findViewById(R.id.ccp);

        // ccp.setClickable(false);

        sessionManager = new SessionManager(this);
        checkConnectivity=new CheckConnectivity();
        mCountryCode=ccp.getSelectedCountryNameCode()+"-"+ccp.getSelectedCountryCode();

        login = (Button) findViewById(R.id.email_sign_in_button);
        tvSignup = (TextView) findViewById(R.id.tvsignup);
        imgLogo=(ImageView)findViewById(R.id.imgLogo);

        //close keyboard
        ccp.setKeyboardAutoPopOnSearch(false);

        //set Logo
        setLogo();

       // generateToken();

        login.setOnClickListener(this);
        tvSignup.setOnClickListener(this);
        tvForgotPass.setOnClickListener(this);

        if (getIntent().getStringExtra("action") != null) {
            action = getIntent().getStringExtra("action");
        }


        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                mCountryCode=ccp.getSelectedCountryNameCode()+"-"+ccp.getSelectedCountryCode();
            }
        });


    }

    public boolean checkValid() {

        String contactPattern = "^[0-9]{10,15}$";
        String passwordPattern = "^[a-zA-Z0-9]{8,}$";

        if (etMobile.getText().toString().equalsIgnoreCase("")) {
            etMobile.setError("Please enter contact");
            return false;
        }
        else if (!etMobile.getText().toString().matches(contactPattern)) {
            etMobile.setError("Please enter valid contact");
            return false;
        }

        else if (etPassword.getText().toString().equalsIgnoreCase("")) {
            etPassword.setError("Please enter password");
            return false;
        }

        else if (etPassword.getText().length()<6) {
            etPassword.setError(" Password must be at least 6 characters");
            return false;
        }

        return true;
    }

    private void showDialog(String message) {
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Login")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.email_sign_in_button:
                if (InternetConnection.isConnectedToNetwork(this)) {
                    userLogin();
                } else {
                    showDialog("No internet connection..!!!");
                }
                break;

            case R.id.tvsignup:
                Intent intent = new Intent(getApplicationContext(), RegistrationActivity.class);
                startActivity(intent);
                break;

            case R.id.tvForgotPass:
                Intent intent1 = new Intent(getApplicationContext(), ForgotPassActivity.class);
                startActivity(intent1);
                break;

        }
    }

    private void userLogin() {
        if (checkValid()) {
            progressDialog = ProgressDialog.show(this, "Please Wait", null, false, true);

            initVolleyCallback();

            mVolleyService = new VolleyService(mResultCallback, this);
            String lToken=sessionManager.onGetToken();

            Map<String, String> params = new HashMap<>();
            //params.put("name", edtName.getText().toString());
            params.put("contact", etMobile.getText().toString());
            params.put("password", etPassword.getText().toString());
            params.put("country_code",mCountryCode);
            params.put("token",lToken);

            Log.d("params", params.toString());
            String url = getApplicationContext().getResources().getString(R.string.BASE_URL) + login_url;

            mVolleyService.postDataVolleyParameters(USER_LOGIN, url, params);
        }
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {

                    case USER_LOGIN:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("login response", response.toString());

                            //  Toast.makeText(Login.this,response.toString(),Toast.LENGTH_SHORT).show();
                            int status = jsonObj.getInt("status");

                            if (status == 1) {
                                sessionManager.createLoginSession(jsonObj.getString("customer_auto_id"));
                                showNext();
                            }
                            else {
                                showDialog(jsonObj.getString("msg"));
                            }
                        } catch (Exception e) {

                            Log.v("Register_Login", e.toString());
                        }
                        progressDialog.dismiss();
                        // progressBar.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                //  Toast.makeText(getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                showDialog("Something went wrong. Please try again !!!");
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private void showNext() {
        if (action.equals("action_cart")){
            Intent intent=new Intent(getApplicationContext(),CartActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
            finish();
        }
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
    protected void onPause() {
        super.onPause();
        if (checkConnectivity != null) {
            this.unregisterReceiver(checkConnectivity);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(checkConnectivity, intentFilter);

    }

//    public void generateToken() {
//
//        final String token =sessionManager.onGetToken();
//        Log.e("NEW_INACTIVITY_TOKEN", token);
//
//        if (TextUtils.isEmpty(token)) {
//            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
//                @Override
//                public void onSuccess(InstanceIdResult instanceIdResult) {
//                    String newToken = instanceIdResult.getToken();
//                    Log.e("newToken", newToken);
//                    if (token.isEmpty()){
//                        sessionManager.onSetToken(newToken);
//                    }
//                }
//            });
//        }
//    }
}
