package com.lk.bihu.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.lk.bihu.R;

/**
 * 详情Fragment
 */
public class DetailFragment extends BaseFragment{
    private WebView webView;
    @Override
    protected int getLayoutId() {
        return R.layout.detail_fragment_layout;
    }

    @Override
    protected void initView(View v) {
        webView= (WebView) v.findViewById(R.id.detail_frag_web);
    }

    @Override
    protected void initData() {
        Bundle bundle = getArguments();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadDataWithBaseURL(null, bundle.getString("body"), "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient());
        //控制图片不超出屏幕范围
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
    }
}
