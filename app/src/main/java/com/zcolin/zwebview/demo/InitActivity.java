/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 上午8:51
 * ********************************************************
 */

package com.zcolin.zwebview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.fosung.ui.R;


public class InitActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        load();

        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void load() {
        //TODO 加载数据
    }
}
