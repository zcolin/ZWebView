/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */
package com.zcolin.zwebview.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.fosung.ui.R;
import com.zcolin.zwebview.ZWebView;


/**
 * 带JsBridge的webview的Demo
 */
public class WebViewVideoActivity extends AppCompatActivity {
    private ZWebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_video);

        webView = findViewById(R.id.webView);
        webView.setSupportVideoFullScreen(this);
        webView.setSupportProgressBar();

        webView.loadUrl("http://app.html5.qq.com/navi/index");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.hideCustomView()) {
                return true;
            } else if (webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
