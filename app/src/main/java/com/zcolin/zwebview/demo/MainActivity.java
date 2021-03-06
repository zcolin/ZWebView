package com.zcolin.zwebview.demo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;

import com.fosung.ui.R;
import com.zcolin.frame.util.ActivityUtil;


/**
 * 程序主页面
 */
public class MainActivity extends AppCompatActivity {

    private Button btnWebview;
    private Button btnWebviewvideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btnWebview = findViewById(R.id.btn_webview);
        btnWebviewvideo = findViewById(R.id.btn_webviewvideo);
        btnWebview.setOnClickListener(v -> ActivityUtil.startActivity(this, WebViewActivity.class));
        btnWebviewvideo.setOnClickListener(v -> ActivityUtil.startActivity(this, WebViewVideoActivity.class));
    }
}
