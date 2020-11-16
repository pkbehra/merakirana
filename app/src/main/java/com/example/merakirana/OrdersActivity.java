package com.example.merakirana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.Adapter.CustomAdapter;
import com.example.Interface.CancelOrderListener;
import com.example.Interface.IResult;
import com.example.Model.ChildModel;
import com.example.Model.ParentModel;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.service.BlockStatusService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Utility.ConstantVariables.CANCEL_ORDER;
import static com.example.Utility.ConstantVariables.USER_ORDERS;
import static com.example.Utility.SessionManager.KEY_ID;

public class OrdersActivity extends AppCompatActivity implements CancelOrderListener {

    List<ParentModel> parentModels = new ArrayList<>();
    List<ChildModel> childModels;

    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    private String OrdersUrl ="Show-Order-History-Customer";
    private String CancelOrderUrl="Cancel-Order";
    private SessionManager sessionManager;
    private CustomAdapter listAdapter;
    ExpandableListView recyclerView;
    ParentModel parentModel;
    String[] namearray;
    RelativeLayout linearLayout;
    ProgressDialog progressDialog;
    private int previousGroup = -1;
    // private CheckConnectivity checkConnectivity;
    TextView nodataC;
    CancelOrderListener cancelOrderListener;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_orders);
        setupToolbar();
        init();
    }

    private void init() {

        cancelOrderListener=this;
        progressDialog = ProgressDialog.show(this, "Please Wait", null, false, true);
        sessionManager = new SessionManager(this);
        //  checkConnectivity = new CheckConnectivity();
        recyclerView = findViewById(R.id.ordersList);
        linearLayout = findViewById(R.id.mainlayoutidnew);
        nodataC = findViewById(R.id.nodatacosmetic);

        getOrders();
        expandAll();

        recyclerView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //get the group header
                ParentModel headerInfo = parentModels.get(groupPosition);
                //get the child info
                ChildModel detailInfo = headerInfo.getChildItemList().get(childPosition);

                return false;
            }
        });
        // setOnGroupClickListener listener for group heading click
        recyclerView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                //get the group header
                ParentModel headerInfo = parentModels.get(groupPosition);
                expand(v);


                return false;
            }
        });
    }

    private void setupToolbar() {
        toolbar =  findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Orders");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // finish();
                Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
    }


    private void getOrders() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, this);


        HashMap<String,String> user=sessionManager.getUserDetails();
        String id=user.get(KEY_ID);

        user.put("customer_auto_id",id);

        mVolleyService.postDataVolleyParameters(USER_ORDERS, this.getResources().getString(R.string.BASE_URL) + OrdersUrl, user);
        listAdapter = new CustomAdapter(this, parentModels, this,cancelOrderListener,recyclerView);
        recyclerView.setAdapter(listAdapter);
    }

    private void initVolleyCallback() {
        String[] namearray = null;
        mResultCallBack = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {

                if (requestId == USER_ORDERS) {
                    try {

                        parentModels.clear();
                        JSONObject jsonObj = new JSONObject(response);
                        String status = jsonObj.getString("status");
                        String orderId, orderTime, subtotal, total, deliverycharges, prodnames, prodprices, quntiry, images, qty, wt, statusnew;
                        parentModel = null;

                        if (Integer.parseInt(status) == 1) {
                            JSONArray menusJsonArray = jsonObj.getJSONArray("allorders");

                            if (menusJsonArray.length() == 0) {

                                nodataC.setVisibility(View.VISIBLE);


                                recyclerView.setVisibility(View.GONE);
                            } else {

                                nodataC.setVisibility(View.GONE);

                                recyclerView.setVisibility(View.VISIBLE);


                                for (int j = 0; j < menusJsonArray.length(); j++) {

                                    JSONObject jsonObjectMenu = menusJsonArray.getJSONObject(j);

                                    orderId = jsonObjectMenu.getString("order_id");
                                    orderTime = jsonObjectMenu.getString("order_date") + " " + jsonObjectMenu.getString("order_time");

                                    subtotal = jsonObjectMenu.getString("sub_total");
                                    total = jsonObjectMenu.getString("total_price");
                                    prodnames = jsonObjectMenu.getString("product_name");
                                    prodprices = jsonObjectMenu.getString("product_price");
                                    deliverycharges = jsonObjectMenu.getString("delivery_charge");
                                    quntiry = jsonObjectMenu.getString("quantity");
                                    //  qty = jsonObjectMenu.getString("product_qty");
                                    // wt = jsonObjectMenu.getString("product_weight");
                                    images = jsonObjectMenu.getString("product_logo");
                                    statusnew = jsonObjectMenu.getString("status");
                                    parentModel = new ParentModel();
                                    parentModel.setOrderId(orderId);
                                    parentModel.setOrder_time(orderTime);
                                    parentModel.setDelivery_charges(deliverycharges);
                                    parentModel.setSub_total(subtotal);
                                    parentModel.setTotal_price(total);
                                    parentModel.setPrdoctlist(prodnames);
                                    parentModel.setStatus(statusnew);

                                    String[] namearray = prodnames.split("\\|");
                                    String[] pricearray = prodprices.split("\\|");
                                    String[] quntityarray = quntiry.split("\\|");
                                    String[] imagearray = images.split("\\|");
                                    //  String[] qtarray = qty.split("\\|");
                                    //String[] wtarray = wt.split("\\|");
                                    childModels = new ArrayList<>();

                                    for (int k = 0; k < namearray.length; k++) {
                                        ChildModel childModel = new ChildModel();
                                        childModel.setProduct_nameenglish(namearray[k]);
                                        childModel.setProduct_price(pricearray[k]);
                                        childModel.setProduct_quanity(quntityarray[k]);
                                        // childModel.setProduct_weight(qtarray[k] + " " + wtarray[k]);
                                        String newimage = getString(R.string.PROD_IMG_URL) + imagearray[k];
                                        childModel.setProduct_image(newimage);
                                        childModels.add(childModel);
                                    }
                                    parentModel.setChildModels(childModels);
                                    parentModels.add(parentModel);
                                }
                            }
                        } else {


                            nodataC.setVisibility(View.VISIBLE);

                            recyclerView.setVisibility(View.GONE);
                        }
                    } catch (Exception e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        Log.v("Category activity", e.toString());
                    }

                    listAdapter.notifyDataSetChanged();

                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }else {
                    //Handle cancel order API response

                    try {
                        JSONObject jsonObj = null;
                        jsonObj = new JSONObject(response);
                        Log.d("Cancel order response ",response.toString());

                        int status = jsonObj.getInt("status");

                        if (status == 1) {
                            Toast.makeText(OrdersActivity.this, "Order cancelled successfully", Toast.LENGTH_SHORT).show();
                            //set notify orders adapter.call the API again
                            getOrders();
                        }
                        else {
                            Toast.makeText(OrdersActivity.this,jsonObj.getString("msg"), Toast.LENGTH_LONG).show();
                        }

                    }
                    catch (Exception e) {
                        progressDialog.dismiss();
                        Log.v("Cancel booking", e.toString());
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                Log.v("Volley requestid ", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private void expandAll() {
        int count = listAdapter.getGroupCount();
        for (int i = 0; i < count; i++) {
            recyclerView.expandGroup(i);
        }
    }

    public static void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    @Override
    public void onClickCancelOrder(String orderId) {

        showAlert(orderId);
    }

    private void cancelOrder(String orderId) {
        progressDialog = ProgressDialog.show(this, "Please Wait", null, false, true);
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, this);

        Map<String, String> params = new HashMap<>();

        params.put("order_id",orderId);

        mVolleyService.postDataVolleyParameters(CANCEL_ORDER, this.getResources().getString(R.string.BASE_URL) + CancelOrderUrl, params);
    }


    private void showAlert(final String orderId) {
        final android.app.AlertDialog.Builder alertbox = new AlertDialog.Builder(this);
        // int position = 0;
        alertbox.setMessage("Would you like to cancel your order?");
        alertbox.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        cancelOrder(orderId);
                    }
                });
        alertbox.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertbox.show();
    }

    @Override
    public void onResume() {
        super.onResume();

        //to check block status register broadcast
        Intent background = new Intent(this, BlockStatusService.class);
        startService(background);
        registerReceiver(broadcastReceiver, new IntentFilter("efunhub.com.automobile.closeactivity"));
    }

    @Override
    public void onPause() {
        super.onPause();

        //to check block status Unregister broadcast
        Intent background = new Intent(this, BlockStatusService.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
