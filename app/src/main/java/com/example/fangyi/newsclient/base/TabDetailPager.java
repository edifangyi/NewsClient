package com.example.fangyi.newsclient.base;

        import android.app.Activity;
        import android.view.Gravity;
        import android.view.View;
        import android.widget.TextView;

        import com.example.fangyi.newsclient.domain.NewsData;

/**
 * 页签详情页
 * Created by FANGYI on 2016/7/9.
 */

public class TabDetailPager extends BaseMenuDetailPager {
    private NewsData.NewsTabData mNewsTabData;
    private TextView textView;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData newsTabData) {
        super(mActivity);
        mNewsTabData = newsTabData;
    }

    @Override
    public View initViews() {
        textView = new TextView(mActivity);
        textView.setText("页签详情页");
        textView.setGravity(Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {
        textView.setText(mNewsTabData.title);
    }
}
