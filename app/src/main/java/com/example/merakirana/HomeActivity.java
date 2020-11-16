package com.example.merakirana;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.Adapter.CategoryExpandableListAdapter;
import com.example.Adapter.ExpandableListAdapter;
import com.example.Adapter.SubChildAdapter;
import com.example.Fragment.HomeFragment;
import com.example.Fragment.WishlistFragment;
import com.example.Interface.AddToCartListener;
import com.example.Interface.IResult;
import com.example.Interface.RemoveCart;
import com.example.Interface.RemoveWishlist;
import com.example.Model.CartRVModel;
import com.example.Model.CategoryBaseModel;
import com.example.Model.CategoryModel;
import com.example.Model.MenuModel;
import com.example.Model.SubCategoryModel;
import com.example.Utility.InternetConnection;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import static com.example.Utility.ConstantVariables.SHOW_MAIN_CATEGORY;

public class HomeActivity extends AppCompatActivity
        implements View.OnClickListener,NavigationView.OnNavigationItemSelectedListener, AddToCartListener, RemoveWishlist, RemoveCart {


    // ExpandableListAdapter expandableListAdapter;
    CategoryExpandableListAdapter expandableListAdapter;

    private SubChildAdapter subChildAdapter;

    ExpandableListView expandableListView;
    List<MenuModel> listTitles = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> listDataChild = new HashMap<>();
    HashMap<MenuModel, List<MenuModel>> listDataSubChild = new HashMap<>();

    MenuItem cartItem;
    MenuItem walletItem;
    Menu mMenu;
    SessionManager sessionManager;
    Context context;
    private boolean shouldLoadHomeFragOnBackPress = true;
    public static int navItemIndex = 0;

    TextView tvTitle, textCartItemCount;
    ImageView ivBack, ivIcon;
    DrawerLayout drawer;
    ImageView imgNavLogo;
    ImageView cartImage;
    ImageView imgPowerbyLogo;

    Toolbar toolbar;

    final String show_category_url="Show-Category";
    //  final String show_category_url="Show-Category";

    private IResult mResultCallback;
    private VolleyService mVolleyService;

    CategoryBaseModel categoryBaseModel;
    List<CategoryModel> categoryModelList=new ArrayList<>();
    String newfeaturesList;
    String[] featureslist;
    List<String> list;

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        setupToolbar();
        // expandableListView.setGroupIndicator(null);

        pushFragment(HomeFragment.newInstance(), true);

        // prepareMenuData();
        //populateMenuData();

        getData();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //inject navigation header
        View header = navigationView.getHeaderView(0);
        imgNavLogo=(ImageView)header.findViewById(R.id.imgNavLogo);

        imgPowerbyLogo=findViewById(R.id.imgPowerbyLogo);
        imgPowerbyLogo.setOnClickListener(this);

        String newstr=sessionManager.getFeatureLogo();
        String newurl=getResources().getString(R.string.base_url_logo)+newstr;


        //set navigation header image from session
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL(newurl);
            imgNavLogo.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        } catch (IOException e) {
            //Log.e(TAG, e.getMessage());
        }

    }

    private void getData() {

        if (InternetConnection.isConnectedToNetwork(this)){
            showMainCategory();
        }
        else {
            setupNavigrionDrawer(0);
            populatNavMenuData();
        }
    }


    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvTitle.setText("Merakirana");
    }

    private void init() {

        expandableListView = findViewById(R.id.expandableLV);

        sessionManager = new SessionManager(this);


        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ivBack = (ImageView) findViewById(R.id.ivBack);

        pushFragment(HomeFragment.newInstance(), true);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ivBack.setOnClickListener(this);

        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        list = Arrays.asList(featureslist);


    }

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
            ft.replace(R.id.frameMain1, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setTitle("Confirm Exit");
// builder.setIcon(R.drawable.icon_exit);
            builder.setMessage("Do you want to exit?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);

        this.mMenu = menu;
        cartItem = mMenu.findItem(R.id.nav_myCart);
        walletItem=mMenu.findItem(R.id.nav_myWallet);

        //check is user purchased wallet feature


        View actionView = MenuItemCompat.getActionView(cartItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        cartImage=(ImageView)actionView.findViewById(R.id.cartImage);

        ArrayList<CartRVModel> cartModels = sessionManager.getAddToCartList(context);

        if (cartModels != null) {
            setupBadge();
        }

        if(sessionManager.isLoggedIn())
        {
            if(list.contains("Wallet"))
            {
                walletItem.setVisible(true);
            }else {
                walletItem.setVisible(false);
            }

        }else {
            walletItem.setVisible(false);
        }

        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionsItemSelected(cartItem);
            }


        });

        return true;
    }

    private void setupBadge() {

        if(list.contains("Cart"))
        {
            ArrayList<CartRVModel> cartModels = sessionManager.getAddToCartList(context);

            if (cartModels != null) {

                if (textCartItemCount != null) {
                    if (cartModels.size() == 0) {
                        if (textCartItemCount.getVisibility() != View.GONE) {
                            textCartItemCount.setVisibility(View.GONE);
                            // cartImage.setVisibility(View.GONE);
                        }
                    } else {

                        textCartItemCount.setText(String.valueOf(Math.min(cartModels.size(), 99)));

                        if (textCartItemCount.getVisibility() != View.VISIBLE) {
                            textCartItemCount.setVisibility(View.VISIBLE);
                            // cartImage.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

        }else {
            cartImage.setVisibility(View.GONE);
            textCartItemCount.setVisibility(View.GONE);
        }


    }

    private void loadFragment() {
        FragmentManager fm=getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.frameMain1,fragment)
                .addToBackStack(String.valueOf(fm))
                .commit();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.nav_myCart:
                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_myWallet:
               /* fragment=new WalletFragment();
                loadFragment();
                tvTitle.setText("Wallet");*/
                //check is user login
                if (sessionManager.isLoggedIn())
                {
                    startActivity(new Intent(HomeActivity.this,WalletActivity.class));
                }else {
                    startActivity(new Intent(HomeActivity.this,LoginActivity.class));
                }
                break;
        }
/*
        if (id == R.id.nav_myCart) {
            Intent intent = new Intent(getApplicationContext(), CartActivity.class);
            startActivity(intent);
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.invalidateOptionsMenu();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void addToCart() {
        setupBadge();

    }

    @Override
    public void removeWishlist() {
        pushFragment(WishlistFragment.newInstance(), true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ivBack:
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                startActivity(intent);
                break;
            case R.id.imgPowerbyLogo:
                Uri uri = Uri.parse("https://grobiz.app/");
                Intent intentURI = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intentURI);


              /*  Intent myWebLink = new Intent(android.content.Intent.ACTION_VIEW);
                myWebLink.setData(Uri.parse("https://grobiz.app/"));
                startActivity(myWebLink);*/
                break;
        }
    }

    private void showMainCategory(){
        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        String url = getResources().getString(R.string.BASE_URL) + show_category_url;

        mVolleyService.postDataVolley(SHOW_MAIN_CATEGORY, url);

    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {
                    case SHOW_MAIN_CATEGORY:
                        try {
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            Log.d("main category",response.toString());

                            if (status == 0) {
                                Log.d("show latest error",jsonObj.getString("error"));
                                //  toastClass.makeToast(getActivity().getApplicationContext(), "Error loading sell property");
                                setupNavigrionDrawer(0);
                                populatNavMenuData();

                            }
                            else if (status == 1) {
                                // toastClass.makeToast(getActivity().getApplicationContext(), "successful sell property");
                                Gson gson = new Gson();
                                categoryBaseModel = gson.fromJson(
                                        response, CategoryBaseModel.class);

                                categoryModelList=categoryBaseModel.getAllcategories();
                                setupNavigrionDrawer(1);
                                populatNavMenuData();
                            }
                        }
                        catch (Exception e) {
                            Log.v("Show main category", e.toString());
                            setupNavigrionDrawer(0);
                            populatNavMenuData();

                        }
                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                setupNavigrionDrawer(0);
                populatNavMenuData();
                //  Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private void setupNavigrionDrawer(int status) {



        setTitleColor(R.color.colorWhite);

        MenuModel headModel = new MenuModel("Home",null ,R.drawable.ic_home_red_24dp, true, false);
        listTitles.add(headModel);

        if (!headModel.isChild()) {
            listDataChild.put(headModel, null);
        }

        if (status==1){
            headModel = new MenuModel("Categories",null, R.drawable.brake, true, true);
            listTitles.add(headModel);
            List<MenuModel> childModelList = new ArrayList<>();

            for (int i=0;i<categoryModelList.size(); i++){
                MenuModel childModel = new MenuModel(categoryModelList.get(i).getMainCategoryName(),null, 0, true, true);
                childModelList.add(childModel);

                List<SubCategoryModel> subCategoryModelList=categoryModelList.get(i).getSubcategory();

                List<MenuModel> subChildModelList=new ArrayList<>();

                for (int j=0;j<subCategoryModelList.size();j++){
                    MenuModel subchildModel = new MenuModel(subCategoryModelList.get(j).getSubcategoryname(),subCategoryModelList.get(j).getId(), 0, false, false);
                    subChildModelList.add(subchildModel);
                }

                if (childModel.isChild()){
                    listDataSubChild.put(childModel,subChildModelList);
                }
            }

            if (headModel.isChild()) {
                listDataChild.put(headModel, childModelList);
            }
        }


        if (list.contains("Wishlist")) {
            headModel = new MenuModel("Wishlist", null,R.drawable.ic_favorite_border_red_24dp, true, false);
            listTitles.add(headModel);

            if (!headModel.isChild()) {

                listDataChild.put(headModel, null);
                drawer.closeDrawer(GravityCompat.START);
            }
        }

        if (sessionManager.isLoggedIn()){

            if (list.contains("Refer & Earn")) {
                headModel = new MenuModel("Refer & Earn", null,R.drawable.ic_favorite_border_red_24dp, true, false);
                listTitles.add(headModel);

                if (!headModel.isChild()) {

                    listDataChild.put(headModel, null);
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        }



       /* headModel = new MenuModel("Refer & Earn", null,R.drawable.ic_share_black_24dp, true, false);
        listTitles.add(headModel);

        if (!headModel.isChild()) {

            listDataChild.put(headModel, null);
            drawer.closeDrawer(GravityCompat.START);
        }
*/



        if (sessionManager.isLoggedIn()){


            if (list.contains("Order History")) {
                headModel = new MenuModel("My Orders",null, R.drawable.ic_history_black_24dp, true, false);
                listTitles.add(headModel);

                if (!headModel.isChild()) {

                    listDataChild.put(headModel, null);

                }
            }

        }


       /* headModel = new MenuModel("Terms & Conditions", null,R.drawable.ic_terms_red_24dp, true, false);
        listTitles.add(headModel);

        if (!headModel.isChild()) {

            listDataChild.put(headModel, null);

        }
*/
        if (sessionManager.isLoggedIn()){

            headModel = new MenuModel("Account", null, R.drawable.ic_account_circle_black_24dp, true, false);
            listTitles.add(headModel);

            if (!headModel.isChild()) {

                listDataChild.put(headModel, null);

            }
        }

        if (sessionManager.isLoggedIn()){
            headModel = new MenuModel("Logout", null,R.drawable.ic_lock_outline_red_24dp, true, false);
            listTitles.add(headModel);

            if (!headModel.isChild()) {
                listDataChild.put(headModel, null);

            }
        }
        else {
            headModel = new MenuModel("Login", null,R.drawable.ic_lock_outline_red_24dp, true, false);
            listTitles.add(headModel);

            if (!headModel.isChild()) {
                listDataChild.put(headModel, null);

            }

        }
        expandableListAdapter = new CategoryExpandableListAdapter(this, listTitles, listDataChild);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

    }

    private void populatNavMenuData(){
        subChildAdapter=new SubChildAdapter(this,listTitles,listDataChild,listDataSubChild);
        subChildAdapter.notifyDataSetChanged();
        expandableListView.setAdapter(subChildAdapter);

        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup)
                    expandableListView.collapseGroup(previousGroup);
                previousGroup = groupPosition;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (listTitles.get(groupPosition).isGroup()){
                    if (!listTitles.get(groupPosition).isChild()){
                        if ((listTitles.get(groupPosition).getMenuName()).equals("Home"))
                        {

                            pushFragment(HomeFragment.newInstance(), true);
                            drawer.closeDrawer(GravityCompat.START);

                            //expandableListView.setGroupIndicator(R.drawable.lock);
                        }

                        else if ((listTitles.get(groupPosition).getMenuName()).equals("Login"))
                        {

                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                            //expandableListView.setGroupIndicator(R.drawable.lock);

                        }

                        else if ((listTitles.get(groupPosition).getMenuName()).equals("Logout"))
                        {
                            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                            builder.setTitle("Logout");
// builder.setIcon(R.drawable.icon_exit);
                            builder.setMessage("Do you want to logout?")
                                    .setCancelable(true)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //clear cart
                                            sessionManager.deleteCartList();
                                            sessionManager.deleteWishList();
                                            sessionManager.logoutUser();
                                            sessionManager.onSetToken("");
                                            finish();
                                           /* Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);*/
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                            //expandableListView.setGroupIndicator(R.drawable.lock);

                        }

                        else if ((listTitles.get(groupPosition).getMenuName()).equals("Account"))
                        {

                            startActivity(new Intent(HomeActivity.this, AccountActivity.class));

                        }

                        else if ((listTitles.get(groupPosition).getMenuName()).equals("Wishlist"))
                        {

                            pushFragment(WishlistFragment.newInstance(), true);
                            drawer.closeDrawer(GravityCompat.START);

                            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            getSupportActionBar().setDisplayShowHomeEnabled(false);
                            tvTitle.setText("Wishlist");


                            ivBack.setVisibility(View.VISIBLE);
                            ivBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);

                                }
                            });

                        }

                        else if ((listTitles.get(groupPosition).getMenuName()).equals("My Orders"))
                        {

                            //  pushFragment(OrdersFragment.newInstance(), true);
                            drawer.closeDrawer(GravityCompat.START);

                            // getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                            //getSupportActionBar().setDisplayShowHomeEnabled(false);
                            //tvTitle.setText("My Orders");

                            // ivWallet.setVisibility(View.GONE);
                            // ivBack.setVisibility(View.VISIBLE);

                           /* ivBack.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    startActivity(intent);

                                }
                            });*/
                            startActivity(new Intent(HomeActivity.this,OrdersActivity.class));

                        }

                        else if ((listTitles.get(groupPosition).getMenuName()).equals("Refer & Earn"))
                        {
                            drawer.closeDrawer(GravityCompat.START);
                            startActivity(new Intent(HomeActivity.this,ReferAndEarnActivity.class));

                        }

                        else if ((listTitles.get(groupPosition).getMenuName()).equals("Terms & Conditions"))
                        {
                            startActivity(new Intent(HomeActivity.this, TermsActivity.class));


                        }
                    }
                }
                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (listDataChild.get(listTitles.get(groupPosition))!=null){
                    //    else if ((childList.get(headList.get(groupPosition)).get(childPosition).getMenuName()).equals("Western Wear")){
//                        startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
//                    }
//                    else if ((childList.get(headList.get(groupPosition)).get(childPosition).getMenuName()).equals("Top Wear")){
//                        startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
//                    }
//                    else if ((childList.get(headList.get(groupPosition)).get(childPosition).getMenuName()).equals("Bottom Wear")){
//                        startActivity(new Intent(HomeActivity.this,ProfileActivity.class));
//                    }
                }
                return false;
            }
        });

    }


    @Override
    public void removeCart() {
        setupBadge();
    }

}


