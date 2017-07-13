package com.qf.kenlibrary.widget.tabview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.qf.kenlibrary.R;
import com.qf.kenlibrary.widget.looperviewpager.LoopViewPager;

/**
 * Created by ken on 2017/3/28.
 * 配合viewpager滑动的tab圆点
 */
public class TabView extends FrameLayout{

    private LinearLayout ll;
    private int count;
    private int checkedImg;//被选中的图片样式
    private int unChecedImg;//未被选中

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        parseAttr(attrs);
    }


    /**
     *  初始化方法
     */
    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.layout_tabview, this, true);
        ll = (LinearLayout) findViewById(R.id.ll_tabview);
    }

    /**
     * 解析自定义属性
     */
    private void parseAttr(AttributeSet attrs) {
        TypedArray typedArray = getContext().getResources().obtainAttributes(attrs, R.styleable.TabView);
        checkedImg = typedArray.getResourceId(R.styleable.TabView_checked, R.drawable.ic_x_recycler_view_pager_indicator_focus);
        unChecedImg = typedArray.getResourceId(R.styleable.TabView_unchecked, R.drawable.ic_x_recycler_view_pager_indicator);
        typedArray.recycle();//回收资源
    }

    /**
     * 设置小圆点
     */
    public void setCount(int count){
        if(count > 0){
            ll.removeAllViews();//清空所有的圆点
            this.count = count;
            for (int i = 1; i <= count; i++){
                ImageView iv = new ImageView(getContext());
                if(i == 1) {
                    iv.setImageResource(checkedImg);
                    iv.setTag("checked");
                } else {
                    iv.setImageResource(unChecedImg);
                }
                ll.addView(iv);
            }
        }
    }

    /**
     * 设置需要级联的viewpager
     * @param vp
     */
    public void setViewPager(ViewPager vp){
        if(vp != null){
            vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    position = LoopViewPager.toRealPosition(position, count);

                    //将原来被选中的圆点设置为未被选中状态
                    ImageView iv = (ImageView) ll.findViewWithTag("checked");
                    iv.setImageResource(unChecedImg);
                    iv.setTag("");

                    //将position处的圆点设置为被选中的状态
                    ImageView iv2 = (ImageView) ll.getChildAt(position % count);
                    iv2.setImageResource(checkedImg);
                    iv2.setTag("checked");
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }
}
