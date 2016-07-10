package com.example.fangyi.newsclient.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.domain.NewsData;
import com.example.fangyi.newsclient.domain.TabData;
import com.example.fangyi.newsclient.global.GlobalContants;
import com.google.gson.Gson;
import com.socks.library.KLog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 页签详情页
 * Created by FANGYI on 2016/7/9.
 */

public class TabDetailPager extends BaseMenuDetailPager {
    @BindView(R.id.vp_nesw_rolling)
    ViewPager vpNeswRolling;
    @BindView(R.id.lv_news_list)
    ListView lvNewsList;
    private NewsData.NewsTabData mNewsTabData;
    private String mUrl;
    private TabData mTabDetailData;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData newsTabData) {
        super(mActivity);
        mNewsTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mNewsTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();
        //没封装成功
//        NohttpUtils nohttpUtils = new NohttpUtils();
//        nohttpUtils.getDataFromServer(mUrl);
//        String result = nohttpUtils.getResult();
//        parseData(result);
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
        Request<String> request = NoHttp.createStringRequest(mUrl, RequestMethod.GET);
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
            }
        }

        @Override
        public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            Toast.makeText(mActivity, "请求失败", Toast.LENGTH_SHORT).show();
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
    public void parseData(String result) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);
        KLog.e("mTabDetailData ==" + mTabDetailData);
        vpNeswRolling.setAdapter(new TopNewsAdapter());
    }

    /**
     * 头条新闻适配器
     */
    class TopNewsAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return mTabDetailData.data.topnews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView image = new ImageView(mActivity);
            image.setScaleType(ImageView.ScaleType.FIT_XY);//基于控件大小填充图片

            //Glide图片加载
            String url = mTabDetailData.data.topnews.get(position).topimage;
            Glide.with(mActivity)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.mipmap.news_pic_default)
                    .crossFade()
                    .into(image);
            container.addView(image);
            return image;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}
