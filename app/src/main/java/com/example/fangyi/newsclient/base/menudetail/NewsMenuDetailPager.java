package com.example.fangyi.newsclient.base.menudetail;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.BaseMenuDetailPager;
import com.example.fangyi.newsclient.base.TabDetailPager;
import com.example.fangyi.newsclient.domain.NewsData;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.ButterKnife;

/**
 * 菜单详情页 -- 新闻
 * Created by FANGYI on 2016/7/8.
 */

public class NewsMenuDetailPager extends BaseMenuDetailPager {

    private ViewPager mViewPager;

    private ArrayList<TabDetailPager> mPagerList;

    private ArrayList<NewsData.NewsTabData> mNewsTabData;// 页签网络数据

    private SlidingTabLayout tabLayout_1;
    private ImageButton btnNext;
    private ImageButton btnFront;


    public NewsMenuDetailPager(Activity activity, ArrayList<NewsData.NewsTabData> children) {
        super(activity);
        mNewsTabData = children;
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.news_menu_derail, null);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_menu_detail);
        tabLayout_1 = (SlidingTabLayout) view.findViewById(R.id.tl_1);
        btnNext = (ImageButton) view.findViewById(R.id.btn_next);
        btnFront = (ImageButton) view.findViewById(R.id.btn_front);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        mPagerList = new ArrayList<>();

        // 初始化页签数据
        for (int i = 0; i < mNewsTabData.size(); i++) {
            TabDetailPager pager = new TabDetailPager(mActivity, mNewsTabData.get(i));
            mPagerList.add(pager);
        }

        mViewPager.setAdapter(new MenuDetailAdapter());
        tabLayout_1.setViewPager(mViewPager);//适配器，在mViewPager适配以后调用
        ChoicePager();


    }

    /**
     * 页面选择器跳转显示 - 控制箭头显示隐藏
     */
    private void ChoicePager() {
        //跳转上一个标签页
        btnFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = mViewPager.getCurrentItem();
                if (currentItem == 1) {
                    btnNext.setVisibility(View.VISIBLE);
                    btnFront.setVisibility(View.GONE);
                }

                if (currentItem == mPagerList.size() - 2) {
                    btnFront.setVisibility(View.VISIBLE);
                    btnNext.setVisibility(View.VISIBLE);
                }

                mViewPager.setCurrentItem(--currentItem);
            }
        });

        //跳转下一个标签页
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentItem = mViewPager.getCurrentItem();
                if (currentItem == 2) {
                    btnFront.setVisibility(View.VISIBLE);
                }

                if (currentItem == mPagerList.size() - 2) {
                    btnNext.setVisibility(View.GONE);
                }

                mViewPager.setCurrentItem(++currentItem);

            }
        });

        /**
         * ViewPager控制
         */
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {

                    case 0:
                        btnFront.setVisibility(View.GONE);
                        break;
                    case 1:
                        btnFront.setVisibility(View.GONE);
                        break;
                    case 2:
                        btnFront.setVisibility(View.GONE);
                        break;

                    default:
                        if (position == mPagerList.size() - 1) {
                            btnNext.setVisibility(View.GONE);
                        } else {
                            btnFront.setVisibility(View.VISIBLE);
                            btnNext.setVisibility(View.VISIBLE);
                        }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * SlidingTabLayout控制
         */
        tabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                switch (position) {

                    case 0:
                        btnFront.setVisibility(View.GONE);
                        break;
                    case 1:
                        btnFront.setVisibility(View.GONE);
                        break;
                    case 2:
                        btnFront.setVisibility(View.GONE);
                        break;

                    default:
                        if (position == mPagerList.size() - 1) {
                            btnNext.setVisibility(View.GONE);
                        } else {
                            btnFront.setVisibility(View.VISIBLE);
                            btnNext.setVisibility(View.VISIBLE);
                        }
                }
            }

            @Override
            public void onTabReselect(int position) {

            }
        });


    }

    class MenuDetailAdapter extends PagerAdapter {

        /**
         * 重写此方法，返回页面标题，用于SlidingTabLayout页签的显示
         *
         * @param position
         * @return
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsTabData.get(position).title;
        }

        @Override
        public int getCount() {
            return mPagerList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            TabDetailPager pager = mPagerList.get(position);
            container.addView(pager.mRootView);
            pager.initData();
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }


}