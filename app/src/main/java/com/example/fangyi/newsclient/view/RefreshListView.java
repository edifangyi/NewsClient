package com.example.fangyi.newsclient.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fangyi.newsclient.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FANGYI on 2016/7/12.
 */

public class RefreshListView extends ListView implements AbsListView.OnScrollListener {

    private static final int STATE_PULL_REFRESH = 0;// 下拉刷新
    private static final int STATE_RELEASE_REFRESH = 1;// 松开刷新
    private static final int STATE_REFRESHING = 2;// 正在刷新

    /**
     * 头布局id
     */
    @BindView(R.id.iv_refresh_down)
    ImageView ivRefreshDown;
    @BindView(R.id.pb_refresh)
    ProgressBar pbRefresh;
    @BindView(R.id.tv_refresh_title)
    TextView tvRefreshTitle;
    @BindView(R.id.tv_refresh_time)
    TextView tvRefreshTime;

    private View mHeaderView;
    private int mHeaderViewHeight;//头布局高度
    private int startY = -1;//滑动起点的Y坐标
    private int endY;

    private int mCurrrentState = STATE_PULL_REFRESH;// 当前状态

    private RotateAnimation animUp;
    private RotateAnimation animDown;


    private View mFooterView;
    private int mFooterViewHeight;//脚布局高度


    public RefreshListView(Context context) {
        super(context);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initHeaderView();
        initFooterView();
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    /**
     * 初始化头布局
     */
    private void initHeaderView() {
        mHeaderView = View.inflate(getContext(), R.layout.refresh_header, null);
        ButterKnife.bind(this, mHeaderView);
        this.addHeaderView(mHeaderView);

        mHeaderView.measure(0, 0);
        mHeaderViewHeight = mHeaderView.getMeasuredHeight();
        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏刷新头布局
        initArrowAnim();

        tvRefreshTime.setText("最后刷新时间" + getCurrentTime());//最后刷新时间

    }

    /**
     * 初始化脚布局
     */
    private void initFooterView() {
        mFooterView = View.inflate(getContext(), R.layout.refresh_listview_footer, null);
        this.addFooterView(mFooterView);

        mFooterView.measure(0, 0);
        mFooterViewHeight = mFooterView.getMeasuredHeight();
        mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);//隐藏
        this.setOnScrollListener(this);

    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startY == -1) {//确保startY有效
                    startY = (int) ev.getRawY();
                }

                if (mCurrrentState == STATE_REFRESHING) {//正在刷新时不作处理
                    break;

                }
                endY = (int) ev.getRawY();

                int dy = endY - startY;//移动偏移量
                if (dy > 0 && getFirstVisiblePosition() == 0) {//只有下拉并且当前是第一个item，才允许下拉
                    int padding = dy - mHeaderViewHeight;//计算padding
                    mHeaderView.setPadding(0, padding, 0, 0);

                    if (padding > 0 && mCurrrentState != STATE_RELEASE_REFRESH) {//状态改为松开刷新
                        mCurrrentState = STATE_RELEASE_REFRESH;
                        refreshState();
                    } else if (padding <= 0 && mCurrrentState != STATE_PULL_REFRESH) {//改为下拉刷新状态
                        mCurrrentState = STATE_PULL_REFRESH;
                    }

                    return true;
                }

                break;
            case MotionEvent.ACTION_UP:
                startY = -1;//重置
                if (mCurrrentState == STATE_RELEASE_REFRESH) {
                    mCurrrentState = STATE_REFRESHING;//正在刷新
                    mHeaderView.setPadding(0, 0, 0, 0);//显示

                    refreshState();
                } else if (mCurrrentState == STATE_PULL_REFRESH) {
                    mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 刷新下拉控件的布局
     */
    private void refreshState() {
        switch (mCurrrentState) {
            case STATE_PULL_REFRESH:
                tvRefreshTitle.setText("下拉刷新");
                ivRefreshDown.setVisibility(View.VISIBLE);
                pbRefresh.setVisibility(View.INVISIBLE);
                ivRefreshDown.startAnimation(animDown);
                break;
            case STATE_RELEASE_REFRESH:
                tvRefreshTitle.setText("松开刷新");
                ivRefreshDown.setVisibility(View.VISIBLE);
                pbRefresh.setVisibility(View.INVISIBLE);
                ivRefreshDown.startAnimation(animUp);
                break;
            case STATE_REFRESHING:
                tvRefreshTitle.setText("正在刷新...");
                ivRefreshDown.clearAnimation();//必须先清除动画，才可以隐藏
                ivRefreshDown.setVisibility(View.INVISIBLE);
                pbRefresh.setVisibility(View.VISIBLE);

                if (mListener != null) {
                    mListener.onRefresh();
                }

                break;
            default:
                break;
        }
    }

    /**
     * 初始化箭头动画
     */
    private void initArrowAnim() {
        // 箭头向上动画
        animUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animUp.setDuration(200);
        animUp.setFillAfter(true);

        // 箭头向下动画
        animDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animDown.setDuration(200);
        animDown.setFillAfter(true);
    }


    /**
     * 定义接口
     */
    OnRefreshListener mListener;

    /**
     * 设置接口监听
     */
    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public interface OnRefreshListener {
        public void onRefresh();

        public void onLoadMore();// 加载下一页数据
    }


    /**
     * 收起下拉刷新的控件
     */
    public void onRefreshComplete(boolean success) {
        mCurrrentState = STATE_PULL_REFRESH;
        tvRefreshTitle.setText("下拉刷新");
        ivRefreshDown.setVisibility(View.VISIBLE);
        pbRefresh.setVisibility(View.INVISIBLE);

        mHeaderView.setPadding(0, -mHeaderViewHeight, 0, 0);//隐藏
        if (success) {
            tvRefreshTime.setText("最后刷新时间" + getCurrentTime());//最后刷新时间
        } else {
            Toast.makeText(getContext(), "刷新失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 收起脚布局控件
     */
    public void onRefreshCompleteOfFooter(boolean success) {
        if (success) {//正在加载更多
            mFooterView.setPadding(0, -mFooterViewHeight, 0, 0);//隐藏脚布局
            isLoadingMore = false;
        }
    }

    /**
     * 获取当前时间
     */
    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        return format.format(new Date());
    }


    private boolean isLoadingMore;

    /**
     * 监听滚动状态改变
     */
    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //滚动状态闲置 || 滚动状态扔
        if (scrollState == SCROLL_STATE_IDLE
                || scrollState == SCROLL_STATE_FLING) {

            if (getLastVisiblePosition() == getCount() - 1 && !isLoadingMore) {// 滑动到最后
                System.out.println("到底了.....");
                mFooterView.setPadding(0, 0, 0, 0);// 显示
                setSelection(getCount() - 1);// 改变listview显示位置

                isLoadingMore = true;

                if (mListener != null) {
                    mListener.onLoadMore();
                }
            }
        }
    }

    /**
     * 在滚动
     */
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
