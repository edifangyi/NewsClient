package com.example.fangyi.newsclient.fragment;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fangyi.newsclient.MainActivity;
import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.impl.NewsCenterPager;
import com.example.fangyi.newsclient.domain.NewsData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 侧边栏
 * Created by FANGYI on 2016/7/6.
 */

public class LeftMenuFragment extends BaseFragment {
    @BindView(R.id.lv_left_menu)
    ListView lvLeftMenu;

    private int mCurrentPos = 0;// 当前被点击的菜单项

    private ArrayList<NewsData.NewsMenuData> mMenuList;
    private MenuAdapter menuAdapter;

    @Override
    public View initViews() {

        View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {
        lvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCurrentPos = position;
                menuAdapter.notifyDataSetChanged();//刷新会重新跑一边getView方法，在里面把tvTitle换一下颜色

                setCurrentMenuDetailPager(position);
                
                toggleSlidingMenu();
            }
        });
    }
    /**
     * 切换SlidingMenu状态
     */
    private void toggleSlidingMenu() {
        MainActivity mainUI = (MainActivity) mActivity;
        SlidingMenu slidingMenu = mainUI.getSlidingMenu();
        slidingMenu.toggle();//切换状态，显示时隐藏，隐藏时显示
    }


    /**
     * 设置当前菜单详情页
     *
     * @param position
     */
    protected void setCurrentMenuDetailPager(int position) {
        MainActivity mainUi = (MainActivity) mActivity;
        ContentFragment fragment = mainUi.getContentFragment();// 获取主页面fragment
        NewsCenterPager pager = fragment.getNewsCenterPager();// 获取新闻中心页面
        pager.setCurrentMenuDetailPager(position);// 设置当前菜单详情页
    }

    /**
     * 侧边栏数据适配器
     */
    class MenuAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mMenuList.size();
        }

        @Override
        public NewsData.NewsMenuData getItem(int position) {
            return mMenuList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mActivity, R.layout.list_left_menu_item, null);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_left_menu_title);
            NewsData.NewsMenuData newsMenuData =  getItem(position);// getItem(int position) 的用法
            tvTitle.setText(newsMenuData.title);

            if (mCurrentPos == position) {
                //当前绘制的View是否为选中
                //显示红色
                tvTitle.setEnabled(true);
            }else {
                //显示白色
                tvTitle.setEnabled(false);
            }
            return view;
        }
    }


    /**
     * 设置网络数据
     */
    public void setMenuData(NewsData data) {
        mMenuList = data.data;
        menuAdapter = new MenuAdapter();
        lvLeftMenu.setAdapter(menuAdapter);
    }
}
