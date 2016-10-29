package com.white.countdown;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.white.countdownbutton.CountDownButton;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

    Button mBtnStop;
    CountDownButton mCountDownButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnStop = $(R.id.btn_stop);
        mCountDownButton = $(R.id.cd_btn);
        mCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "doSomething...");
            }
        });
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownButton.removeCountDown();
            }
        });

    }
}
