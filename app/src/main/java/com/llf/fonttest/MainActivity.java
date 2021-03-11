package com.llf.fonttest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    String TAG = "react hahahha";

    MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    final String ACTION_ANGE = "react_hahaha";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv_1 = (TextView) findViewById(R.id.tv_1);
        Typeface typeFace1 = Typeface.createFromAsset(getAssets(), "fonts/FZSTK.TTF");
        tv_1.setTypeface(typeFace1);

        TextView tv_2 = (TextView) findViewById(R.id.tv_2);
        Typeface typeFace2 = Typeface.createFromAsset(getAssets(), "fonts/SIMLI.TTF");
        tv_2.setTypeface(typeFace2);

        IntentFilter intentFilter = new IntentFilter(ACTION_ANGE);
//        intentFilter.addAction(ACTION_ANGE); //为BroadcastReceiver指定action，即要监听的消息名字。
        registerReceiver(myBroadcastReceiver, intentFilter); //注册监听

        tv_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "run: ");
                        sendBroadcast(new Intent(ACTION_ANGE),null);
                    }
                }, 1000);
            }
        });


    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: " + action);
            //判断是否接到电池变换消息
            if (ACTION_ANGE.equals(action)) {
                //处理内容
//                Log.d(TAG, "onReceive 1111: " + DataManager.getDateTips(activity(), DAY_COST, Calendar.getInstance().getTimeInMillis()));
                Toast.makeText(getApplicationContext(), "hhaha", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(myBroadcastReceiver);
        super.onDestroy();
    }
}
