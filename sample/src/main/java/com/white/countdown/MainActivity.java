package com.white.countdown;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.white.countdownbutton.CountDownButton;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int SUCCESS_CODE = 0;
    public static final int ERROR_CODE = 1;

    private static class MyHandler extends Handler {
        WeakReference<MainActivity> mActivityWeakReference;

        public MyHandler(MainActivity activity) {
            mActivityWeakReference = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mActivityWeakReference.get();
            if (activity != null) {
                switch (msg.what) {
                    case SUCCESS_CODE:
                        Toast.makeText(activity, "获取验证码成功", Toast.LENGTH_SHORT).show();
                        break;
                    case ERROR_CODE:
                        Toast.makeText(activity, "获取验证码失败", Toast.LENGTH_SHORT).show();
                        activity.mCountDownButton.removeCountDown();
                        break;
                    default:

                        break;
                }
            }
        }
    }

    private EditText mEtPhone;
    private Button mBtnStop;
    private CountDownButton mCountDownButton;
    private MyHandler mMyHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyHandler = new MyHandler(this);

        mEtPhone = $(R.id.et_phone);
        mBtnStop = $(R.id.btn_stop);
        mCountDownButton = $(R.id.cd_btn);
        mCountDownButton.setEnabled(!TextUtils.isEmpty(mEtPhone.getText()));
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mCountDownButton.setEnabled(!TextUtils.isEmpty(s.toString()) && !mCountDownButton.isCountDownNow());
            }
        });
        mCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = mEtPhone.getText().toString();
                if (TextUtils.isEmpty(phoneNum)) {
                    Toast.makeText(MainActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                getSmsCode(phoneNum);
            }
        });
        mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountDownButton.removeCountDown();
            }
        });
    }

    private void getSmsCode(final String phoneNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 模拟发送请求获取验证码
                try {
                    Thread.sleep(5000);
                    if (phoneNum.equals("10086")) {
                        mMyHandler.sendEmptyMessage(SUCCESS_CODE);
                    } else {
                        mMyHandler.sendEmptyMessage(ERROR_CODE);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    mMyHandler.sendEmptyMessage(ERROR_CODE);
                }
            }
        }).start();
    }

    private <T extends View> T $(int resId) {
        return (T) findViewById(resId);
    }

}
