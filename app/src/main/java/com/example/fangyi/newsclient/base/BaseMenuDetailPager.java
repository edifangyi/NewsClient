package com.example.fangyi.newsclient.base;

import android.app.Activity;
import android.view.View;

/**
 * 菜单详情页基类
 * Created by FANGYI on 2016/7/8.
 */

public abstract class BaseMenuDetailPager {
    public Activity mActivity;

    public View mRootView;//跟布局对象

    public BaseMenuDetailPager(Activity mActivity) {
        this.mActivity = mActivity;
        this.mRootView = initViews();
    }

    /**
     * 初始化界面
     */
    public abstract View initViews();

    /**
     * 初始化数据
     */
    public void initData() {

    }

}
