package com.lk.bihu.activity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.lk.bihu.BihuApplication;
import com.lk.bihu.R;
import com.lk.bihu.utils.DeviceUtil;

public abstract class BaseActivity extends FragmentActivity {
    private BihuApplication app = null;
    private WindowManager mWindowManager = null;
    private View mNightView = null;
    private WindowManager.LayoutParams mNightViewParam;
    private boolean mIsAddedView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        app = (BihuApplication) getApplication();
        if (app.isNightMode()) {
            setTheme(R.style.AppTheme_night);
        } else {
            setTheme(R.style.AppTheme_day);
        }
        super.onCreate(savedInstanceState);
        mIsAddedView = false;
        if (app.isNightMode()) {
            initNightView();
            mNightView.setBackgroundResource(R.color.night_mask);
        }
        setContentView(getLayoutId());
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        DeviceUtil.setTranslucent(BaseActivity.this);
    }

    @Override
    protected void onDestroy() {
        if (mIsAddedView) {
            app = null;
            mWindowManager.removeViewImmediate(mNightView);
            mWindowManager = null;
            mNightView = null;
        }
        super.onDestroy();
    }

    public void ChangeToDay() {
        app.setNightMode(false);
        mNightView.setBackgroundResource(android.R.color.transparent);
    }

    public void ChangeToNight() {
        app.setNightMode(true);
        initNightView();
        mNightView.setBackgroundResource(R.color.night_mask);
    }

    /**
     * wait a time until the onresume finish
     */
    public void recreateOnResume() {
        new Handler().postDelayed(new Runnable() {
            public void run() {
                recreate();
            }
        }, 100);
    }

    private void initNightView() {
        if (mIsAddedView == true)
            return;
        mNightViewParam = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.TYPE_APPLICATION,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT);
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        mNightView = new View(this);
        mWindowManager.addView(mNightView, mNightViewParam);
        mIsAddedView = true;
    }

    /**
     * 得到布局id
     */
    protected abstract int getLayoutId();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 逻辑代码
     */
    protected abstract void initData();

    /**
     * 显示一个短的土司
     */
    protected void showShortToast(String text) {
        Toast.makeText(BaseActivity.this, text, Toast.LENGTH_SHORT).show();
    }
    /**
     * dp转成px
     */
    public int dip2px(float dipValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
