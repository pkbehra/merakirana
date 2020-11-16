package com.example.merakirana;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.Adapter.TransactionAdapter;
import com.example.Interface.IResult;
import com.example.Model.TransactionBaseModel;
import com.example.Model.TransactionsModel;
import com.example.Utility.InternetConnection;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.service.BlockStatusService;
import com.google.gson.Gson;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Utility.ConstantVariables.SHOW_ALL_TRANSACTIONS;
import static com.example.Utility.ConstantVariables.SHOW_WALLET_DETAILS;
import static com.example.Utility.ConstantVariables.TRANSFER_WALLET_AMOUNT;
import static com.example.Utility.SessionManager.KEY_ID;

public class WalletActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cvWalletBalance, cvPbWallet;
    private TextView tvWalletBalance;
    private Button btnTransfer;
    private LinearLayout llNoTransaction, llTransactionHistory;
    private RelativeLayout rlMainWallet;
    private ProgressBar pbWalletTransfer;
    private SessionManager sessionManager;
    private EditText edtMobileNo, edtAmount;
    private Button btnSend;
    private AlertDialog alertDialog;
    android.app.AlertDialog alertDialog1;
    ImageView ivClose;


    private List<TransactionsModel> creditedTransactionList = new ArrayList<>();
    private List<TransactionsModel> debitedTransactionList = new ArrayList<>();
    private ArrayList<TransactionsModel> transactionsModelArrayList = new ArrayList<>();

    private TransactionAdapter mTransactionAdapter;
    private RecyclerView rvTransactionHistory;

    private String sid;
    private Calendar calendar;
    private SimpleDateFormat df;
    private String formattedDate;

    private IResult mResultCallback;
    private VolleyService mVolleyService;

    final String show_wallet_url = "Wallet";
    final String tranfer_amount_url = "Transfer-Amount";
    final String show_transaction_url = "Show-Wallet-Tranzaction-History";

    String wallet_amount = "0";

    ProgressDialog progressDialog;
    ConstraintLayout noInternet;
    CardView retryBtn;
    Toolbar toolbar;


    CountryCodePicker ccp;
    String countrycode = "";

    TextView txtNoWalletHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_wallet);

        setupToolbar();
        init();
        getData();
        HashMap<String, String> userAccount = sessionManager.getUserDetails();
        sid = userAccount.get(KEY_ID);
      /*  getUserWallet();
        getTransactionHistory();
*/
        btnTransfer.setOnClickListener(this);
    }

    private void init() {

        noInternet = findViewById(R.id.noInternetCard);
        retryBtn = findViewById(R.id.retryBtn);

        retryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        // toastClass = new ToastClass();
        sessionManager = new SessionManager(this);

        txtNoWalletHistory = findViewById(R.id.txtNoWalletHistory);
        cvWalletBalance = findViewById(R.id.cvWalletBalance);
        tvWalletBalance = findViewById(R.id.tvWalletBalance);
        btnTransfer = findViewById(R.id.btnTransfer);
        rvTransactionHistory = findViewById(R.id.rvTransaction);
        llNoTransaction = findViewById(R.id.llNoTransaction);
        llTransactionHistory = findViewById(R.id.llTransactionHistory);

        rlMainWallet = findViewById(R.id.rlMainWallet);
        cvPbWallet = findViewById(R.id.cvPbWallet);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvTransactionHistory.setLayoutManager(linearLayoutManager);
        rvTransactionHistory.setItemAnimator(new DefaultItemAnimator());

        //check is user purchased wallet transfer
        String newfeaturesList = sessionManager.getFeaturesArray();
        String[] featureslist = newfeaturesList.split(",");
        List<String> list = Arrays.asList(featureslist);

        if (list.contains("Wallet Transfer")) {
            btnTransfer.setVisibility(View.VISIBLE);
        } else {
            btnTransfer.setVisibility(View.GONE);
        }


    }

    private void setupToolbar() {
        toolbar = findViewById(R.id.toolbarMain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Wallet");
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

    private void getData() {
        if (InternetConnection.isConnectedToNetwork(this)) {
            progressDialog = ProgressDialog.show(this, "Please Wait", null, false, true);
            getWallet();

            String newfeaturesList = sessionManager.getFeaturesArray();
            String[] featureslist = newfeaturesList.split(",");
            List<String> list = Arrays.asList(featureslist);

            if (list.contains("Transaction History")) {
                llTransactionHistory.setVisibility(View.VISIBLE);
                getTransctions();
            } else {
                llTransactionHistory.setVisibility(View.GONE);
            }

        } else {
            noInternet.setVisibility(View.VISIBLE);
        }
    }

    private void getWallet() {
        initVolleyCallback();

        HashMap<String, String> user = sessionManager.getUserDetails();
        String id = user.get(KEY_ID);

        mVolleyService = new VolleyService(mResultCallback, this);

        Map<String, String> params = new HashMap<>();
        params.put("customer_auto_id", id);

        String url = getResources().getString(R.string.BASE_URL) + show_wallet_url;

        mVolleyService.postDataVolleyParameters(SHOW_WALLET_DETAILS, url, params);
    }

    private void getTransctions() {
        initVolleyCallback();

        HashMap<String, String> user = sessionManager.getUserDetails();
        String id = user.get(KEY_ID);

        mVolleyService = new VolleyService(mResultCallback, this);

        Map<String, String> params = new HashMap<>();
        params.put("customer_auto_id", id);

        String url = getResources().getString(R.string.BASE_URL) + show_transaction_url;

        mVolleyService.postDataVolleyParameters(SHOW_ALL_TRANSACTIONS, url, params);
    }

    private void transferAmount() {
        if (checkValid()) {
            progressDialog = ProgressDialog.show(this, "Please Wait", null, false, true);

            initVolleyCallback();

            HashMap<String, String> user = sessionManager.getUserDetails();
            String id = user.get(KEY_ID);

            mVolleyService = new VolleyService(mResultCallback, this);

            Map<String, String> params = new HashMap<>();
            params.put("customer_auto_id", id);
            params.put("amount", edtAmount.getText().toString().trim());
            params.put("contact", edtMobileNo.getText().toString().trim());
            params.put("country_code", countrycode);//"5d2577d462c29"

            String url = this.getResources().getString(R.string.BASE_URL) + tranfer_amount_url;

            mVolleyService.postDataVolleyParameters(TRANSFER_WALLET_AMOUNT, url, params);
        }
    }

    private void showTransferDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.transfer_dialog, null);
        dialogBuilder.setView(dialogView);

        edtMobileNo = dialogView.findViewById(R.id.edtMobileNo);
        edtAmount = dialogView.findViewById(R.id.edtAmount);
        btnSend = dialogView.findViewById(R.id.btnSend);
        ccp = (CountryCodePicker) dialogView.findViewById(R.id.ccpwallet);

        pbWalletTransfer = dialogView.findViewById(R.id.pbWalletTransfer);
        ivClose = (ImageView) dialogView.findViewById(R.id.ivClose);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        ccp = (CountryCodePicker) dialogView.findViewById(R.id.ccpwallet);

        countrycode = ccp.getDefaultCountryNameCode() + "-" + ccp.getDefaultCountryCode();

        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                countrycode = ccp.getSelectedCountryNameCode() + "-" + ccp.getSelectedCountryCode();

            }

        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValid()) {
                    // pbWalletTransfer.setVisibility(View.VISIBLE);

                    if (wallet_amount.equals("0")) {
                        Toast.makeText(WalletActivity.this, "Your current balance is not sufficient to transfer", Toast.LENGTH_SHORT).show();
                    } else {
                        btnSend.setVisibility(View.GONE);
                        transferAmount();
                    }
                    // pbWalletTransfer.setVisibility(View.VISIBLE);
                    //alertDialog.dismiss();
                }
            }
        });

        alertDialog = dialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private boolean checkValid() {

        if (edtMobileNo.getText().toString().equalsIgnoreCase("")) {
            edtMobileNo.setError("Please enter contact");
            return false;
        } else if (edtAmount.getText().toString().equalsIgnoreCase("")) {
            edtAmount.setError("Please enter amount");
            return false;
        }

        return true;
    }

    private void showDialog(String message) {
        alertDialog1 = new android.app.AlertDialog.Builder(this)
                .setTitle("Wallet")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog1.dismiss();
                    }
                }).show();
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {
                    case SHOW_WALLET_DETAILS:
                        try {
                            progressDialog.dismiss();
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            Log.v("Show wallet", response.toString());
                            //   Toast.makeText(getActivity().getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                            if (status == 0) {
                                Log.d("show wallet error", jsonObj.getString("error"));
                            } else if (status == 1) {
                                // toastClass.makeToast(getActivity().getApplicationContext(), "successful sell property");
                                JSONArray walletArray = jsonObj.getJSONArray("wallet");

                                JSONObject walletData = walletArray.getJSONObject(0);
                                wallet_amount = walletData.getString("amount");

                                if(wallet_amount.equals("0"))
                                {
                                    tvWalletBalance.setText(wallet_amount);
                                }else {
                                    tvWalletBalance.setText("$ " + wallet_amount);
                                }

                            }
                        } catch (Exception e) {
                            Log.v("Show wallet", e.toString());
                        }
                        progressDialog.dismiss();
                        break;

                    case SHOW_ALL_TRANSACTIONS:
                        try {
                            progressDialog.dismiss();
                            transactionsModelArrayList.clear();
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            Log.v("show transactions", response.toString());
                            //   Toast.makeText(getActivity().getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                            if (status == 0) {
                                Log.d("show transaction error", jsonObj.getString("error"));
                            } else if (status == 1) {
                                Gson gson = new Gson();
                                TransactionBaseModel transactionBaseModel = gson.fromJson(
                                        response, TransactionBaseModel.class);

                                creditedTransactionList = transactionBaseModel.getCreditedHistory();
                                debitedTransactionList = transactionBaseModel.getDebittedHistory();

                                for (int i = 0; i < creditedTransactionList.size(); i++) {
                                    TransactionsModel transactionsModel = creditedTransactionList.get(i);
                                    transactionsModel.setTrans_type("Credit");
                                    transactionsModelArrayList.add(transactionsModel);
                                }

                                for (int i = 0; i < debitedTransactionList.size(); i++) {
                                    TransactionsModel transactionsModel = debitedTransactionList.get(i);
                                    transactionsModel.setTrans_type("Debit");
                                    transactionsModelArrayList.add(transactionsModel);
                                }

                                mTransactionAdapter = new TransactionAdapter(WalletActivity.this, transactionsModelArrayList);
                                rvTransactionHistory.setAdapter(mTransactionAdapter);
                                mTransactionAdapter.notifyDataSetChanged();


                            }
                            if (transactionsModelArrayList.isEmpty()) {

                                txtNoWalletHistory.setVisibility(View.VISIBLE);

                            }


                        } catch (Exception e) {
                            Log.v("Show transaction", e.toString());

                        }
                        progressDialog.dismiss();
                        break;

                    case TRANSFER_WALLET_AMOUNT:
                        try {
                            progressDialog.dismiss();
                            jsonObj = new JSONObject(response);
                            int status = jsonObj.getInt("status");

                            Log.v("transfer amount", response.toString());
                            //  Toast.makeText(getActivity().getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();

                            if (status == 1) {
                                showDialog(jsonObj.getString("msg"));
                                btnSend.setVisibility(View.VISIBLE);
                                alertDialog.dismiss();
                                getData();
                                getTransctions();

                            } else {
                                showDialog(jsonObj.getString("msg"));
                                btnSend.setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            Log.v("transfer wallet", e.toString());
                        }
                        progressDialog.dismiss();
                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                progressDialog.dismiss();
                showDialog("Something went wrong. Please try again !!!");
                //  Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnTransfer:
                if (wallet_amount.equals("0"))
                {
                    Toast.makeText(this, "You don't have sufficient balance.", Toast.LENGTH_SHORT).show();
                }else {
                    showTransferDialog();
                }
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        //to check block status register broadcast
        Intent background = new Intent(WalletActivity.this, BlockStatusService.class);
        startService(background);
        registerReceiver(broadcastReceiver, new IntentFilter("efunhub.com.automobile.closeactivity"));
    }

    @Override
    public void onPause() {
        super.onPause();

        //to check block status Unregister broadcast
        Intent background = new Intent(WalletActivity.this, BlockStatusService.class);
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


}
