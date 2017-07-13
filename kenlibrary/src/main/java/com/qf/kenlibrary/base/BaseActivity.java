package com.qf.kenlibrary.base;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import butterknife.ButterKnife;

/**
 * Created by ken on 2017/3/27.
 *
 * 4.4以后提供一个沉浸式状态栏
 *
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected FragmentManager fragmentManager;
    protected Fragment showFragment;//当前正在显示的fragme


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentID());
        ButterKnife.bind(this);

        //获得fragment管理器
        fragmentManager = getSupportFragmentManager();

        initView();
        bindListener();
        loadDatas();

        //开启沉浸式状态栏
        if(isOpenStatus()) {
            Window window = getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置主窗体全屏
                window.setStatusBarColor(Color.TRANSPARENT);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }

    }

    protected abstract int getContentID();

    /**
    初始化控件
     */
    protected void initView(){}

    /**
     * 绑定监听
     */
    protected void bindListener(){};

    /**
     * 加载数据
     */
    protected void loadDatas(){};

    /**
     * 是否开启沉浸式状态栏
     * @return
     */
    protected boolean isOpenStatus(){
        return true;
    }


    /**
     * 显示fragment
     */
    public void showFragment(int layoutid, Fragment fragment){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(showFragment != null){
            //隐藏正在显示的fragment
            fragmentTransaction.hide(showFragment);
        }

        Fragment fragmentByTag = fragmentManager.findFragmentByTag(fragment.getClass().getName());
        if(fragmentByTag != null){
            //显示
            fragmentTransaction.show(fragmentByTag);
        } else {
            //新建
            fragmentByTag = fragment;
            fragmentTransaction.add(layoutid, fragmentByTag, fragmentByTag.getClass().getName());
        }
        fragmentTransaction.commit();
        showFragment = fragmentByTag;//当前显示的页面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
