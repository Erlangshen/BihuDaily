package com.lk.bihu.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
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
}
