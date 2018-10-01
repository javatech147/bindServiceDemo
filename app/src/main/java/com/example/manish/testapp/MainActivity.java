package com.example.manish.testapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Intent myServiceIntent;

    private MyService myService;
    private ServiceConnection serviceConnection;
    private TextView tvRandomNumber;
    private boolean mIsServiceBound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvRandomNumber = findViewById(R.id.tvRandomNumber);
        myServiceIntent = new Intent(this, MyService.class);
        Log.d(TAG, "onCreate");
    }


    public void btnStartService(View view) {
        startService(myServiceIntent);
    }

    public void btnStopService(View view) {
        stopService(myServiceIntent);
    }

    public void btnBindService(View view) {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mIsServiceBound = true;
                MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) service;
                myService = myServiceBinder.getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mIsServiceBound = false;
            }
        };

        bindService(myServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    public void btnUnbindService(View view) {
        if (mIsServiceBound) {
            unbindService(serviceConnection);
            mIsServiceBound = false;
        }
    }

    public void btnDisplayText(View view) {
        if (mIsServiceBound) {
            tvRandomNumber.setText("Random Number : " + myService.getRandomNumber());
        } else {
            tvRandomNumber.setText("Service is not Bound");
        }
    }
}
