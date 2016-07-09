package com.example.fangyi.newsclient.base.impl;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.BasePager;

/**
 * 首页实现
 * Created by FANGYI on 2016/7/6.
 */

public class HomePager extends BasePager {

    public HomePager(Activity Activity) {
        super(Activity);
    }

    @Override
    public void initData() {
        tvTitle.setText(R.string.ContentTitleText1);//修改标题
        btnMenu.setVisibility(View.GONE);
        setSlidingMenuEnable(false);//关闭侧边栏

        TextView textView = new TextView(mActivity);
        textView.setText("首页");
        textView.setGravity(Gravity.CENTER);
        flContent.addView(textView);//向FrameLayout中动态添加布局
    }
}
