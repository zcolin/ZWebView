package com.zcolin.zwebview.demo.app;

import com.zcolin.frame.BuildConfig;
import com.zcolin.frame.app.BaseApp;
import com.zcolin.frame.util.LogUtil;

/**
 * 程序入口
 */
public class App extends BaseApp {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.LOG_DEBUG = BuildConfig.DEBUG;
    }
}
