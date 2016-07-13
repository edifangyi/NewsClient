package com.example.fangyi.newsclient.base;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.domain.NewsData;
import com.example.fangyi.newsclient.domain.TabData;
import com.example.fangyi.newsclient.global.GlobalContants;
import com.example.fangyi.newsclient.view.RefreshListView;
import com.example.fangyi.newsclient.view.TopNewsRolldingViewPager;
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


    private RefreshListView lvNewsList;//新闻列表
    private TopNewsRolldingViewPager vpNeswRolling;//头条新闻ViewPager
    private TextView tvNewsTitle;//头条新闻标题
    private CircleIndicator ciNewsIndicator;//头条新闻VIewpager的指示点

    private String mUrl;
    private String mMoreUrl;//更多页面的地址

    private TabData mTabDetailData;

    private NewsData.NewsTabData mNewsTabData;//头条新闻的集合
    private ArrayList<TabData.TopNewsData> mTopNewsList;//头条新闻数据集合
    private ArrayList<TabData.TabNewsData> mNewsList;//新闻列表数据集合
    private NewsAdapter newsAdapter;

    public TabDetailPager(Activity mActivity, NewsData.NewsTabData newsTabData) {
        super(mActivity);
        mNewsTabData = newsTabData;
        mUrl = GlobalContants.SERVER_URL + mNewsTabData.url;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.tab_detail_pager, null);

        View headerView = View.inflate(mActivity, R.layout.list_header_topnews, null);//加载头布局

        lvNewsList = (RefreshListView) view.findViewById(R.id.lv_news_list);
        vpNeswRolling = (TopNewsRolldingViewPager) headerView.findViewById(R.id.vp_nesw_rolling);
        tvNewsTitle = (TextView) headerView.findViewById(R.id.tv_news_title);
        ciNewsIndicator = (CircleIndicator) headerView.findViewById(R.id.ci_news_indicator);

        lvNewsList.addHeaderView(headerView);//将头条新闻以头布局的形式加给ListView


        return view;
    }

    @Override
    public void initData() {
        getDataFromServer();

        //设置头条新闻标题监听
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

        //设置下拉刷新监听
        lvNewsList.setOnRefreshListener(new RefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                if (mMoreUrl != null) {
                    getMoreDataFromServer();
                } else {
                    Toast.makeText(mActivity, "最后一页了", Toast.LENGTH_SHORT).show();
                    lvNewsList.onRefreshCompleteOfFooter(true);//收起加载更多的布局
                }
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
                parseData(result, false);//解析数据，并且不会下载下一页
                lvNewsList.onRefreshComplete(true);//隐藏下拉刷新控件
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                lvNewsList.onRefreshComplete(false);//隐藏下拉刷新控件
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }

    /**
     * 从服务器获取下一页数据
     */
    private void getMoreDataFromServer() {
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(mMoreUrl, RequestMethod.GET);
        requestQueue.add(NOHTTP_WHAT_TEST, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                // 请求成功
                String result = response.get();// 响应结果
                parseData(result, true);//加载下一页
                lvNewsList.onRefreshCompleteOfFooter(true);//隐藏脚布局刷控件
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
                lvNewsList.onRefreshCompleteOfFooter(true);//隐藏脚布局刷控件
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
    public void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        mTabDetailData = gson.fromJson(result, TabData.class);

        //处理更多页面链接
        setMoreUrl();

        if (!isMore) {
            mTopNewsList = mTabDetailData.data.topnews;
            mNewsList = mTabDetailData.data.news;

            if (mTopNewsList != null) {
                tvNewsTitle.setText(mTopNewsList.get(0).title);//设置头条新闻标题

                vpNeswRolling.setAdapter(new TopNewsAdapter());
                ciNewsIndicator.setViewPager(vpNeswRolling);//头条新闻ViewPager指示器
            }

            if (mNewsList != null) {
                newsAdapter = new NewsAdapter();
                lvNewsList.setAdapter(newsAdapter);
            }
        } else {//如果是加载下一页，需要将数据追加给原来的集合
            ArrayList<TabData.TabNewsData> moreNews = mTabDetailData.data.news;
            mNewsList.addAll(moreNews);//第一页数据还在，追加第二页
            newsAdapter.notifyDataSetChanged();
        }

    }

    /**
     * 处理更多页面链接
     */
    private void setMoreUrl() {
        String more = mTabDetailData.data.more;

        if (!TextUtils.isEmpty(more)) {
            mMoreUrl = GlobalContants.SERVER_URL + more;
        } else {
            mMoreUrl = null;
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
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.list_news_item, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TabData.TabNewsData item = getItem(position);
            holder.tvNewsListTitle.setText(item.title);
            holder.tvNewsListDate.setText(item.pubdate);

            Glide.with(mActivity)
                    .load(item.listimage)
                    .centerCrop()
                    .placeholder(R.mipmap.pic_item_list_default)
                    .crossFade()
                    .into(holder.ivNewsPic);

            return convertView;
        }

    }

    static class ViewHolder {
        @BindView(R.id.iv_news_pic)
        ImageView ivNewsPic;
        @BindView(R.id.tv_news_list_title)
        TextView tvNewsListTitle;
        @BindView(R.id.tv_news_list_date)
        TextView tvNewsListDate;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
