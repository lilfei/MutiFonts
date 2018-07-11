package com.llf.fonttest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

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
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
