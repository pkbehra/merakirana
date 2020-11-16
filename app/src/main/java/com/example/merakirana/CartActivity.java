package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.VolleyError;
import com.example.Adapter.CartAdapter;
import com.example.Fragment.SelectPaymentModeBottomSheet;
import com.example.Fragment.ShippingDetailsBottomSheet;
import com.example.Interface.AddRemoveCartListener;
import com.example.Interface.IResult;
import com.example.Interface.RemoveCart;
import com.example.Interface.SaveAddressListener;
import com.example.Interface.SelectPaymentListener;
import com.example.Interface.UpdateItemPrice;
import com.example.Model.CartRVModel;
import com.example.Model.ModelCartDetails;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.broadcastRecivers.CheckConnectivity;
import com.example.service.BlockStatusService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Utility.ConstantVariables.GET_MIN_ORDER_AMOUNT;
import static com.example.Utility.ConstantVariables.PLACE_CUSTOMER_ORDER;
import static com.example.Utility.ConstantVariables.RETRIVE_ALL_CART_DETAILS;
import static com.example.Utility.ConstantVariables.SEND_SHIPPING_DETAILS;
import static com.example.Utility.SessionManager.KEY_ADDRESS;
import static com.example.Utility.SessionManager.KEY_AREA;
import static com.example.Utility.SessionManager.KEY_CITY;
import static com.example.Utility.SessionManager.KEY_ID;
import static com.example.Utility.SessionManager.KEY_PIN_CODE;

