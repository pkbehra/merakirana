package com.example.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.Interface.IResult;
import com.example.Utility.SessionManager;
import com.example.Utility.VolleyService;
import com.example.merakirana.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.Nullable;

import static com.example.Utility.ConstantVariables.BLOCK_STATUS;
import static com.example.Utility.SessionManager.KEY_ID;

/**
 * Created by Admin on 01-06-2018.
 */

public class BlockStatusService extends Service {

    private boolean isRunning;
    private Context mContext;
    private Handler handler;
    private Timer timer;
    private TimerTask doAsynchronousTask;
    private SessionManager sessionManager;


    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;

    private String BlockStatusURL ="Check-Customer-Status";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        this.mContext = this;
        this.isRunning = false;
        handler = new Handler();
        timer = new Timer();
        sessionManager = new SessionManager(this);
        Log.d("onCreate Service","oncreate");

    }

    @Override
    public void onDestroy() {
        this.isRunning = false;
        doAsynchronousTask.cancel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("onStartCommand Service","onStartCommand");
        if (!this.isRunning) {
            this.isRunning = true;

            // Timer task makes your service will repeat after every 20 Sec.
            doAsynchronousTask = new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            getBlockStatus();
                        }

                    });
                }
            };
            //Starts after 20 sec and will repeat on every 20 sec of time interval.
            timer.schedule(doAsynchronousTask, 20000, 20000);  // 20 sec timer
        }
        return START_STICKY;
    }

    private void getBlockStatus() {
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, mContext);

        HashMap<String,String> user=sessionManager.getUserDetails();
        String id=user.get(KEY_ID);

        Map<String, String> params = new HashMap<>();
        params.put("customer_auto_id",id);

        String url = getResources().getString(R.string.BASE_URL) + BlockStatusURL;
        Log.d("Block status",params.toString());

        mVolleyService.postDataVolleyParameters(BLOCK_STATUS,url,params);
    }

    private void initVolleyCallback() {
        mResultCallBack = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = new JSONObject(response);
                    String activestatus=jsonObject.get("activestatus").toString();
                    int status=jsonObject.getInt("status");

                    Log.e("activestatus",activestatus);

                    if(status == 1)
                    {
                        if (activestatus.equalsIgnoreCase("Block")) {
                            Intent intent = new Intent();
                            intent.setAction("efunhub.com.automobile.closeactivity");
                            sendBroadcast(intent);
                            sessionManager.logoutUser();
                            doAsynchronousTask.cancel();
                            Toast.makeText(mContext, "Your account is blocked", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                Log.v("Volley RequestId", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }
}
