package com.example.fangyi.newsclient.base.impl;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;

import com.example.fangyi.newsclient.MainActivity;
import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.BaseMenuDetailPager;
import com.example.fangyi.newsclient.base.BasePager;
import com.example.fangyi.newsclient.base.menudetail.InteractMenuDetailPager;
import com.example.fangyi.newsclient.base.menudetail.NewsMenuDetailPager;
import com.example.fangyi.newsclient.base.menudetail.PhotoMenuDetailPager;
import com.example.fangyi.newsclient.base.menudetail.TopicMenuDetailPager;
import com.example.fangyi.newsclient.domain.NewsData;
import com.example.fangyi.newsclient.fragment.LeftMenuFragment;
import com.example.fangyi.newsclient.global.GlobalContants;
import com.example.fangyi.newsclient.utils.CacheUtils;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

import static com.example.fangyi.newsclient.global.GlobalContants.CATEGORIES_URL;


/**
 * 首页实现
 * Created by FANGYI on 2016/7/6.
 */

public class NewsCenterPager extends BasePager {
    private ArrayList<BaseMenuDetailPager> menuDetailPagers;//4个菜单详情页的集合
    private NewsData mNewsData;


    public NewsCenterPager(Activity Activity) {
        super(Activity);
    }

    @Override
    public void initData() {
        tvTitle.setText(R.string.ContentTitleText2);
        setSlidingMenuEnable(true);//开启侧边栏

        String cache = CacheUtils.getCache(GlobalContants.CATEGORIES_URL,
                mActivity);

        if (!TextUtils.isEmpty(cache)) {// 如果缓存存在,直接解析数据, 无需访问网路
            parseData(cache);
        }

        getDataFromServer();// 不管有没有缓存, 都获取最新数据, 保证数据最新

    }


    /**
     * 用来标志请求的what, 类似handler的what一样，这里用来区分请求
     */
    private static final int NOHTTP_WHAT_TEST = 0x001;

    /**
     * 从服务器获取数据
     */
    private void getDataFromServer() {
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(CATEGORIES_URL, RequestMethod.GET);
        requestQueue.add(NOHTTP_WHAT_TEST, request, onResponseListener);
    }

    /**
     * 回调对象，接受请求结果
     */
    private OnResponseListener<String> onResponseListener = new OnResponseListener<String>() {
        @Override
        public void onStart(int what) {
        }

        @SuppressWarnings("unused")
        @Override
        public void onSucceed(int what, Response<String> response) {
            if (what == NOHTTP_WHAT_TEST) {
                // 请求成功
                String result = response.get();// 响应结果
                parseData(result);
                // 设置缓存
                CacheUtils.setCache(CATEGORIES_URL,
                        result, mActivity);
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

        }

        @Override
        public void onFinish(int what) {
        }


    };

    /**
     * 解析网络数据
     *
     * @param result
     */
    private void parseData(String result) {
        Gson gson = new Gson();
        mNewsData = gson.fromJson(result, NewsData.class);

        //传递数据给NewsCenterPager.java
        //刷新侧边栏数据
        MainActivity mainUI = (MainActivity) mActivity;
        LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();
        leftMenuFragment.setMenuData(mNewsData);

        //准备4个菜单详情页
        menuDetailPagers = new ArrayList<>();
        menuDetailPagers.add(new NewsMenuDetailPager(mActivity, mNewsData.data.get(0).children));
        menuDetailPagers.add(new TopicMenuDetailPager(mActivity));
        menuDetailPagers.add(new PhotoMenuDetailPager(mActivity, btnPhoto));
        menuDetailPagers.add(new InteractMenuDetailPager(mActivity));

        //设置 菜单详情页-新闻 为默认当前页
        setCurrentMenuDetailPager(0);

    }

    /**
     * 设置当前菜单详情页
     */
    public void setCurrentMenuDetailPager(int position) {
        BaseMenuDetailPager pager = menuDetailPagers.get(position);// 获取当前要显示的菜单详情页
        flContent.removeAllViews();// 清除之前的布局

        flContent.addView(pager.mRootView);// 将菜单详情页的布局设置给帧布局
        tvTitle.setText(mNewsData.data.get(position).title);//设置当前页的标题
        pager.initData();//初始化当前页面的数据



        if (pager instanceof PhotoMenuDetailPager) {
            btnPhoto.setVisibility(View.VISIBLE);
        } else {
            btnPhoto.setVisibility(View.GONE);
        }
    }


}
