package com.gopher.meidcalcollection;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.gopher.meidcalcollection.base.BaseActivity;

/**
 * Created by Administrator on 2017/11/16.
 */

public class Launcher extends BaseActivity {
    @Override
    public int bindLayout() {
        return R.layout.activity_launcher;
    }

    @Override
    public View bindView() {
        return null;
    }

    @Override
    public void initParms(Bundle parms) {

    }

    @Override
    public void initView(View view) {

        //添加动画效果
        AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
        animation.setDuration(2000);
        animation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //跳转界面
                getOperation().forward(MainActivity.class);
                finish();
                //右往左推出效果
                //				overridePendingTransition(R.anim.push_left_in,R.anim.push_left_out);
                //转动淡出效果1
                overridePendingTransition(R.anim.scale_rotate_in, R.anim.alpha_out);
                //下往上推出效果
                //				overridePendingTransition(R.anim.push_up_in,R.anim.push_up_out);
            }
        });
        view.setAnimation(animation);
    }

    @Override
    public void doBusiness(Context mContext) {

    }

    @Override
    public void resume() {

    }

    @Override
    public void destroy() {

    }
}
