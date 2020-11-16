package com.example.Fragment;


import android.app.AlertDialog;
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
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.android.volley.VolleyError;
import com.example.Interface.IResult;
import com.example.Model.UserProfileModel;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.merakirana.R;
import com.example.service.BlockStatusService;
import com.rilixtech.Country;
import com.rilixtech.CountryCodePicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;

import static com.example.Utility.ConstantVariables.UPDATE_USER_PROFILE;
import static com.example.Utility.ConstantVariables.USER_PROFILE;
import static com.example.Utility.SessionManager.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private IResult mResultCallback;
    private VolleyService mVolleyService;
    //   TextView btnEditProf;
    String profile_url="Profile-Customer";
    String update_prof_url="Update-Profile-Customer";

    ProgressDialog progressDialog;
    SessionManager sessionManager;
    UserProfileModel userProfileModel=new UserProfileModel();
    View view;
    Toolbar toolbar;

    AlertDialog alertDialog;

    EditText etName,etEmail,etMobile,etAddress,etCity,etArea,etPincode;
    Button btnUpdate;
    private CountryCodePicker mCountryCodePicker;
    String country_code="";

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);

        init();
        showUserProfile();
        return view;

    }

    private void init() {
        toolbar = view.findViewById(R.id.skintool);

        sessionManager=new SessionManager(getActivity());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etMobile = view.findViewById(R.id.etContact);
        etAddress = view.findViewById(R.id.etAddress);
        etCity = view.findViewById(R.id.etCity);
        etArea = view.findViewById(R.id.etArea);
        etPincode = view.findViewById(R.id.etPin);
        btnUpdate = view.findViewById(R.id.btnUpadte);
        mCountryCodePicker=view.findViewById(R.id.ccp);

        //  mCountryCodePicker.setClickable(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        //close keyboard
        mCountryCodePicker.setKeyboardAutoPopOnSearch(false);

        mCountryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected(Country selectedCountry) {
                country_code=mCountryCodePicker.getSelectedCountryNameCode()+"-"+mCountryCodePicker.getSelectedCountryCode();
            }
        });

    }

    private void showDialog(String message, final int status){
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Profile")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (status==2){
                            showUserProfile();
                            alertDialog.dismiss();
                            getActivity().getSupportFragmentManager().popBackStack();

                        }
                        else {
                            alertDialog.dismiss();
                        }
                    }
                }).show();
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

                            if (status == 0) {
                                showDialog(jsonObj.getString("msg"),status);
                                progressDialog.dismiss();                            }
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

                                etName.setText(userProfileModel.getName());
                                etEmail.setText(userProfileModel.getEmail());
                                etMobile.setText(userProfileModel.getContact());
                                etAddress.setText(userProfileModel.getAddress());
                                etCity.setText(userProfileModel.getCity());
                                etArea.setText(userProfileModel.getArea());
                                etPincode.setText(userProfileModel.getPincode());

                                country_code=userProfileModel.getCountryCode();
                                String[] lCountryNameCode=country_code.split("-");
                                mCountryCodePicker.setCountryForNameCode(lCountryNameCode[0]);

                                progressDialog.dismiss();
                            }
                        }
                        catch (Exception e) {

                            Log.v("User Profile", e.toString());
                        }
                        progressDialog.dismiss();
                        break;

                    case UPDATE_USER_PROFILE:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("change profile",response.toString());

                            int status = jsonObj.getInt("status");

                            if (status == 0) {
                                showDialog(jsonObj.getString("msg"),status);
                                progressDialog.dismiss();                            }
                            else if (status == 1) {
                                showDialog("Profile updated successfully",2);
                                progressDialog.dismiss();
                            }
                        }
                        catch (Exception e) {
                            Log.v("Update profile", e.toString());
                            progressDialog.dismiss();
                        }
                        progressDialog.dismiss();
                        // progressBar.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                showDialog("Something went wrong. Please try again !!!",0);
                progressDialog.dismiss();
                //Toast.makeText(getActivity().getApplicationContext(), "Something went wrong. Please try again !!!", Toast.LENGTH_LONG).show();
                Log.v("Volley requestid", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
                //  progressBar.setVisibility(View.GONE);
            }
        };
    }

    private void editProfile() {

        if (checkValid()){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, false, true);

            //progressBar.setVisibility(View.VISIBLE);
            initVolleyCallback();

            HashMap<String,String> user=sessionManager.getUserDetails();
            String id=user.get(KEY_ID);

            mVolleyService = new VolleyService(mResultCallback, getActivity());

            Map<String, String> params = new HashMap<>();
            params.put("customer_auto_id",id );
            params.put("name", etName.getText().toString());
            params.put("email", etEmail.getText().toString());
            params.put("country_code",country_code);
            params.put("contact", etMobile.getText().toString());
            params.put("city", etCity.getText().toString());
            params.put("area", etArea.getText().toString());
            params.put("address", etAddress.getText().toString());
            params.put("pincode", etPincode.getText().toString());

            String url = getResources().getString(R.string.BASE_URL) + update_prof_url;
            Log.d("edit profile",params.toString());

            mVolleyService.postDataVolleyParameters(UPDATE_USER_PROFILE, url, params);

        }
    }

    public boolean checkValid() {

        String contactPattern = "^[0-9]{10,15}$";
        String emailPattern = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String namepattern= "^[a-zA-Z ][a-zA-Z \\\\s]+$";

        if (etName.getText().toString().equalsIgnoreCase("")) {
            etName.setError("Please enter name");
            return false;
        }

        else if(!etName.getText().toString().matches(namepattern)){
            etName.setError("Please enter only text");
            return false;
        }  else if (etEmail.getText().toString().equalsIgnoreCase("")) {
            etEmail.setError("Please enter email id");
            return false;
        } else if (!etEmail.getText().toString().matches(emailPattern)) {
            etEmail.setError("Please enter valid email id");
            return false;
        }
        else if (etMobile.getText().toString().equalsIgnoreCase("")) {
            etMobile.setError("Please enter contact");
            return false;
        }
        else if (!etMobile.getText().toString().matches(contactPattern)) {
            etMobile.setError("Please enter valid contact");
            return false;
        }

        else if (etAddress.getText().toString().equalsIgnoreCase("")) {
            etAddress.setError("Please enter address");
            return false;
        }
        else if (etCity.getText().toString().equalsIgnoreCase("")) {
            etCity.setError("Please enter city");
            return false;
        }
        else if (!etCity.getText().toString().matches(namepattern)) {
            etCity.setError("Please enter city");
            return false;
        }
        else if(!etArea.getText().toString().matches(namepattern)){
            etArea.setError("Please enter area");
            return false;
        }
        else if (etPincode.getText().toString().equalsIgnoreCase("")) {
            etPincode.setError("Please enter zip code");
            return false;
        }
        /*else if (!etPincode.getText().toString().matches(pincodeRegex)) {
            etPincode.setError("Please enter valid pincode");
            return false;
        }
*/
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        //to check block status register broadcast
        Intent background = new Intent(getActivity(), BlockStatusService.class);
        getActivity().startService(background);
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("efunhub.com.automobile.closeactivity"));

    }

    @Override
    public void onPause() {
        super.onPause();

        //to check block status Unregister broadcast
        Intent background = new Intent(getActivity(), BlockStatusService.class);
        getActivity().stopService(background);
        getActivity().unregisterReceiver(broadcastReceiver);

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("efunhub.com.automobile.closeactivity")) {
                getActivity().finish();
            }
        }
    };


}
