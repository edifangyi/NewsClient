package com.example.fangyi.newsclient.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.domain.NewsData;
import com.example.fangyi.newsclient.domain.TabData;
import com.example.fangyi.newsclient.global.GlobalContants;
import com.google.gson.Gson;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

/**
 * 页签详情页
 * Created by FANGYI on 2016/7/9.
 */

public class TabDetailPager extends BaseMenuDetailPager {
    @BindView(R.id.vp_nesw_rolling)
    ViewPager vpNeswRolling;
    @BindView(R.id.lv_news_list)
    ListView lvNewsList;//新闻列表
    @BindView(R.id.tv_news_title)
    TextView tvNewsTitle;//头条新闻的标题
    @BindView(R.id.ci_news_indicator)
    CircleIndicator ciNewsIndicator;//头条新闻的ViewPager指示器

    private String mUrl;
    private TabData mTabDetailData;

    private NewsData.NewsTabData mNewsTabData;//头条新闻的集合
    private ArrayList<TabData.TopNewsData> mTopNewsList;//头条新闻数据集合
    private ArrayList<TabData.TabNewsData> mNewsList;//新闻列表数据集合

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

        vpNeswRolling.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tvNewsTitle.setText(mTopNewsList.get(position).title);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
        requestQueue.add(NOHTTP_WHAT_TEST, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                // 请求成功
                String result = response.get();// 响应结果
                parseData(result);
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {

            }

            @Override
            public void onFinish(int what) {

            }
        });
    }


    /**
     * 解析网络数据
     *
     * @param result
     */
    public void parseData(String result) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);

        mTopNewsList = mTabDetailData.data.topnews;
        mNewsList = mTabDetailData.data.news;

        if (mTopNewsList != null) {
            tvNewsTitle.setText(mTopNewsList.get(0).title);//设置头条新闻标题

            vpNeswRolling.setAdapter(new TopNewsAdapter());
            ciNewsIndicator.setViewPager(vpNeswRolling);//头条新闻ViewPager指示器
        }

        if (mNewsList != null) {
            lvNewsList.setAdapter(new NewsAdapter());

        }


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
            String url = mTopNewsList.get(position).topimage;
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


    /**
     * 新闻列表适配器
     */
    class NewsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mNewsList.size();
        }

        @Override
        public TabData.TabNewsData getItem(int position) {
            return mNewsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {

            }
            return null;
        }
    }


}
