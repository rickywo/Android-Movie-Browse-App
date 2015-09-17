package edu.ricky.mada2.utility;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Ricky Wu on 2015/9/14.
 */
public class NetworkStateManager {
    private Context context;
    Handler mHandler = new Handler();
    private boolean isRunning = true;
    public boolean isConnected;

    public NetworkStateManager(Context context) {
        this.context = context;
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                while (isRunning) {
                    try {
                        Thread.sleep(10000);
                        mHandler.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                // Write your code here to update the UI.
                                checkNetwork();
                            }
                        });
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
            }
        }).start();
    }

    public void checkNetwork() {
        ConnectivityManager cn=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf=cn.getActiveNetworkInfo();
        if(nf != null && nf.isConnected()==true )
        {
            Log.e("Connection", String.valueOf(isConnected));
            isConnected = true;
        }
        else
        {
            Log.e("Connection", String.valueOf(isConnected));
            isConnected = false;
        }
    }

    public void stop() {
        isRunning = false;
    }

    public void start() {
        isRunning = true;
    }
}

