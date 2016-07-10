package com.example.fangyi.newsclient.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ViewPager 事件分发，请求父控件及祖宗控件是否拦截事件  暂时没用
 * true拦截，false不拦截
 * Created by FANGYI on 2016/7/6.
 */

public class NewsViewPager extends ViewPager {

    public NewsViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentItem() != 0) {
            getParent().requestDisallowInterceptTouchEvent(true);
        } else {
            //如果是第一个页面，需要显示侧边栏，请求父控件拦截
            getParent().requestDisallowInterceptTouchEvent(false);
        }

        return super.dispatchTouchEvent(ev);
    }
}
