package com.example.merakirana;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.Adapter.ProdFeaturesAdapter;
import com.example.Adapter.ProdImageSliderAdapter;
import com.example.Adapter.ProdSpecificationAdapter;
import com.example.Interface.AddToCartListener;
import com.example.Interface.IResult;
import com.example.Model.AllProductFeatureModel;
import com.example.Model.AllProductGalleryModel;
import com.example.Model.AllProductSpecificationModel;
import com.example.Model.CartRVModel;
import com.example.Model.ProductDetailBaseModel;
import com.example.Model.WishlistRVModel;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.service.BlockStatusService;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Utility.ConstantVariables.SHOW_PRODUCT_DETAILS;

public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener, AddToCartListener {

    Toolbar toolbar;
    MenuItem cartItem;
    Menu mMenu;
    SessionManager sessionManager;
    TextView tvTitle, textCartItemCount;
    ImageView cartImage;
    ImageView ivWallet, ivBack, ivIcon,productImage;
    Button btnAddTocart,btnWishlist;
    RelativeLayout rlImageViewPager;
    ImageView ivback;

    String mProductId="";

    final String show_prod_details_url="Show-Product-Details";
    private IResult mResultCallback;
    private VolleyService mVolleyService;

    ProgressDialog progressDialog;
    LinearLayout llProductFeatures,llProductSpecifications;
    RelativeLayout rlProductImage;

    ProductDetailBaseModel productDetailBaseModel;
    List<AllProductGalleryModel> productGalleryModelList=new ArrayList<>();
    List<AllProductFeatureModel> productFeatureModelList=new ArrayList<>();
    List<AllProductSpecificationModel> productSpecificationModelList=new ArrayList<>();

    private static ViewPager mPager;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    private static int NUM_PAGES = 0;

    RecyclerView featureRecycler;
    ProdFeaturesAdapter prodFeaturesAdapter;

    RecyclerView specificationrecycler;
    ProdSpecificationAdapter specificationAdapter;

    TextView name,price;
    private String mProductName,mProductPrice,mProductQty,mProductUnit,mProductDescription;
    TextView prodName,prodDescription,prodPrice,tvNoDataFeatures,tvNoDataSpecification;
    private String mProductAutoId="";
    private String mProductLogo="";

    String newfeaturesList;
    String[] featureslist;
    List<String> listfeature;

    ImageView imgWishlist;
    AddToCartListener addToCartListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        setupToolbar();
        getData();
    }

    private void getData() {
        getProdDetails();


    }

    private void getProdDetails() {
        progressDialog = ProgressDialog.show(this, "Please Wait", null, false, true);

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback, this);

        String url = getResources().getString(R.string.BASE_URL) + show_prod_details_url;

        Map<String, String> params = new HashMap<>();
        params.put("product_auto_id",mProductAutoId);

        Log.d("params",params.toString());

        mVolleyService.postDataVolleyParameters(SHOW_PRODUCT_DETAILS, url,params);


    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                progressDialog.dismiss();

                JSONObject jsonObj = null;

                switch (requestId) {
                    case SHOW_PRODUCT_DETAILS:
                        try {
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            Log.d("product details",response.toString());

                            if (status == 0) {
                                Log.d("show details error",jsonObj.getString("error"));
                                Toast.makeText(ProductDetailsActivity.this,jsonObj.getString("error"),Toast.LENGTH_SHORT).show();
                            }
                            else if (status == 1) {
                                // toastClass.makeToast(getActivity().getApplicationContext(), "successful sell property");
                                Gson gson = new Gson();
                                productDetailBaseModel = gson.fromJson(
                                        response, ProductDetailBaseModel.class);

                                productGalleryModelList=productDetailBaseModel.getAllproductGallery();
                                productSpecificationModelList=productDetailBaseModel.getAllproductSpecifications();
                                productFeatureModelList=productDetailBaseModel.getAllproductFeatures();

                                setDetailsData();
                            }
                        }
                        catch (Exception e) {
                            progressDialog.dismiss();

                            Log.v("Show product details", e.toString());
                        }
                        break; }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(ProductDetailsActivity.this,error.toString(),Toast.LENGTH_SHORT).show();

                //  Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private void setDetailsData() {
        setGallery();
        setFeatures();
        setSpecification();
    }

    private void setFeatures() {
        featureRecycler=findViewById(R.id.featureRecycler);
        featureRecycler.setHasFixedSize(true);
        featureRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        featureRecycler.setItemAnimator(new DefaultItemAnimator());

        if(productFeatureModelList.isEmpty())
        {
            llProductFeatures.setVisibility(View.GONE);
        }else {
            llProductFeatures.setVisibility(View.VISIBLE);
            prodFeaturesAdapter=new ProdFeaturesAdapter(productFeatureModelList,this);
            featureRecycler.setAdapter(prodFeaturesAdapter);
            prodFeaturesAdapter.notifyDataSetChanged();
        }
    }

    private void setSpecification() {
        specificationrecycler=findViewById(R.id.specificationRecycler);
        specificationrecycler.setHasFixedSize(true);
        specificationrecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        specificationrecycler.setItemAnimator(new DefaultItemAnimator());


        if(productSpecificationModelList.isEmpty())
        {
            llProductSpecifications.setVisibility(View.GONE);
        }else {
            llProductSpecifications.setVisibility(View.VISIBLE);

            specificationAdapter=new ProdSpecificationAdapter(productSpecificationModelList,this);
            specificationrecycler.setAdapter(specificationAdapter);
            specificationAdapter.notifyDataSetChanged();
        }
    }

    private void setGallery() {

        if(productGalleryModelList.isEmpty())
        {
            //visible single image layout
            rlProductImage.setVisibility(View.VISIBLE);
            rlImageViewPager.setVisibility(View.GONE);


        }else {
            //visible view pager for image slider
            rlProductImage.setVisibility(View.GONE);
            rlImageViewPager.setVisibility(View.VISIBLE);

            mPager=findViewById(R.id.pager);
            mPager.setAdapter(new ProdImageSliderAdapter(productGalleryModelList,this));

            indicator=findViewById(R.id.indicator);
            indicator.setViewPager(mPager);

            final float density = getResources().getDisplayMetrics().density;

            //Set circle indicator radius
            indicator.setRadius(5 * density);

            NUM_PAGES =productGalleryModelList.size();
        }

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int position) {
                currentPage=position;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }


    private void init() {

        sessionManager=new SessionManager(this);
        addToCartListener=this;

        prodName=findViewById(R.id.prodName);
        prodDescription=findViewById(R.id.prodDescription);
        prodPrice=findViewById(R.id.prodPrice);
        tvNoDataFeatures=findViewById(R.id.tvNoDataFeatures);
        tvNoDataSpecification=findViewById(R.id.tvNoDataSpecification);
        btnAddTocart=findViewById(R.id.btnAddTocart);
        imgWishlist=findViewById(R.id.imgWishlist);
        // btnWishlist=findViewById(R.id.btnWishlist);
        productImage=findViewById(R.id.productImage);
        rlImageViewPager=findViewById(R.id.rlImageViewPager);
        llProductFeatures=findViewById(R.id.llProductFeatures);
        llProductSpecifications=findViewById(R.id.llProductSpecifications);
        rlProductImage=findViewById(R.id.rlProductImage);

        btnAddTocart.setOnClickListener(this);
        imgWishlist.setOnClickListener(this);

        mProductId=getIntent().getStringExtra("prodId");
        mProductAutoId=getIntent().getStringExtra("prodAutoId");
        mProductLogo=getIntent().getStringExtra("Product_Logo");
        mProductName=getIntent().getStringExtra("Product_Name");
        mProductPrice=getIntent().getStringExtra("Product_Price");
        mProductQty=getIntent().getStringExtra("Product_Qty");
        mProductUnit=getIntent().getStringExtra("Product_Unit");
        mProductDescription=getIntent().getStringExtra("Product_Description");

        prodName.setText(mProductName);
        prodDescription.setText(mProductDescription);
        prodPrice.setText("$ "+mProductPrice);

        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listfeature = Arrays.asList(featureslist);


        if(listfeature.contains("Wishlist"))
        {
            imgWishlist.setVisibility(View.VISIBLE);
            final ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(this);

            if(wishlistProductLists!=null&&wishlistProductLists.size()!=0) {

                for (int i = 0; i < wishlistProductLists.size(); i++) {

                    if (mProductId.equalsIgnoreCase(wishlistProductLists.get(i).getProductId())) {
                        imgWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                        break;
                    }
                }
            }


        }else {
            imgWishlist.setVisibility(View.INVISIBLE);
        }

        if(listfeature.contains("Cart"))
        {
            btnAddTocart.setVisibility(View.VISIBLE);

        }else {
            btnAddTocart.setVisibility(View.INVISIBLE);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivBack:
                finish();
                break;
            case R.id.btnAddTocart:
                CartRVModel cartProduct=new CartRVModel();

                cartProduct.setPrice(mProductPrice);
                cartProduct.setLogo(mProductLogo);
                cartProduct.setName(mProductName);
                cartProduct.setId(mProductAutoId);
                cartProduct.setProductId(mProductId);
                cartProduct.setProdQty(1);
                cartProduct.setDescriptions(mProductDescription);
                sessionManager.addToCart(this,cartProduct);
                addToCartListener.addToCart();

                break;
            case R.id.imgWishlist:

                final ArrayList<WishlistRVModel> wishlistProductLists = sessionManager.getAddToWishlist(this);
                if(wishlistProductLists!=null&&wishlistProductLists.size()!=0){

                    for(int i=0;i<wishlistProductLists.size();i++){

                        if(mProductId.equalsIgnoreCase(wishlistProductLists.get(i).getProductId())) {

                            final int finalI = i;
                            new AlertDialog.Builder(this)
                                    .setTitle("Wishlist")
                                    .setMessage("Do you want to remove from Wishlist?")
                                    .setNegativeButton(android.R.string.no, null)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface arg0, int arg1) {

                                            sessionManager.removeProductFromWishlist(ProductDetailsActivity.this,wishlistProductLists.get(finalI).getProductId());
                                            imgWishlist.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                            Toast.makeText(ProductDetailsActivity.this, "Removed from Wishlist", Toast.LENGTH_SHORT).show();
                                        }
                                    }).create().show();


                        }else {

                            // CosmeticArrivalModel model = list.get(pos);
                            // MainCategoryProductsModel model = list.get(position);
                            imgWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                            WishlistRVModel wishlistProductList = new WishlistRVModel();
                            wishlistProductList.setPrice(mProductPrice);
                            wishlistProductList.setStatus("1");
                            wishlistProductList.setName(mProductName);
                            wishlistProductList.setId(mProductAutoId);
                            wishlistProductList.setProductId(mProductId);
                            wishlistProductList.setLogo(mProductLogo);
                            wishlistProductList.setDescriptions(mProductDescription);
                            // wishlistProductList.setImagelogo(model.getSinglelogo());
                            // wishlistProductList.setUnit(model.getProdQty());
                            // wishlistProductList.setWeight(model.getWight());
                            sessionManager.addToWishlist(this, wishlistProductList);
                        }

                    }


                }else{
                    //MainCategoryProductsModel model = list.get(position);
                    imgWishlist.setImageResource(R.drawable.ic_favorite_red_24dp);
                    WishlistRVModel wishlistProductList = new WishlistRVModel();
                    wishlistProductList.setPrice(mProductPrice);
                    wishlistProductList.setStatus("1");
                    wishlistProductList.setDescriptions(mProductDescription);
                    wishlistProductList.setName(mProductName);
                    wishlistProductList.setId(mProductAutoId);
                    wishlistProductList.setProductId(mProductId);
                    wishlistProductList.setLogo(mProductLogo);
                    //wishlistProductList.setImagelogo(model.getSinglelogo());
                    // wishlistProductList.setUnit(model.getProdQty());
                    // wishlistProductList.setWeight(model.getWight());
                    sessionManager.addToWishlist(this, wishlistProductList);
                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //to check block status register broadcast
        Intent background = new Intent(ProductDetailsActivity.this, BlockStatusService.class);
        startService(background);
        registerReceiver(broadcastReceiver, new IntentFilter("efunhub.com.automobile.closeactivity"));
    }

    @Override
    protected void onPause() {
        super.onPause();

        //to check block status Unregister broadcast
        Intent background = new Intent(ProductDetailsActivity.this, BlockStatusService.class);
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


    private void setupToolbar() {
        toolbar=(Toolbar) findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        tvTitle=(TextView)findViewById(R.id.tvTitle);
        tvTitle.setText("Details");

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
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id){

            case R.id.nav_myCart:
                Intent intent=new Intent(this,CartActivity.class);
                startActivity(intent);
                break;
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


    @Override
    public void addToCart() {
        setupBadge();
    }
}
