# CountDownButton
CountDownButton是一个具有倒计时功能的自定义按钮

## 使用

```
// 布局文件：
<com.white.countdownbutton.CountDownButton
        android:id="@+id/cd_btn"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="get verifyCode"
        app:countDown="60000"
        app:countDownFormat="Please wait (%ds)"
        app:countDownInterval="1000"
        app:enableCountDown="true"
        />
        
// Activity代码：
...
mCountDownButton = (CountDownButton) findViewById(R.id.cd_btn);
// 设置点击事件
mCountDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点击按钮要执行的操作,同时开始倒计时
                Toast.makeText(activity, "click countdown button", Toast.LENGTH_SHORT).show();
            }
        });

mBtnStop = (Button) findViewById(R.id.btn_stop)
mBtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用removeCountDown()即可取消当前倒计时
                mCountDownButton.removeCountDown();
            }
        });
```

## 属性和方法

你可以在在xml中配置的属性

属性 | 说明
---|---
countDown | 倒计时总时长(单位为毫秒)，默认60000毫秒
countDownFormat | 倒计时过程中按钮文本的格式，默认"%d"，直接显示剩余秒数
countDownInterval | 倒计时间隔(单位为毫秒)，默认1000毫秒
enableCountDown | 倒计时是否可用，默认为true

你可以通过以下方法在代码中动态设置相关的属性

方法 | 说明
---|---
setEnableCountDown(boolean enableCountDown) | 设置倒计时是否可用
setCountDownFormat(String countDownFormat) | 设置倒计时过程中按钮文本的格式
setCount(int count) | 设置倒计时总时长，单位为毫秒
setInterval(long interval) | 设置倒计时间隔，单位为毫秒
setCountDown(int count, long interval, String countDownFormat) | 同时设置三个属性
removeCountDown() | 取消倒计时

## Licence

[MIT License](https://opensource.org/licenses/MIT) @[Wh1te](https://github.com/WhiteDG)
