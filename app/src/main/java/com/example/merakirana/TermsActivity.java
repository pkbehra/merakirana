package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

public class TermsActivity extends AppCompatActivity {
    TextView tvTerms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        tvTerms=(TextView)findViewById(R.id.tvTerms);

        tvTerms.setText("Neque porro quisquam est quidolorem ipsum quia dolor sit amet, consectetur,dipisci velit.\n" +
                "\nNeque porro quisquam est quidolorem ipsum quia dolor sit amet, consectetur,dipisci velit.\n"+
                "\nNeque porro quisquam est quidolorem ipsum quia dolor sit amet, consectetur,dipisci velit.");


        Toolbar toolbar = (Toolbar) findViewById(R.id.skintool);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
