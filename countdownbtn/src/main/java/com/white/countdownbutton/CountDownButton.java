package com.white.countdownbutton;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

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
    private static final int DEFAULT_COUNT = 60;
    /**
     * 默认倒计时文字格式(显示秒数)
     */
    private static final String DEFAULT_COUNT_FORMAT = "%d";
    /**
     * 默认按钮文字 {@link #getText()}
     */
    private String mDefaultText;
    /**
     * 倒计时时长
     */
    private int mCount;
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
    private boolean canCountDown = false;
    /**
     * 点击事件监听器
     */
    private OnClickListener onClickListener;

    private Subscription click;

    public void setCanCountDown(boolean canCountDown) {
        this.canCountDown = canCountDown;
    }

    public void setCountDownFormat(String countDownFormat) {
        this.mCountDownFormat = countDownFormat;
    }

    public void setCount(int count) {
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
    public void setCountDown(int count, long interval, String countDownFormat) {
        this.mCount = count;
        this.mCountDownFormat = countDownFormat;
        this.mInterval = interval;
        setCanCountDown(true);
    }

    /**
     * 移除倒计时
     */
    public void removeCountDown() {
        if (click != null) {
            click.unsubscribe();
            setText(mDefaultText);
            setEnabled(true);
            setClickable(true);
        }
    }

    public CountDownButton(Context context) {
        super(context);
    }

    public CountDownButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CountDownButton);
        mCountDownFormat = typedArray.getString(R.styleable.CountDownButton_countDownFormat);
        if (typedArray.hasValue(R.styleable.CountDownButton_countDown)) {
            this.canCountDown = true;
            mCount = (int) typedArray.getFloat(R.styleable.CountDownButton_countDown, DEFAULT_COUNT);
        }
        mInterval = (int) typedArray.getFloat(R.styleable.CountDownButton_countDownInterval, DEFAULT_INTERVAL);
        typedArray.recycle();
    }

    public CountDownButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                if (canCountDown && rect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    mDefaultText = getText().toString();
                    setText(String.format(Locale.CHINA, mCountDownFormat, mCount));
                    setEnabled(false);
                    setClickable(false);
                    click = Observable.interval(0, mInterval, TimeUnit.MILLISECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<Long>() {
                                @Override
                                public void call(Long aLong) {
                                    long nowCount = mCount - aLong;
                                    if (nowCount == 0) {
                                        setEnabled(true);
                                        setClickable(true);
                                        setText(mDefaultText);
                                        click.unsubscribe();
                                    } else if (mCount - aLong > 0) {
                                        setText(String.format(Locale.CHINA, mCountDownFormat, nowCount));
                                    }
                                }
                            });
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }
}
