package com.example.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.Interface.IResult;
import com.example.Interface.SaveAddressListener;
import com.example.Model.UserProfileModel;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.merakirana.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.example.Utility.ConstantVariables.SEND_SHIPPING_DETAILS;
import static com.example.Utility.ConstantVariables.USER_PROFILE;
import static com.example.Utility.SessionManager.KEY_ID;

public class ShippingDetailsBottomSheet extends BottomSheetDialogFragment {

    View view;
    EditText etAddress,etCity,etArea,etPincode;
    Button btnSend;
    SaveAddressListener saveAddressListener;

    ProgressDialog progressDialog;
    private IResult mResultCallback;
    private VolleyService mVolleyService;
    SessionManager sessionManager;

    String send_shipping_details_url="Save-Address-Customer";
    String profile_url="Profile-Customer";

    UserProfileModel userProfileModel=new UserProfileModel();

    AlertDialog alertDialog;
    ProgressBar progressBar;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.shipping_details_bottomsheet, container, false);
        init();
        showUserProfile();
        return view;
    }

    private void init() {
        saveAddressListener= (SaveAddressListener) getActivity();
        sessionManager=new SessionManager(getActivity());
        progressBar=view.findViewById(R.id.progressbar);
        etAddress=view.findViewById(R.id.edtPayemntAddress);
        etCity=view.findViewById(R.id.edtCity);
        etArea=view.findViewById(R.id.edtArea);
        etPincode=view.findViewById(R.id.edtPayemntPincode);
        btnSend=view.findViewById(R.id.btnSaveAddress);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendShippingDetails();
            }
        });

    }

    private void sendShippingDetails() {
        if (isValid()){
            btnSend.setClickable(false);
            //progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, false, true);
            progressBar.setVisibility(View.VISIBLE);
            Bundle bundle=getArguments();
            final int cartTotal=bundle.getInt("totalCost");

            HashMap<String,String> user=sessionManager.getUserDetails();
            String id=user.get(KEY_ID);


            initVolleyCallback();

            mVolleyService = new VolleyService(mResultCallback,getActivity());

            Map<String, String> params = new HashMap<>();
            params.put("pincode",etPincode.getText().toString());
            Log.d("customer_auto_id",id);
            params.put("customer_auto_id",id);
            params.put("city",etCity.getText().toString() );
            params.put("area",etArea.getText().toString() );
            params.put("address",etAddress.getText().toString() );
            params.put("cart_total",String.valueOf(cartTotal));

            String url =getResources().getString(R.string.BASE_URL) + send_shipping_details_url;

            mVolleyService.postDataVolleyParameters(SEND_SHIPPING_DETAILS, url, params);
        }
    }
    private void showUserProfile() {
        progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, false, true);

        initVolleyCallback();

        HashMap<String,String> user=sessionManager.getUserDetails();
        String id=user.get(KEY_ID);

        mVolleyService = new VolleyService(mResultCallback, getActivity());

        Map<String, String> params = new HashMap<>();
        params.put("customer_auto_id",id );

        String url = getActivity().getResources().getString(R.string.BASE_URL) + profile_url;

        Log.d("show profile",params.toString());

        mVolleyService.postDataVolleyParameters(USER_PROFILE, url, params);
    }


    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {

                    case USER_PROFILE:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("user prof",response.toString());
                            int status = jsonObj.getInt("status");
                            // String msg  = jsonObj.getString("msg");

                            if (status == 0) {
                                // showDialog(jsonObj.getString("msg"),status);
                                Toast.makeText(getActivity(), "No data available", Toast.LENGTH_SHORT).show();
                                // toastClass.makeToast(getActivity().getApplicationContext(), "Error loading user profile");
                            }
                            else if (status == 1) {

                                JSONObject profile=jsonObj.getJSONObject("profile");

                                userProfileModel.setCustomer_auto_id(profile.getString("_id"));
                                userProfileModel.setCustomerId(profile.getString("customer_id"));
                                userProfileModel.setName(profile.getString("name"));

                                userProfileModel.setEmail(profile.getString("email"));
                                userProfileModel.setCountryCode(profile.getString("country_code"));
                                userProfileModel.setContact(profile.getString("contact"));
                                userProfileModel.setPassword(profile.getString("password"));
                                userProfileModel.setCity(profile.getString("city"));
                                userProfileModel.setArea(profile.getString("area"));
                                userProfileModel.setAddress(profile.getString("address"));
                                userProfileModel.setPincode(profile.getString("pincode"));
                                userProfileModel.setStatus(profile.getString("status"));
                                userProfileModel.setRegisterDate(profile.getString("register_date"));
                                userProfileModel.setUpdatedAt(profile.getString("updated_at"));
                                userProfileModel.setCreatedAt(profile.getString("created_at"));

                                Log.d("useprofilemodel",userProfileModel.toString());

                                etAddress.setText(userProfileModel.getAddress());
                                etCity.setText(userProfileModel.getCity());
                                etArea.setText(userProfileModel.getArea());
                                etPincode.setText(userProfileModel.getPincode());

                                //country_code=userProfileModel.getCountryCode();
                                //  String[] lCountryNameCode=country_code.split("-");
                                // mCountryCodePicker.setCountryForNameCode(lCountryNameCode[0]);

                            }
                        }
                        catch (Exception e) {

                            Log.v("User Profile", e.toString());
                        }
                        progressDialog.dismiss();
                        break;


                    case SEND_SHIPPING_DETAILS:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("change pass",response.toString());

                            int status = jsonObj.getInt("status");
                            if (status == 1) {
                                int cutBalance = jsonObj.getInt("cutbalance");
                                String deliverCharges = jsonObj.getString("delivery_charge");
                                int finalTotal = jsonObj.getInt("final_total");
                                int taxprice = jsonObj.getInt("taxprice");
                                int tax = jsonObj.getInt("tax");

                                //saveAddressListener.saveShippingDetails(,deliverCharges,finalTotal,tax,taxprice);
                                // showDialog(jsonObj.getString("msg"),1);
                                //  toastClass.makeToast(getActivity().getApplicationContext(), "Password updated successfully");
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
                showDialog("Something went wrong. Please try again !!!");
                progressBar.setVisibility(View.GONE);
                btnSend.setClickable(true);
                //Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private void showDialog(String message) {
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Shipping Details")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        alertDialog.dismiss();
                    }
                }).show();
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

}
