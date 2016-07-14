package com.example.fangyi.newsclient.base.menudetail;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fangyi.newsclient.R;
import com.example.fangyi.newsclient.base.BaseMenuDetailPager;
import com.example.fangyi.newsclient.domain.PhotosData;
import com.example.fangyi.newsclient.global.GlobalContants;
import com.example.fangyi.newsclient.utils.CacheUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.socks.library.KLog;
import com.yolanda.nohttp.NoHttp;
import com.yolanda.nohttp.RequestMethod;
import com.yolanda.nohttp.rest.OnResponseListener;
import com.yolanda.nohttp.rest.Request;
import com.yolanda.nohttp.rest.RequestQueue;
import com.yolanda.nohttp.rest.Response;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 菜单详情页 -- 图片
 * Created by FANGYI on 2016/7/8.
 */

public class PhotoMenuDetailPager extends BaseMenuDetailPager {
    private static final int NOHTTP_WHAT_TEST = 0x001;

    @BindView(R.id.xrv_photo)
    XRecyclerView xrvPhoto;

    private ImageButton btnPhoto;

    private PhotosData mPhotosData;
    private ArrayList<PhotosData.PhotoInfo> mPhotoList;
    private String mMoreUrl;
    private NormalRecyclerViewAdapter mAdapter;

    public PhotoMenuDetailPager(Activity mActivity, ImageButton btnPhoto) {
        super(mActivity);
        this.btnPhoto = btnPhoto;

        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDisplay();
                KLog.e("点击切换视图");
            }
        });
    }

    @Override
    public View initViews() {
        View view = View.inflate(mActivity, R.layout.menu_photo_pager, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void initData() {

        String cache = CacheUtils.getCache(GlobalContants.PHOTOS_URL, mActivity);
        if (!TextUtils.isEmpty(cache)) {

        }

        getDataFromServer();
    }

    public void getDataFromServer() {
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(GlobalContants.PHOTOS_URL, RequestMethod.GET);
        requestQueue.add(NOHTTP_WHAT_TEST, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                // 请求成功
                String result = response.get();// 响应结果
                parseData(result, false);//解析数据，并且不会下载下一页
                // 设置缓存
                CacheUtils.setCache(GlobalContants.PHOTOS_URL, result, mActivity);
                xrvPhoto.refreshComplete();
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
     * 从服务器获取下一页数据
     */
    private void getMoreDataFromServer() {
        RequestQueue requestQueue = NoHttp.newRequestQueue();
        // 创建请求对象
        Request<String> request = NoHttp.createStringRequest(GlobalContants.PHOTOS_URL, RequestMethod.GET);
        requestQueue.add(NOHTTP_WHAT_TEST, request, new OnResponseListener<String>() {
            @Override
            public void onStart(int what) {

            }

            @Override
            public void onSucceed(int what, Response<String> response) {
                // 请求成功
                String result = response.get();// 响应结果
                parseData(result, true);//加载下一页
                xrvPhoto.loadMoreComplete();
            }

            @Override
            public void onFailed(int what, String url, Object tag, Exception exception, int responseCode, long networkMillis) {
            }

            @Override
            public void onFinish(int what) {

            }
        });
    }



    protected void parseData(String result, boolean isMore) {
        Gson gson = new Gson();
        PhotosData data = gson.fromJson(result, PhotosData.class);
        //处理更多页面链接
//        setMoreUrl();
        // 获取组图列表集合
        mPhotoList = data.data.news;
//
//        if (mPhotoList != null) {
//
//            rvPhoto.setLayoutManager(new LinearLayoutManager(mActivity));
//            rvPhoto.setAdapter(new NormalRecyclerViewAdapter(mActivity));
//        }

//        if (!isMore) {
//
//
//            if (mTopNewsList != null) {
//
//            }
//
//            if (mPhotoList != null) {
//
//            }
//
//
//        } else {//如果是加载下一页，需要将数据追加给原来的集合
//            ArrayList<PhotosData.PhotoInfo> morePhotoList = data.data.news;
//            mPhotoList.addAll(morePhotoList);//第一页数据还在，追加第二页
//            mAdapter.notifyDataSetChanged();
//        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        xrvPhoto.setLayoutManager(layoutManager);
        mAdapter = new NormalRecyclerViewAdapter(mActivity);
        xrvPhoto.setAdapter(mAdapter);
        xrvPhoto.setArrowImageView(R.mipmap.ic_pulltorefresh_arrow);


        xrvPhoto.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                //refresh data here
                getDataFromServer();
            }

            @Override
            public void onLoadMore() {
                // load more data here
                getMoreDataFromServer();

            }
        });
    }


//    /**
//     * 处理更多页面链接
//     */
//    private void setMoreUrl() {
//        String more = mPhotosData.data.more;
//
//
//        if (!TextUtils.isEmpty(more)) {
//            mMoreUrl = GlobalContants.SERVER_URL + more;
//        } else {
//            mMoreUrl = null;
//        }
//    }




    public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder> {
        private final LayoutInflater mLayoutInflater;
        private final Context mContext;


        public NormalRecyclerViewAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new NormalTextViewHolder(mLayoutInflater.inflate(R.layout.recycler_photo_item, parent, false));
        }

        @Override
        public void onBindViewHolder(NormalTextViewHolder holder, int position) {
            holder.newsInfoDesc.setText(mPhotoList.get(position).title);

            String url = mPhotoList.get(position).listimage;
            Glide.with(mActivity)
                    .load(url)
                    .centerCrop()
                    .placeholder(R.mipmap.news_pic_default)
                    .crossFade()
                    .into(holder.newsInfoPhoto);
        }

        @Override
        public int getItemCount() {
            return mPhotoList.size();
        }

        public class NormalTextViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.news_info_photo)
            ImageView newsInfoPhoto;
            @BindView(R.id.news_info_desc)
            TextView newsInfoDesc;

            NormalTextViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("NormalTextViewHolder", "onClick--> position = " + getPosition());
                    }
                });
            }
        }
    }

    private boolean isListDisplay = true;// 是否是列表展示

    /**
     * 切换展现方式
     */
    private void changeDisplay() {

        xrvPhoto.setLayoutManager(new LinearLayoutManager(mActivity));

        if (isListDisplay) {
            isListDisplay = false;

            xrvPhoto.setLayoutManager(new GridLayoutManager(mActivity, 2));//这里用线性宫格显示 类似于grid view
            xrvPhoto.setAdapter(new NormalRecyclerViewAdapter(mActivity));

            btnPhoto.setImageResource(R.mipmap.icon_pic_list_type);

        } else {
            isListDisplay = true;

            xrvPhoto.setAdapter(new NormalRecyclerViewAdapter(mActivity));
            btnPhoto.setImageResource(R.mipmap.icon_pic_grid_type);
        }
    }

}
