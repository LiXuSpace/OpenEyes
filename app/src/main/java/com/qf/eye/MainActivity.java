package com.qf.eye;

import android.content.Intent;
import android.widget.RadioGroup;

import com.qf.fragment.DiscoverFragment;
import com.qf.fragment.HomeFragment;
import com.qf.kenlibrary.base.BaseActivity;

import butterknife.Bind;

/**
 * 首页
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.rg)
    public RadioGroup rg;

    @Override
    public int getContentID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //跳转到欢迎页
        startActivity(new Intent(this, WelcomeActivity.class));
    }

    @Override
    protected void bindListener() {
        rg.setOnCheckedChangeListener(this);
        rg.getChildAt(0).performClick();//模拟点击
    }

    /**
     * tab选项回调方法
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_home:
                //精选
                showFragment(R.id.fl_fragment, new HomeFragment());
                break;
            case R.id.rb_discover:
                //发现
                showFragment(R.id.fl_fragment, new DiscoverFragment());
                break;
            case R.id.rb_writer:
                //作者
                break;
            case R.id.rb_mime:
                //我的
                break;
        }
    }
}