public class CartActivity extends AppCompatActivity implements SelectPaymentListener, SaveAddressListener,
        UpdateItemPrice, AddRemoveCartListener, RemoveCart, View.OnClickListener {

    private List<CartRVModel> cartRVModel = new ArrayList<>();
    RecyclerView recyclerViewCart;
    CartAdapter cartAdapter;
    LinearLayout llMyCart,llCartInvisible;

    RelativeLayout nestedCart;
    SessionManager sessionManager;
    CardView cvCart;
    ImageView ivEmptyCart;
    Button btnGetShipping,btnPayMode,btnplaceOrder,BtnemptyCart;
    ShippingDetailsBottomSheet shippingDetailsBottomSheet;
    SelectPaymentModeBottomSheet selectPaymentModeBottomSheet;

    ArrayList<CartRVModel> cartRVModelArrayList=new ArrayList<>();

    private IResult mResultCallback;
    private VolleyService mVolleyService;

    String place_customer_order_url="Make-Order-Customer";
    String get_min_order_value="Show-Charges";
    private String cartUrl = "Cart-Charges";
    String send_shipping_details_url="Save-Address-Customer";
    String profile_url="Profile-Customer";

    AlertDialog alertDialog;
    ProgressBar progressBar;

    private String paymentMethod;
    TextView tvAmtErr,tvWaiting;
    private static int subTotalAmt=0;
    private static int walletAmt=0;
    private static String shippingAmt;
    private static float grandTotalAmt=0;
    private static float taxprice=0;
    private static int taxpercent=0;

    Toolbar toolbar;

    TextView subTotal,shippingCharges,walletAmount,grandTotal;
    CheckConnectivity  checkConnectivity;

    TextView txtTaxes,txtTaxesPercentage;

    String address="",city="",area="",pincode="";

    private ModelCartDetails modelCartDetails;

    BottomSheetDialog mBottomSheetDialognew;

    View view;
    EditText etAddress,etCity,etArea,etPincode;
    Button btnSend;
    SaveAddressListener saveAddressListener;

    ProgressDialog progressDialog;

    String cut_balance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        init();
        setupToolbar();
        //   showCart();

    }

    private void setupToolbar() {
        toolbar =  findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Cart");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            ft.replace(R.id.offerFrame, fragment, backStateName);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }


    public void showCart(){

        cartRVModelArrayList = sessionManager.getAddToCartList(this);
        if ((cartRVModelArrayList == null) || (cartRVModelArrayList.size() == 0)) {

            nestedCart.setVisibility(View.GONE);
            llCartInvisible.setVisibility(View.VISIBLE);

        }

        else {


            nestedCart.setVisibility(View.VISIBLE);

            llCartInvisible.setVisibility(View.GONE);
            cvCart.setVisibility(View.VISIBLE);

            subTotal.setText(this.getResources().getString(R.string.currency) + " " + String.valueOf(subTotalAmt));

            shippingCharges.setText(this.getResources().getString(R.string.currency) + " " + shippingAmt);

            grandTotal.setText(this.getResources().getString(R.string.currency) + " " + modelCartDetails.getPayprice());

            txtTaxesPercentage.setText("("+modelCartDetails.getTax()+"%)");
            txtTaxes.setText(getResources().getString(R.string.currency)+" "+modelCartDetails.getTaxprice());

//walletAmount.setText(getResources().getString(R.string.currency)+walletAmt);

            cartAdapter = new CartAdapter(this, cartRVModelArrayList);

            cartAdapter.notifyDataSetChanged();

            recyclerViewCart.setAdapter(cartAdapter);

            nestedCart.setVisibility(View.VISIBLE);

            llCartInvisible.setVisibility(View.GONE);
            cvCart.setVisibility(View.VISIBLE);

        }

    }

    public void init(){

        progressBar=findViewById(R.id.progressbar);
        tvAmtErr=findViewById(R.id.tvAmountErr);
        tvWaiting=findViewById(R.id.tvWaiting);

        shippingDetailsBottomSheet=new ShippingDetailsBottomSheet();
        selectPaymentModeBottomSheet=new SelectPaymentModeBottomSheet();

        sessionManager=new SessionManager(this);
        recyclerViewCart = (RecyclerView) findViewById(R.id.rvCart);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        recyclerViewCart.setHasFixedSize(true);
        recyclerViewCart.setNestedScrollingEnabled(false);
        recyclerViewCart.setItemAnimator(new DefaultItemAnimator());

        btnGetShipping = (Button) findViewById(R.id.btnGetShipping);
        btnplaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnPayMode = (Button) findViewById(R.id.btnSelectPayment);

        nestedCart=findViewById(R.id.nestedCartView);
        llMyCart=(LinearLayout)findViewById(R.id.llmyCart);
        BtnemptyCart =(Button)findViewById(R.id.btnEmptyCart);
        ivEmptyCart=(ImageView)findViewById(R.id.ivEmptyCart);
        cvCart=(CardView)findViewById(R.id.cvCart);
        llCartInvisible=(LinearLayout)findViewById(R.id.llCartInvisible);
        // ivWallet=(ImageView)findViewById(R.id.ivWallet);

        subTotal=findViewById(R.id.tvSubTotal);
        shippingCharges=findViewById(R.id.tvDeliveryCharges);
        walletAmount=findViewById(R.id.tvWalletAmountCharges);
        grandTotal=findViewById(R.id.tvCartTotalAmount);

        txtTaxes=findViewById(R.id.txtTaxes);
        txtTaxesPercentage=findViewById(R.id.txtTaxPer);

        btnGetShipping.setOnClickListener(this);
        BtnemptyCart.setOnClickListener(this);
        btnplaceOrder.setOnClickListener(this);
        btnPayMode.setOnClickListener(this);

        subTotalAmt=sessionManager.getSubTotal(this);

        //check login
        if (sessionManager.isLoggedIn()){
            btnGetShipping.setVisibility(View.VISIBLE);
            getAllCartDetails();
        }
        else {
            btnGetShipping.setVisibility(View.GONE);
            showAppLoginAlert();
        }


       /* ivWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pushFragment(WalletFragment.newInstance(),true);
            }
        });*/

        checkConnectivity=new CheckConnectivity();

    }

    private void updateCartTotal(int subtotal,int wallet,String delivery,float grandtotal,float taxPrice,int taxPercent){
        subTotal.setText("$ "+String.valueOf(subtotal));
        walletAmount.setText("$ "+String.valueOf(wallet));
        shippingCharges.setText("$ "+String.valueOf(delivery));
        grandTotal.setText("$ "+String.valueOf(grandtotal));
        txtTaxesPercentage.setText("("+taxPercent+"%"+")");
        txtTaxes.setText("$"+ " " +taxPrice);
        taxprice = Float.valueOf(taxPrice);
        taxpercent = taxPercent;
        grandTotalAmt = grandtotal;
        subTotalAmt=subtotal;

    }

    @Override
    public void removeCart() {
        showCart();
    }

    @Override
    public void addRemoveCart() {
        // subTotalAmt=sessionManager.getSubTotal(this);
        Integer shipping = Integer.valueOf(shippingAmt);
        grandTotalAmt=(subTotalAmt+shipping)-walletAmt;
        updateCartTotal(subTotalAmt,walletAmt,shippingAmt,grandTotalAmt,taxprice,taxpercent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGetShipping:
                if (sessionManager.isLoggedIn()){
                    bottomSheet();
                }
                else {
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    intent.putExtra("action","action_cart");
                    startActivity(intent);
                    finish();
                }
                break;

            case R.id.btnPlaceOrder:
                placeCustomerOrder();
                break;

            case R.id.btnSelectPayment:
                selectPaymentModeBottomSheet.show(getSupportFragmentManager(),selectPaymentModeBottomSheet.getTag());
                break;

            case R.id.btnEmptyCart:
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                break;

        }
    }

    private void bottomSheet() {
        mBottomSheetDialognew = new BottomSheetDialog(CartActivity.this);
        View sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_address, null);
        mBottomSheetDialognew.setContentView(sheetView);

        saveAddressListener= this;
        sessionManager=new SessionManager(this);
        progressBar=sheetView.findViewById(R.id.progressbar);
        etAddress=sheetView.findViewById(R.id.edtPayemntAddress);
        etCity=sheetView.findViewById(R.id.edtCity);
        etArea=sheetView.findViewById(R.id.edtArea);
        etPincode=sheetView.findViewById(R.id.edtPayemntPincode);
        btnSend=sheetView.findViewById(R.id.btnSaveAddress);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()){
                    city=etCity.getText().toString();
                    area=etArea.getText().toString();
                    address=etAddress.getText().toString();
                    pincode=etPincode.getText().toString();

                    sessionManager.saveDeliveryAddress(address,city,area,pincode);
                    saveShippingDetails(etCity.getText().toString(), etArea.getText().toString(), etAddress.getText().toString(), Integer.parseInt(etPincode.getText().toString()));

                    sendShippingDetails();

                }
            }
        });



        etCity = (EditText) sheetView.findViewById(R.id.edtCity);
        etArea = (EditText) sheetView.findViewById(R.id.edtArea);
        etPincode = (EditText) sheetView.findViewById(R.id.edtPayemntPincode);
        etAddress = (EditText) sheetView.findViewById(R.id.edtPayemntAddress);
        btnSend = (Button) sheetView.findViewById(R.id.btnSaveAddress);

        if (sessionManager.isAddressSaved()){
            HashMap<String,String> userAddress=sessionManager.getAddressDetails();
            city=userAddress.get(KEY_CITY);
            area=userAddress.get(KEY_AREA);
            address=userAddress.get(KEY_ADDRESS);
            pincode=userAddress.get(KEY_PIN_CODE);

            etAddress.setText(address);
            etCity.setText(city);
            etArea.setText(address);
            etPincode.setText(pincode);


        }
        // loadProfile();


        mBottomSheetDialognew.show();


    }

    private boolean isValid() {

        String namepattern= "^[a-zA-Z -]+$";

        if (etAddress.getText().toString().equals("")){
            etAddress.setError("Please enter shipping address");
            return false;
        }
        else if (etCity.getText().toString().equals("")){
            etCity.setError("Please enter city");
            return false;
        }
        else if (!etCity.getText().toString().matches(namepattern)){
            etCity.setError("Please enter city");
            return false;
        }
        else if (etArea.getText().toString().equals("")){
            etArea.setError("Please enter area");
            return false;
        }
        else if (etPincode.getText().toString().equals("")){
            etPincode.setError("Please enter Zip code");
            return false;
        }
        return true;
    }

    private void sendShippingDetails() {

        btnSend.setClickable(false);
        //progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, false, true);
        progressBar.setVisibility(View.VISIBLE);


        HashMap<String,String> user=sessionManager.getUserDetails();
        String id=user.get(KEY_ID);


        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback,getApplicationContext());

        Map<String, String> params = new HashMap<>();
        params.put("pincode",etPincode.getText().toString());
        Log.d("customer_auto_id",id);
        params.put("customer_auto_id",id);
        params.put("city",etCity.getText().toString() );
        params.put("area",etArea.getText().toString() );
        params.put("address",etAddress.getText().toString() );
        params.put("cart_total",String.valueOf(grandTotalAmt));

        String url =getResources().getString(R.string.BASE_URL) + send_shipping_details_url;

        mVolleyService.postDataVolleyParameters(SEND_SHIPPING_DETAILS, url, params);

    }

    private void getAllCartDetails() {

        progressBar.setVisibility(View.VISIBLE);

        HashMap<String,String> user=sessionManager.getUserDetails();
        String id=user.get(KEY_ID);


        if (sessionManager.isAddressSaved()){
            HashMap<String,String> userAddress=sessionManager.getAddressDetails();
            pincode=userAddress.get(KEY_PIN_CODE);
        }

        initVolleyCallback();

        HashMap<String, String> params = new HashMap<>();

        params.put("customer_auto_id", id);
        params.put("cart_total", String.valueOf(subTotalAmt));
        params.put("pincode",pincode);

        mVolleyService = new VolleyService(mResultCallback, this);
        mVolleyService.postDataVolleyParameters(RETRIVE_ALL_CART_DETAILS,
                this.getResources().getString(R.string.BASE_URL) + cartUrl, params);
    }

    private void getMinOrderAmount(){
        progressBar.setVisibility(View.VISIBLE);
        tvWaiting.setVisibility(View.VISIBLE);

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback,this);

        String url =getResources().getString(R.string.BASE_URL) + get_min_order_value;

        mVolleyService.postDataVolley(GET_MIN_ORDER_AMOUNT, url);

    }

    private void placeCustomerOrder() {
        //progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, false, true);
        progressBar.setVisibility(View.VISIBLE);
        tvWaiting.setVisibility(View.VISIBLE);
        HashMap<String,String> user=sessionManager.getUserDetails();
        String id=user.get(KEY_ID);

        initVolleyCallback();

        mVolleyService = new VolleyService(mResultCallback,this);

        String prod_auto_id="",prod_id="",prod_name="",prod_price="",prod_logo="",prod_quantity="";

        for (int k=0;k<cartRVModelArrayList.size();k++){
            if (k==0){
                prod_auto_id=prod_auto_id+cartRVModelArrayList.get(k).getId();
                prod_id=prod_id+cartRVModelArrayList.get(k).getProductId();
                prod_name=prod_name+cartRVModelArrayList.get(k).getName();
                prod_price=prod_price+cartRVModelArrayList.get(k).getPrice();
                prod_logo=prod_logo+cartRVModelArrayList.get(k).getLogo();
                prod_quantity=prod_quantity+cartRVModelArrayList.get(k).getProdQty();
            }
            else {
                prod_auto_id=prod_auto_id+"|"+cartRVModelArrayList.get(k).getId();
                prod_id=prod_id+"|"+cartRVModelArrayList.get(k).getProductId();
                prod_name=prod_name+"|"+cartRVModelArrayList.get(k).getName();
                prod_price=prod_price+"|"+cartRVModelArrayList.get(k).getPrice();
                prod_logo=prod_logo+"|"+cartRVModelArrayList.get(k).getLogo();
                prod_quantity=prod_quantity+"|"+cartRVModelArrayList.get(k).getProdQty();
            }
        }

        Map<String, String> params = new HashMap<>();
        params.put("customer_auto_id",id);
        params.put("product_auto_id",prod_auto_id);
        params.put("product_id",prod_id);
        params.put("product_name",prod_name);
        params.put("product_price",prod_price);
        params.put("product_logo",prod_logo);
        params.put("quantity",prod_quantity);
        params.put("sub_total",String.valueOf(subTotalAmt));
        params.put("delivery_charge",String.valueOf(shippingAmt));
        params.put("cutbalance",String.valueOf(walletAmt));
        params.put("total_price",String.valueOf(grandTotalAmt));
        params.put("tranzaction_id","");
        params.put("payment_mode",paymentMethod);

        Log.d("order params",params.toString());

        String url =getResources().getString(R.string.BASE_URL) + place_customer_order_url;

        mVolleyService.postDataVolleyParameters(PLACE_CUSTOMER_ORDER, url, params);

    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {

                    case RETRIVE_ALL_CART_DETAILS:

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 1) {

                                shippingAmt = jsonObject.getString("delivery_charge");
                                cut_balance = jsonObject.getString("cutbalance");
                                taxprice = Float.valueOf((jsonObject.getString("taxprice")));
                                taxpercent= Integer.valueOf(jsonObject.getString("tax"));
                                grandTotalAmt= Float.valueOf(jsonObject.getString("final_total"));

                                shippingCharges.setText(shippingAmt);

                                //    String text = String.format("%.2f", cut_balance);

                                walletAmount.setText(getResources().getString(R.string.currency)+ " " + cut_balance);


                                Gson gson = new Gson();
                                modelCartDetails = gson.fromJson(
                                        response, ModelCartDetails.class);
                                showCart();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                        break;
                    case PLACE_CUSTOMER_ORDER:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("place order",response.toString());

                            int status = jsonObj.getInt("status");
                            if (status == 1) {
                                grandTotalAmt=0;
                                subTotalAmt=0;
                                walletAmt=0;
                                sessionManager.deleteCartList();
                                showCart();
                                Toast.makeText(getApplicationContext(), "Order has been placed successfully", Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(getApplicationContext(), OrdersActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                            }
                        }
                        catch (Exception e) {
                            Log.v("Place order", e.toString());
                        }
                        progressBar.setVisibility(View.GONE);
                        tvWaiting.setVisibility(View.GONE);
                        // progressDialog.dismiss();
                        break;

                    case GET_MIN_ORDER_AMOUNT:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("get min order value",response.toString());

                            int status = jsonObj.getInt("status");
                            if (status == 1) {
                                JSONArray chargesArray=jsonObj.getJSONArray("allcharges");
                                JSONObject chargesObj=chargesArray.getJSONObject(0);

                                String minAmt=chargesObj.getString("min_order_amount");
                                taxprice= Float.valueOf(Integer.parseInt(chargesObj.getString("tax")));

                                int charges=Integer.parseInt(minAmt.trim());

                                if (charges<=subTotalAmt){
                                    Bundle bundle=new Bundle();
                                    bundle.putInt("totalCost", (int) grandTotalAmt);
                                    shippingDetailsBottomSheet.setArguments(bundle);
                                    shippingDetailsBottomSheet.show(getSupportFragmentManager(),shippingDetailsBottomSheet.getTag());
                                }
                                else {
                                    tvAmtErr.setText("Minimum order amount must be atleast $"+minAmt);
                                    tvAmtErr.setVisibility(View.VISIBLE);
                                }
                                //Toast.makeText(getApplicationContext(), "Order placed successfully", Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception e) {
                            Log.v("get min order value", e.toString());
                        }
                        progressBar.setVisibility(View.GONE);
                        tvWaiting.setVisibility(View.GONE);
                        // progressDialog.dismiss();
                        break;
                    case SEND_SHIPPING_DETAILS:
                        try {
                            jsonObj = new JSONObject(response);
                            // Log.d("change pass",response.toString());

                            int status = jsonObj.getInt("status");
                            if (status == 1) {

                                walletAmt = Integer.parseInt(String.valueOf(jsonObj.getInt("cutbalance")));
                                shippingAmt  = (jsonObj.getString("delivery_charge"));
                                grandTotalAmt = Integer.parseInt(String.valueOf(jsonObj.getInt("final_total")));
                                taxprice = Float.valueOf(Integer.parseInt(String.valueOf(jsonObj.getInt("taxprice"))));
                                taxpercent = Integer.parseInt(String.valueOf(jsonObj.getInt("tax")));

                                mBottomSheetDialognew.dismiss();

                                getAllCartDetails();
                                btnPayMode.setVisibility(View.VISIBLE);

                                saveAddressListener.saveShippingDetails(address,city,area, Integer.parseInt(pincode));
                            }
                            else {
                                showDialog(jsonObj.getString("msg"));
                            }
                        }
                        catch (Exception e) {
                            Log.v("Change Password", e.toString());
                        }
                        progressBar.setVisibility(View.GONE);
                        btnSend.setClickable(true);
                        // progressDialog.dismiss();
                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    /* @Override
     public void saveShippingDetails(int cutBalance, String deliverCharges, int finalTotal) {
         btnGetShipping.setVisibility(View.GONE);
         btnPayMode.setVisibility(View.VISIBLE);
         shippingDetailsBottomSheet.dismiss();
         subTotalAmt=sessionManager.getSubTotal(this);

         walletAmt=cutBalance;
         shippingAmt=Integer.parseInt(deliverCharges);
         grandTotalAmt=(subTotalAmt+shippingAmt)-walletAmt;
         updateCartTotal(subTotalAmt,walletAmt,shippingAmt,grandTotalAmt,taxPrice,taxPercent);

     }*/
    private void showDialog(String message) {
        alertDialog = new AlertDialog.Builder(CartActivity.this)
                .setTitle("Shipping Details")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void selectPaymentMethod(String payMode) {
        selectPaymentModeBottomSheet.dismiss();
        btnPayMode.setVisibility(View.GONE);
        btnplaceOrder.setVisibility(View.VISIBLE);
        paymentMethod=payMode;
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(checkConnectivity, intentFilter);

        //to check block status register broadcast
        Intent background = new Intent(CartActivity.this, BlockStatusService.class);
        startService(background);
        registerReceiver(broadcastReceiver, new IntentFilter("efunhub.com.automobile.closeactivity"));

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (checkConnectivity != null) {
            this.unregisterReceiver(checkConnectivity);
        }

        //to check block status Unregister broadcast
        Intent background = new Intent(CartActivity.this, BlockStatusService.class);
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


    /*  @Override
      public void saveShippingDetails(int cutBalance, String deliverCharges, int finalTotal, int tax, int taxPrice) {

          btnGetShipping.setVisibility(View.GONE);
          btnPayMode.setVisibility(View.VISIBLE);
          shippingDetailsBottomSheet.dismiss();
        //  subTotalAmt=sessionManager.getSubTotal(this);

          walletAmt=cutBalance;
          shippingAmt=Integer.parseInt(deliverCharges);
          grandTotalAmt=(subTotalAmt+shippingAmt)-walletAmt;
          updateCartTotal(subTotalAmt,walletAmt,shippingAmt,grandTotalAmt,tax,taxPrice);

          grandTotalAmt = finalTotal;

          grandTotal.setText(this.getResources().getString(R.string.currency) + " " +String.valueOf(finalTotal));
          txtTaxes.setText(this.getResources().getString(R.string.currency) + " " +String.valueOf(taxPrice));
          txtTaxesPercentage.setText(tax);


      }
  */
    @Override
    public void onItemCountChanged(int size, int price) {

        subTotalAmt = price;
        subTotal.setText(this.getResources().getString(R.string.currency) + " " + price);

        if(sessionManager.isLoggedIn())
        {
            getAllCartDetails();
        }

    }

    @Override
    public void saveShippingDetails(String address, String city, String area, int zipcode) {

    }

    private void showAppLoginAlert() {
        final android.app.AlertDialog.Builder alertbox = new AlertDialog.Builder(CartActivity.this);
        // int position = 0;
        alertbox.setMessage("You have to login/register first");
        alertbox.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        //intent.putExtra("action","action_cart");
                        startActivity(intent);
//                        finish();

                        Toast.makeText(CartActivity.this, "Login Activity", Toast.LENGTH_SHORT).show();
                    }
                });
        alertbox.setNegativeButton("Skip", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertbox.show();
    }
}
