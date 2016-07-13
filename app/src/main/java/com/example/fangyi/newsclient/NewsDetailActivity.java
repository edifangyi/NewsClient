package com.example.fangyi.newsclient;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 新闻详情页
 * Created by FANGYI on 2016/7/13.
 */

public class NewsDetailActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_back)
    ImageButton btnBack;
    @BindView(R.id.btn_share)
    ImageButton btnShare;
    @BindView(R.id.btn_size)
    ImageButton btnSize;

    @BindView(R.id.wv_web)
    WebView wvWeb;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        ButterKnife.bind(this);


        String url = getIntent().getStringExtra("url");

        WebSettings settings = wvWeb.getSettings();
        settings.setJavaScriptEnabled(true);//表示支持js
        settings.setBuiltInZoomControls(true);//显示放大缩小按钮
        settings.setUseWideViewPort(true);//支持双击缩放

        wvWeb.setWebViewClient(new WebViewClient() {
            /**
             * 网页开始加载
             * @param view
             * @param url
             * @param favicon
             */
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pbProgress.setVisibility(View.VISIBLE);
            }

            /**
             * 网页加载结束
             * @param view
             * @param url
             */
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbProgress.setVisibility(View.GONE);
                KLog.e("执行关闭");
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                return super.shouldOverrideKeyEvent(view, event);
            }

            /**
             * 所有跳转的连接都会在此方法中回调
             * @param view
             * @param url
             * @return
             */
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);
//                return true;
                return super.shouldOverrideUrlLoading(view, url);
            }
        });

        wvWeb.setWebChromeClient(new WebChromeClient() {
            /**
             * 进度发生变化
             * @param view
             * @param newProgress
             */
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }


            /**
             * 获取网页标题
             * @param view
             * @param title
             */
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }
        });

        wvWeb.loadUrl(url);//加载网页


        btnBack.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnSize.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                break;
            case R.id.btn_share:
                showShare();
                break;
            case R.id.btn_size:
                showChooseDialog();
                break;
        }
    }


    private int mCurrentChooseItem;//记录当前选中的item,点击确定前
    private int mCurrentItem = 2;//默认选择,点击确定后

    /**
     * 设置字体 - 显示选择对话框
     */
    private void showChooseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String[] items = new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};
        builder.setTitle("字体设置");
        builder.setSingleChoiceItems(items, mCurrentItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCurrentChooseItem = which;

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                WebSettings settings = wvWeb.getSettings();
                switch (mCurrentChooseItem) {
                    case 0:
                        settings.setTextSize(WebSettings.TextSize.LARGEST);
//                        settings.setTextZoom();
                        break;
                    case 1:
                        settings.setTextSize(WebSettings.TextSize.LARGER);
                        break;
                    case 2:
                        settings.setTextSize(WebSettings.TextSize.NORMAL);
                        break;
                    case 3:
                        settings.setTextSize(WebSettings.TextSize.SMALLER);
                        break;
                    case 4:
                        settings.setTextSize(WebSettings.TextSize.SMALLEST);
                        break;

                    default:
                        break;
                }
                mCurrentItem = mCurrentChooseItem;

            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    /**
     * 分享，注意在sdcard根目录放text.jpg
     */
    private void showShare() {
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
//        oks.setTheme(OnekeyShareTheme.CLASSIC);//设置主题

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

// 启动分享GUI
        oks.show(this);
    }

}
