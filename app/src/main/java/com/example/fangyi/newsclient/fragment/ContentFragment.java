package com.example.fangyi.newsclient.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.BasePager;
import com.example.fangyi.newsclient.base.impl.GovAffairsPager;
import com.example.fangyi.newsclient.base.impl.HomePager;
import com.example.fangyi.newsclient.base.impl.NewsCenterPager;
import com.example.fangyi.newsclient.base.impl.SettingPager;
import com.example.fangyi.newsclient.base.impl.SmartServicePager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页内容
 * Created by FANGYI on 2016/7/6.
 */

public class ContentFragment extends BaseFragment {
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.rb_home)
    RadioButton rbHome;
    @BindView(R.id.rb_news)
    RadioButton rbNews;
    @BindView(R.id.rb_smart)
    RadioButton rbSmart;
    @BindView(R.id.rb_gov)
    RadioButton rbGov;
    @BindView(R.id.rb_setting)
    RadioButton rbSetting;
    @BindView(R.id.rg_group)
    RadioGroup rgGroup;

    private ArrayList<BasePager> mBasePagerList;

    /**
     * 初始化布局
     *
     * @return
     */
    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.fragment_content, null);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData() {

        //初始化5个子页面
        mBasePagerList = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            BasePager pager = new BasePager(mActivity);
//            mBasePagerList.add(pager);
//        }
        mBasePagerList.add(new HomePager(mActivity));
        mBasePagerList.add(new NewsCenterPager(mActivity));
        mBasePagerList.add(new SmartServicePager(mActivity));
        mBasePagerList.add(new GovAffairsPager(mActivity));
        mBasePagerList.add(new SettingPager(mActivity));

        vpContent.setAdapter(new ContentAdapter());


        setRadioGroup();//监听RadioGroup的选择事件
        setVpContentInitData();//获取当前被选中的页面，初始化当前数据
    }

    /**
     * 获取当前被选中的页面，初始化当前数据
     */
    private void setVpContentInitData() {

        mBasePagerList.get(0).initData();//初始化首页数据

        vpContent.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                mBasePagerList.get(position).initData();//获取当前被选中的页面，初始化当前数据
                //在setRadioGroup()的方法中实现初始化数据也可以，但是要写好几遍，看心情
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 监听RadioGroup的选择事件
     */
    private void setRadioGroup() {
        rgGroup.check(R.id.rb_home);//默认勾选首页

        rgGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_home:
//                        vpContent.setCurrentItem(0);//设置当前页面
                        vpContent.setCurrentItem(0, false);//设置当前页面，去掉切换页面的动画
                        break;
                    case R.id.rb_news:
//                        vpContent.setCurrentItem(1);//设置当前页面
                        vpContent.setCurrentItem(1, false);//设置当前页面，去掉切换页面的动画
                        break;
                    case R.id.rb_smart:
//                        vpContent.setCurrentItem(2);//设置当前页面
                        vpContent.setCurrentItem(2, false);//设置当前页面，去掉切换页面的动画
                        break;
                    case R.id.rb_gov:
//                        vpContent.setCurrentItem(3);//设置当前页面
                        vpContent.setCurrentItem(3, false);//设置当前页面，去掉切换页面的动画
                        break;
                    case R.id.rb_setting:
//                        vpContent.setCurrentItem(4);//设置当前页面
                        vpContent.setCurrentItem(4, false);//设置当前页面，去掉切换页面的动画
                        break;
                }
            }
        });

    }


    /**
     * vpContent适配器
     */
    private class ContentAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mBasePagerList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = mBasePagerList.get(position);
            container.addView(pager.mRootView);
//            pager.initData();//初始化数据...不要放在此处初始化数据，否则会预加载下一个页面
            return pager.mRootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * 获取新闻中心页面
     *
     * @return
     */
    public NewsCenterPager getNewsCenterPager() {
        return (NewsCenterPager) mBasePagerList.get(1);
    }

}
