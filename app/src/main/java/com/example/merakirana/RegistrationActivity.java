package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Interface.IResult;
import com.example.Utility.InternetConnection;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
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

import static com.example.Utility.ConstantVariables.USER_REGISTRATION;
import static com.example.Utility.SessionManager.REFERRER_ID;

public class RegistrationActivity extends AppCompatActivity  {

    TextView tvLogin, tvTerms;
    Context context;

    EditText etName,etEmail,etMobile,etPassword;
    Button btnRegister;

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;
    private String url = "http://www.centriotech.com/merakirana/customer_register.php";
    private CountryCodePicker mCountryCodePicker;
    String country_code="";
    ImageView ivRegisterLogo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        tvLogin = findViewById(R.id.tvLogin);
        tvTerms = findViewById(R.id.tvTerms);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.edtNumber);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);
        mCountryCodePicker = findViewById(R.id.ccp);
        ivRegisterLogo = findViewById(R.id.ivRegisterLogo);

        // sessionManager = new SessionManager(this);


        //close keyboard
        mCountryCodePicker.setKeyboardAutoPopOnSearch(false);

        // mCountryCodePicker.setClickable(false);
        country_code = mCountryCodePicker.getSelectedCountryNameCode() + "-" + mCountryCodePicker.getSelectedCountryCode();

        mCountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                country_code = mCountryCodePicker.getSelectedCountryNameCode() + "-" + mCountryCodePicker.getSelectedCountryCode();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (etName.getText().toString().length()>1 && etMobile.getText().toString().length()>1&&etEmail.getText().toString().length()>11&&etPassword.getText().toString().length()>1)

                        Addcustomer(etName.getText().toString(), etMobile.getText().toString(), etEmail.getText().toString(), etPassword.getText().toString());


                else{
                    Toast.makeText(getApplicationContext(), "Please enter compulsory fields", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    public void Addcustomer(final String register_name, final String register_contact, final String register_email, final String register_password){

        mStringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result.trim().equals("data inserted")){
                    Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etMobile.setText("");
                    etEmail.setText("");
                    etPassword.setText("");

                }

                if (result.trim().equals("data not inserted")) {


                    Toast.makeText(getApplicationContext(), "Registration Failed " + "please try again", Toast.LENGTH_LONG).show();

                }
                if (result.trim().equals("User Already Exists")) {


                    Toast.makeText(getApplicationContext(),  "User Already Exists", Toast.LENGTH_LONG).show();

                }
                if (result.trim().equals("exception") || result.equals("unsuccessful")) {

                    Toast.makeText(getApplicationContext(), "OOPs! Something went wrong. Connection Problem.", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Tag","Error :" + error.toString());

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {

                Map<String, String>  params = new HashMap<String, String>();
                params.put("name", register_name);
                params.put("contact",register_contact );
                params.put("email", register_email);
                params.put("password", register_password);

                return params;
            }
        };
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(mStringRequest);
    }



}
