package com.example.fangyi.newsclient.base;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.fangyi.newsclient.MainActivity;
import com.example.fangyi.newsclient.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页下5个子页面的基类
 * <p>
 * Created by FANGYI on 2016/7/6.
 */

public class BasePager {
    public Activity mActivity;
    public View mRootView;//布局对象

    @BindView(R.id.tv_title)
    public TextView tvTitle;//标题对象
    @BindView(R.id.fl_content)
    public FrameLayout flContent;//内容
    @BindView(R.id.btn_menu)
    public ImageButton btnMenu;//菜单按钮

    public BasePager(Activity Activity) {
        this.mActivity = Activity;
        initViews();
    }

    /**
     * 初始化布局
     */
    public void initViews() {
        mRootView = View.inflate(mActivity, R.layout.base_pager, null);
        ButterKnife.bind(this, mRootView);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSlidingMenu();
            }
        });
    }

    /**
     * 初始化数据
     */
    public void initData() {

    }

    /**
     * 设置侧边栏开启或关闭
     * @param enable
     */
    public void setSlidingMenuEnable(boolean enable) {
        MainActivity mainUi = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUi.getSlidingMenu();
        if (enable) {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        }else {
            slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        }
    }


    /**
     * 切换SlidingMenu状态
     */
    private void toggleSlidingMenu() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
    }


}
