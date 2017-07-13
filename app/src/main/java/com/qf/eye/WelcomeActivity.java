package com.qf.eye;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.TextView;

import com.qf.kenlibrary.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by ken on 2017/3/27.
 * 欢迎页
 */
public class WelcomeActivity extends BaseActivity {

    @Bind(R.id.iv_background)
    public ImageView ivBackGround;
    @Bind(R.id.tv_title)
    public TextView tvTitle;


    @Override
    protected int getContentID() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void initView() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), "font/Lobster-1.4.otf");
        tvTitle.setTypeface(typeface);//设置文本的字体

        //设置背景动画
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1.0f, 1.3f);
//        valueAnimator.setDuration(3000);
//        valueAnimator.setInterpolator(new LinearInterpolator());//线性加速器
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float f = (float) animation.getAnimatedValue();
//                ivBackGround.setScaleX(f);
//                ivBackGround.setScaleY(f);
//            }
//        });
//        valueAnimator.addListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                finish();
//            }
//        });
//        valueAnimator.start();

        //背景动画
        ivBackGround.animate()
                .scaleXBy(1)
                .scaleX(1.1f)
                .scaleYBy(1)
                .scaleY(1.1f)
                .setDuration(2000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        finish();
                    }
                }).start();

    }
}
