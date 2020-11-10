package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    Button login;
    TextView SignnUp, Forgetpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login=(Button)findViewById(R.id.email_sign_in_button);
        SignnUp=(TextView) findViewById(R.id.tvsignup);
        Forgetpass =(TextView) findViewById(R.id.tvForgotPass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(LoginActivity.this, "Wellcome Activity", Toast.LENGTH_SHORT).show();
            }
        });

        SignnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,RegistrationActivity.class);
                startActivity(intent);


            }
        });
        Forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(LoginActivity.this,ForgotPassActivity.class);
                startActivity(intent);
            }
        });
    }
}
