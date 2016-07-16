package com.example.fangyi.newsclient.utils;

import android.content.Context;

/**
 * Created by FANGYI on 2016/7/16.
 */

public class DensityUtils {
    /**
     * dp转ox
     */
    public static int dp2px(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        int px = (int) (dp * density +0.5);
        return px;
    }

    public static float px2dp(Context context, float px) {
        float density = context.getResources().getDisplayMetrics().density;
        float dp = (int) (px/density);
        return dp;
    }
}
