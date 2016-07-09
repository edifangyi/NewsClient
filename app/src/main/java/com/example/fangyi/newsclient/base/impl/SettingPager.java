package com.example.fangyi.newsclient.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.BasePager;

/**
 * 设置页面
 * 
 * @author Kevin
 * 
 */
public class SettingPager extends BasePager {

	public SettingPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		
		tvTitle.setText(R.string.ContentTitleText5);
		btnMenu.setVisibility(View.GONE);
		setSlidingMenuEnable(false);//关闭侧边栏

		TextView text = new TextView(mActivity);
		text.setText("设置");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		// 向FrameLayout中动态添加布局
		flContent.addView(text);
	}

}
