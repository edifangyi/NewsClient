package com.example.fangyi.newsclient.base.menudetail;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.fangyi.newsclient.base.BaseMenuDetailPager;

/**
 * 菜单详情页 -- 互动
 * Created by FANGYI on 2016/7/8.
 */

public class InteractMenuDetailPager extends BaseMenuDetailPager {
    public InteractMenuDetailPager(Activity mActivity) {
        super(mActivity);
    }

    @Override
    public View initViews() {
        TextView textView = new TextView(mActivity);
        textView.setText("菜单详情页 -- 互动");
        textView.setGravity(Gravity.CENTER);
        return textView;
    }
}
