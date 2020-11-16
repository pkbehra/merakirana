package com.example.Fragment;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toolbar;

import com.android.volley.VolleyError;
import com.example.Interface.IResult;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.merakirana.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.Fragment;

import static com.example.Utility.ConstantVariables.CHANGE_PASSWORD;
import static com.example.Utility.SessionManager.KEY_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangePass extends Fragment {

    EditText oldPassEt,newPassEt;
    TextView resetPassBtn;
    private IResult mResultCallback;
    private VolleyService mVolleyService;
    String change_pass_url="Update-Password-Customer";
    SessionManager sessionManager;
    ProgressDialog progressDialog;
    AlertDialog alertDialog;
    View view;
    Toolbar toolbar;

    public ChangePass() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_change_pass, container, false);

        init();
        return view;
    }

    private void init() {
        sessionManager=new SessionManager(getActivity());
        toolbar = (Toolbar) view.findViewById(R.id.skintool);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().onBackPressed();

            }
        });

        oldPassEt=view.findViewById(R.id.etOldPass);
        newPassEt=view.findViewById(R.id.etNewPass);
        resetPassBtn=view.findViewById(R.id.btnUpadte);

        resetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword(){
        if (checkValid()){
            progressDialog = ProgressDialog.show(getActivity(), "Please Wait", null, false, true);

            initVolleyCallback();

            HashMap<String,String> user=sessionManager.getUserDetails();
            String id=user.get(KEY_ID);

            mVolleyService = new VolleyService(mResultCallback, getActivity());

            Map<String, String> params = new HashMap<>();
            params.put("customer_auto_id",id );
            params.put("oldp",oldPassEt.getText().toString() );
            params.put("newp",newPassEt.getText().toString() );

            String url = getResources().getString(R.string.BASE_URL) + change_pass_url;

            Log.d("reset password",params.toString());
            mVolleyService.postDataVolleyParameters(CHANGE_PASSWORD, url, params);
        }
    }

    private void showDialog(String message, final int status) {
        alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle("Change Password")
                .setMessage(message)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (status==1){
                            alertDialog.dismiss();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                        else {
                            alertDialog.dismiss();
                        }
                    }
                }).show();
    }

    private void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObj = null;

                switch (requestId) {

                    case CHANGE_PASSWORD:
                        try {
                            jsonObj = new JSONObject(response);
                            Log.d("change pass",response.toString());

                            int status = jsonObj.getInt("status");

                            if (status == 0) {
                                showDialog(jsonObj.getString("msg"),0);
                                // toastClass.makeToast(getActivity().getApplicationContext(), "Old password does not match");
                            }
                            else if (status == 1) {
                                showDialog(jsonObj.getString("msg"),1);
                                //  toastClass.makeToast(getActivity().getApplicationContext(), "Password updated successfully");

                                oldPassEt.setText("");
                                newPassEt.setText("");
                            }else if(status==2)
                            {
                                showDialog(jsonObj.getString("msg"),2);
                            }
                        }
                        catch (Exception e) {

                            Log.v("Change Password", e.toString());
                        }
                        progressDialog.dismiss();
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

    public boolean checkValid() {

        //   String passwordPattern = "^[a-zA-Z0-9]{8,}$";

        if (oldPassEt.getText().toString().equalsIgnoreCase("")) {
            oldPassEt.setError("Please enter old password");
            return false;
        } else if (oldPassEt.getText().length()<6) {
            oldPassEt.setError("Old password must be at least 6 characters");
            return false;
        }
        else if (newPassEt.getText().toString().equalsIgnoreCase("")) {
            newPassEt.setError("Please enter new password");
            return false;
        }

        else if (newPassEt.getText().length()<6) {
            newPassEt.setError("New password must be at least 6 characters");
            return false;
        }

        return true;
    }

}
