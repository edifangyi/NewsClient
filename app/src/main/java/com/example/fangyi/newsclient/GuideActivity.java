package com.example.fangyi.newsclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.fangyi.newsclient.utils.PrefUtils;

import java.util.ArrayList;

/**
 * Created by FANGYI on 2016/7/5.
 */

public class GuideActivity extends AppCompatActivity {

    private static final int[] mGuideImageIds = new int[]{R.mipmap.guide_1, R.mipmap.guide_2, R.mipmap.guide_3};

    private ViewPager vpGuide;
    private Button btnGuide;
    private View viewRedPoint;//小红点
    private LinearLayout llPointGroup;//引导圆点的父控件
    private ArrayList<ImageView> mImageViewList;
    private int mPointsWidet;//两个圆点之间的距离

    private AlphaAnimation aa;
    private boolean tagBtnAnim = true;//标记按钮动画，渐变显示和退出效果

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        assignViews();

        initView();//初始化界面

        vpGuide.setAdapter(new GuideAdapter());

        setOnPageChangeListener();//ViewPager的滑动监听

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跟新sp,表示已经展示了新手引导
                PrefUtils.setBoolean(GuideActivity.this, "is_user_guide_showed", true);
                //跳转主页面
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
            }
        });
    }

    /**
     * 初始化指定控件
     */
    private void assignViews() {
        vpGuide = (ViewPager) findViewById(R.id.vp_guide);
        btnGuide = (Button) findViewById(R.id.btn_guide);
        llPointGroup = (LinearLayout) findViewById(R.id.ll_point_group);
        viewRedPoint = findViewById(R.id.view_red_point);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mImageViewList = new ArrayList<>();

        //初始化引导页的3个页面
        initGuideImageView();

        //初始化引导页的小圆点
        initPoints();

        //获取两个圆点的左侧之间边距
        getPointsWidet();
    }

    /**
     * 初始化引导页的3个页面
     */
    private void initGuideImageView() {
        for (int i = 0; i < mGuideImageIds.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setBackgroundResource(mGuideImageIds[i]);
            mImageViewList.add(imageView);
        }
    }

    /**
     * 初始化引导页的小圆点
     */
    private void initPoints() {
        for (int i = 0; i < mGuideImageIds.length; i++) {
            View point = new View(this);
            point.setBackgroundResource(R.drawable.shape_point_gray);//设置引导页默认圆点

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            if (i > 0) {
                params.leftMargin = 10;//设置圆点间隔
            }

            point.setLayoutParams(params);//设置圆点大小

            llPointGroup.addView(point);//将圆点添加给线性布局
        }
    }

    /**
     * 获取两个圆点的左侧之间边距
     */
    private void getPointsWidet() {
        //当onCreate()执行完以后，才执行measure(测量大小) layout(界面位置) ondraw(绘制)

        //获取视图树，对layout结束事件进行监听
        llPointGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            //当layout执行结束后回调此方法
            @Override
            public void onGlobalLayout() {
                //删除后续回调
                llPointGroup.getViewTreeObserver().removeOnGlobalLayoutListener(this);//API16以上可以使用
                //两个小圆点左边界之间的距离
                mPointsWidet = llPointGroup.getChildAt(1).getLeft() - llPointGroup.getChildAt(0).getLeft();

            }
        });
    }

    /**
     * ViewPager 的适配器
     */
    private class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mGuideImageIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViewList.get(position));
            return mImageViewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    /**
     * ViewPager的滑动监听
     */
    private void setOnPageChangeListener() {
        vpGuide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动事件 当前位置，百分比，移动距离
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setPointMove(position, positionOffset);//设置小红点移动
            }

            //某个页面被选中
            @Override
            public void onPageSelected(int position) {
                setBtnGuideVisibility(position);//设置 开始体验 按钮 的显示隐藏
            }

            //滑动转换台发生变化
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    /**
     * 设置 开始体验 按钮 的显示隐藏
     *
     * @param position
     */
    private void setBtnGuideVisibility(int position) {

        if (position == mGuideImageIds.length - 1) {
            setBtnAlphaIn();

        } else if (position == mGuideImageIds.length - 2) {

            if (tagBtnAnim) {
                btnGuide.setVisibility(View.INVISIBLE);

            } else {
                setBtnAlphaOut();
            }

        } else {
            btnGuide.setVisibility(View.INVISIBLE);

        }
    }

    /**
     * 设置按钮渐变进入
     */
    private void setBtnAlphaIn() {
        aa = new AlphaAnimation(0, 1);
        btnAnimationCurrency(aa, View.VISIBLE);
        tagBtnAnim = false;//设置标记点
    }

    /**
     * 设置按钮渐变退出
     */
    private void setBtnAlphaOut() {
        aa = new AlphaAnimation(1, 0);
        btnAnimationCurrency(aa, View.INVISIBLE);
        tagBtnAnim = true;//设置标记点
    }

    /**
     * btn动画通用属性
     * @param aa
     * @param visibility
     */
    private void btnAnimationCurrency(AlphaAnimation aa, int visibility) {
        aa.setDuration(500);
        aa.setFillAfter(true);
        btnGuide.startAnimation(aa);
        btnGuide.setVisibility(visibility);
    }


    /**
     * 通过ViewPager的滑动监听，设置小红点移动
     *
     * @param position
     * @param positionOffset
     */
    private void setPointMove(int position, float positionOffset) {
        int len = (int) (mPointsWidet * positionOffset) + position * mPointsWidet;//圆点移动
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewRedPoint.getLayoutParams();//获取的那个钱红点的布局参数
        params.leftMargin = len;//设置左边距
        viewRedPoint.setLayoutParams(params);//重新给小红点设置布局参数
    }

}
