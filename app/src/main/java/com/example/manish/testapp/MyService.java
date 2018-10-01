package com.example.manish.testapp;

import android.app.Service;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    private int mRandomNumber;
    private boolean mIsRandomNumberGenerationOn;
    private static final int MIN_NUMBER = 0;
    private static final int MAX_NUMBER = 99;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand - Thread Id : " + Thread.currentThread().getId());
        mIsRandomNumberGenerationOn = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGeneration();
            }
        }).start();
        return START_STICKY;
    }


    // Binder is an Abstract class that implements IBinder interface.
    // Binder implements IBinder interface.
    class MyServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind - Thread Id : " + Thread.currentThread().getId());
        return new MyServiceBinder();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mIsRandomNumberGenerationOn = false;
        Log.i(TAG, "onDestroy - Thread Id : " + Thread.currentThread().getId());
    }

    private void startRandomNumberGeneration() {
        while (mIsRandomNumberGenerationOn) {
            try {
                Thread.sleep(1000);
                if (mIsRandomNumberGenerationOn) {
                    mRandomNumber = new Random().nextInt(MAX_NUMBER) + MIN_NUMBER;
                    Log.i(TAG, "Thread Id : " + Thread.currentThread().getId() + " Random Number : " + mRandomNumber);
                }
            } catch (InterruptedException e) {
                Log.i(TAG, "Thread Interrupted");
            }
        }
    }

    private void stopRandomNumberGeneration() {
        mIsRandomNumberGenerationOn = false;
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }
}