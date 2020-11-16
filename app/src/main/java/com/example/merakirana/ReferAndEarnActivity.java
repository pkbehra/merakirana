package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Interface.AddToCartListener;
import com.example.Model.CartRVModel;
import com.example.Utility.SessionManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static com.example.Utility.SessionManager.KEY_ID;

public class ReferAndEarnActivity extends AppCompatActivity implements AddToCartListener {



    Toolbar toolbar;
    TextView textCartItemCount,tvTitle;
    ImageView ivback,ivWallet;
    MenuItem cartItem;
    Menu mMenu;
    SessionManager sessionManager;
    String productType="";

    ImageView cartImage;

    String newfeaturesList;
    String[] featureslist;
    List<String> listfeature;

    private Button btnInviteFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        init();
        setupToolbar();
    }

    private void init() {

        btnInviteFriends=(Button)findViewById(R.id.btnInviteFriends);

        sessionManager=new SessionManager(this);

        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listfeature = Arrays.asList(featureslist);

        btnInviteFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String,String> user=sessionManager.getUserDetails();
                String id=user.get(KEY_ID);

                // String applink = "https://play.google.com/";
                String messagelink = "This application is not available on playstore so we enable to provide application link";
//                messagelink = messagelink + applink + sid;
                // messagelink = messagelink + applink;

                Intent i = new Intent(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_SUBJECT, sessionManager.getBusinessName());
                i.putExtra(Intent.EXTRA_TEXT, messagelink);
                i.setType("text/plain");
                startActivity(Intent.createChooser(i, "Share via"));
            }
        });

    }

    private void setupToolbar() {
        toolbar=(Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tvTitle=(TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("Refer and Earn");

        ivWallet=(ImageView)findViewById(R.id.ivWallet);
        ivWallet.setVisibility(View.GONE);

        ivback=(ImageView)findViewById(R.id.ivBack);
        ivback.setVisibility(View.VISIBLE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        this.mMenu = menu;
        cartItem = mMenu.findItem(R.id.nav_myCart);

        View actionView = MenuItemCompat.getActionView(cartItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        cartImage=(ImageView)actionView.findViewById(R.id.cartImage);

        ArrayList<CartRVModel> cartModels = sessionManager.getAddToCartList(this);

        if (cartModels != null) {
            setupBadge();
        }


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(cartItem);
            }
        });

        return true;
    }

    @Override
    public void addToCart() {
        setupBadge();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.nav_myCart:
                Intent intent=new Intent(this,CartActivity.class);
                startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupBadge() {

        if(listfeature.contains("Cart")){

            ArrayList<CartRVModel> cartModels = sessionManager.getAddToCartList(this);

            if (cartModels != null) {
//badgeCount.setBadgeCount(this, iconlayer, String.valueOf(cartModels));

                if (textCartItemCount != null) {
                    if (cartModels.size() == 0) {
                        if (textCartItemCount.getVisibility() != View.GONE) {
                            textCartItemCount.setVisibility(View.GONE);
                        }
                    } else {

                        textCartItemCount.setText(String.valueOf(Math.min(cartModels.size(), 99)));

                        if (textCartItemCount.getVisibility() != View.VISIBLE) {
                            textCartItemCount.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }else {

            textCartItemCount.setVisibility(View.GONE);
            cartImage.setVisibility(View.GONE);
        }

    }

}
