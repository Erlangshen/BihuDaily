package com.lk.bihu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.lk.bihu.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LoadingActivity extends Activity {
    @Bind(R.id.loading)
    RelativeLayout loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        initData();
    }

    protected int getLayoutId() {
        return R.layout.loading_layout;
    }

    protected void initData() {
        AnimationSet set1 = new AnimationSet(true);
        final AnimationSet set2 = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alpha = new AlphaAnimation(1, 0);
        alpha.setDuration(1000);
        scale.setDuration(2000);
        set1.addAnimation(scale);
        set2.addAnimation(alpha);
        set1.setFillAfter(true);
        set2.setFillAfter(true);
        set1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                loading.startAnimation(set2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        set2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Intent i = new Intent();
                i.setClass(LoadingActivity.this, MainActivity.class);
                startActivity(i);
                LoadingActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        loading.startAnimation(set1);
    }
}
