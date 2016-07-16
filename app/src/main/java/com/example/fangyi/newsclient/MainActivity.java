package com.example.fangyi.newsclient;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.example.fangyi.newsclient.fragment.ContentFragment;
import com.example.fangyi.newsclient.fragment.LeftMenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

    private static final String FRAGMENT_LEFT_MENU = "fragment_left_menu";
    private static final String FRAGMENT_CONTENT = "fragment_content";

    private SlidingMenu slidingMenu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initSlidingMenu();
        initFragment();
    }

    /**
     * 初始化侧边栏
     */
    private void initSlidingMenu() {
        setBehindContentView(R.layout.left_menu);//设置侧边栏布局
        slidingMenu = getSlidingMenu();//获取侧边栏对象
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);//设置侧边拖拽

        int width = getWindowManager().getDefaultDisplay().getWidth();//获取屏幕宽度，代码适配

        slidingMenu.setBehindOffset(width * 200 / 320);//设置预留屏幕宽度
//        slidingMenu.setBehindWidth(500);//设置侧边栏宽度-像素
//        slidingMenu.setShadowDrawable(R.drawable.shape_left_menu_shadow);//设置分割线资源
//        slidingMenu.setShadowWidthRes(R.dimen.slidingmenu_shadow_width);//给分割线设置宽度
    }

    /**
     * 初始化fragment,将Fragment数据填充给布局文件
     */
    private void initFragment() {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();//开启事务
        transaction.replace(R.id.left_menu, new LeftMenuFragment(), FRAGMENT_LEFT_MENU);//用fragment替换framelayout
        transaction.replace(R.id.activity_main, new ContentFragment(), FRAGMENT_CONTENT);
        transaction.commit();//提交事务

        // Fragment leftMenuFragment = fm.findFragmentByTag(FRAGMENT_LEFT_MENU);//打的标记找fragment

    }

    /**
     * 获取侧边栏Fragment
     *
     * @return
     */
    public LeftMenuFragment getLeftMenuFragment() {
        FragmentManager fm = getSupportFragmentManager();
        LeftMenuFragment fragment = (LeftMenuFragment) fm.findFragmentByTag(FRAGMENT_LEFT_MENU);
        return fragment;
    }

    /**
     * 获取主页面fragment
     */

    public ContentFragment getContentFragment() {
        FragmentManager fm = getSupportFragmentManager();
        ContentFragment fragment = (ContentFragment) fm
                .findFragmentByTag(FRAGMENT_CONTENT);

        return fragment;
    }


}
