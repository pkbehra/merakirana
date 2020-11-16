package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Fragment.FeaturesFragment;
import com.example.Fragment.GalleryFragment;
import com.example.Fragment.OverviewFragment;
import com.example.Fragment.SpecificationFragment;
import com.example.Utility.SessionManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


public class ProdDetailsActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    Toolbar toolbar;
    TextView tvTitle;
    ImageView ivback,ivWallet;
    String name,price;
    int image;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_details);


        toolbar=(Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        ivback=(ImageView)findViewById(R.id.ivBack);
        tvTitle=(TextView)findViewById(R.id.tvTitle);
        ivWallet=(ImageView)findViewById(R.id.ivWallet);

        tvTitle.setText("Car Tire");
        ivback.setVisibility(View.VISIBLE);
        ivWallet.setVisibility(View.GONE);

        ivback.setVisibility(View.VISIBLE);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sessionManager=new SessionManager(this);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        setUpVIewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        name = getIntent().getExtras().getString("name");
        price = getIntent().getExtras().getString("price");
        image = getIntent().getExtras().getInt("image");

    }

    public void setUpVIewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OverviewFragment(), "Overview");
        adapter.addFrag(new FeaturesFragment(), "Features");
        adapter.addFrag(new SpecificationFragment(), "Specifications");
        adapter.addFrag(new GalleryFragment(), "Gallery");

        viewPager.setAdapter(adapter);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
