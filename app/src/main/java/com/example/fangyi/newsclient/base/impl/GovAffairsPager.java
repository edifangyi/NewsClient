package com.example.fangyi.newsclient.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.BasePager;


/**
 * 政务
 * 
 * @author Kevin
 * 
 */
public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		tvTitle.setText(R.string.ContentTitleText4);
		setSlidingMenuEnable(true);//开启侧边栏

		TextView text = new TextView(mActivity);
		text.setText("政务");
		text.setTextColor(Color.RED);
		text.setTextSize(25);
		text.setGravity(Gravity.CENTER);

		// 向FrameLayout中动态添加布局
		flContent.addView(text);
	}

}
