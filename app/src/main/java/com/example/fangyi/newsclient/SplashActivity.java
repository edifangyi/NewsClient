package com.example.fangyi.newsclient;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.example.fangyi.newsclient.utils.PrefUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FANGYI on 2016/7/5.
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.activity_splash)
    RelativeLayout activitySplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        starAnim();
    }

    /**
     * 启动动画
     */
    private void starAnim() {
        AnimationSet as = new AnimationSet(false);

        //旋转动画
        RotateAnimation ra = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);//动画时间
        ra.setFillAfter(true);//保持动画状态

        //缩放动画
        ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(1000);//动画时间
        sa.setFillAfter(true);//保持动画状态

        //渐变动画
        AlphaAnimation aa = new AlphaAnimation(0, 1);
        aa.setDuration(1000);//动画时间
        aa.setFillAfter(true);//保持动画状态

        as.addAnimation(ra);
        as.addAnimation(sa);
        as.addAnimation(aa);

        //设置动画监听
        as.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            //动画执行结束
            @Override
            public void onAnimationEnd(Animation animation) {
                jumpNextPage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        activitySplash.startAnimation(as);
    }

    /**
     * 跳转下一个页面
     */
    private void jumpNextPage() {
        boolean userGuide = PrefUtils.getBoolean(this, "is_user_guide_showed", false);
        if (!userGuide) {
            //跳转到新手引导页
            startActivity(new Intent(SplashActivity.this, GuideActivity.class));
        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        }
        finish();

    }


}
