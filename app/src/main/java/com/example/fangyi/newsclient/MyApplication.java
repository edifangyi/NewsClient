package com.example.fangyi.newsclient;

import android.app.Application;

import com.yolanda.nohttp.NoHttp;

/**
 * Created by FANGYI on 2016/7/8.
 */

public class MyApplication extends Application {
    private static Application _instance;


    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        NoHttp.init(this);

    }
}
