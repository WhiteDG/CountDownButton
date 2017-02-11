package com.white.countdownbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import java.util.Locale;

/**
 * Author: Wh1te
 * Date: 2016/10/18
 */

public class CountDownButton extends Button {

    /**
     * 默认时间间隔1000ms
     */
    private static final long DEFAULT_INTERVAL = 1000;
    /**
     * 默认时长60s
     */
    private static final long DEFAULT_COUNT = 60 * 1000;
    /**
     * 默认倒计时文字格式(显示秒数)
     */
    private static final String DEFAULT_COUNT_FORMAT = "%d";
    /**
     * 默认按钮文字 {@link #getText()}
     */
    private String mDefaultText;
    /**
     * 倒计时时长，单位为秒
     */
    private long mCount;
    /**
     * 时间间隔
     */
    private long mInterval;
    /**
     * 倒计时文字格式
     */
    private String mCountDownFormat = DEFAULT_COUNT_FORMAT;
    /**
     * 倒计时是否可用
     */
    private boolean mEnableCountDown = true;
    /**
     * 点击事件监听器
     */
    private OnClickListener onClickListener;

    /**
     * 倒计时
     */
    private CountDownTimer mCountDownTimer;

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownButton);
        mCountDownFormat = typedArray.getString(R.styleable.CountDownButton_countDownFormat);
        if (typedArray.hasValue(R.styleable.CountDownButton_countDown)) {
            mCount = (int) typedArray.getFloat(R.styleable.CountDownButton_countDown, DEFAULT_COUNT);
        }
        mInterval = (int) typedArray.getFloat(R.styleable.CountDownButton_countDownInterval, DEFAULT_INTERVAL);
        mEnableCountDown = (mCount > mInterval) && typedArray.getBoolean(R.styleable.CountDownButton_enableCountDown, true);
        typedArray.recycle();
    }


    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        super.setOnClickListener(onClickListener);
        this.onClickListener = onClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                Rect rect = new Rect();
                this.getGlobalVisibleRect(rect);
                if (onClickListener != null && rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    onClickListener.onClick(this);
                }
                if (mEnableCountDown && rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    mDefaultText = getText().toString();
                    setText(String.format(Locale.CHINA, mCountDownFormat, mCount));
                    setEnabled(false);
                    setClickable(false);
                    if (mCountDownTimer == null) {
                        mCountDownTimer = new CountDownTimer(mCount, mInterval) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                setText(String.format(Locale.CHINA, mCountDownFormat, millisUntilFinished / 1000));
                            }

                            @Override
                            public void onFinish() {
                                setEnabled(true);
                                setClickable(true);
                                setText(mDefaultText);
                            }
                        };
                    }
                    mCountDownTimer.start();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setEnableCountDown(boolean enableCountDown) {
        this.mEnableCountDown = (mCount > mInterval) && enableCountDown;
    }

    public void setCountDownFormat(String countDownFormat) {
        this.mCountDownFormat = countDownFormat;
    }

    public void setCount(long count) {
        this.mCount = count;
    }

    public void setInterval(long interval) {
        mInterval = interval;
    }

    /**
     * 设置倒计时数据
     *
     * @param count           时长
     * @param interval        间隔
     * @param countDownFormat 文字格式
     */
    public void setCountDown(long count, long interval, String countDownFormat) {
        this.mCount = count;
        this.mCountDownFormat = countDownFormat;
        this.mInterval = interval;
        setEnableCountDown(true);
    }

    /**
     * 移除倒计时
     */
    public void removeCountDown() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            setText(mDefaultText);
            setEnabled(true);
            setClickable(true);
        }
    }
}
