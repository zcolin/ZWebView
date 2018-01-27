/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */

package com.zcolin.ui.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.fosung.ui.R;
import com.zcolin.frame.util.ActivityUtil;


/**
 * 程序主页面
 */
public class MainActivity extends AppCompatActivity {
    private Button btnWebview;//WebViewActivity
    private Button btnWebviewvideo;//WebViewVideoActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnWebview = findViewById(R.id.btn_webview);
        btnWebviewvideo = findViewById(R.id.btn_webviewvideo);
        btnWebview.setOnClickListener(v -> ActivityUtil.startActivity(this,WebViewActivity.class));
        btnWebviewvideo.setOnClickListener(v -> ActivityUtil.startActivity(this, WebViewVideoActivity.class));
    }
}
