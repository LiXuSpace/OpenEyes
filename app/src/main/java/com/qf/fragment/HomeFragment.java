package com.qf.fragment;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.qf.adapter.home.Item1;
import com.qf.adapter.home.Item2;
import com.qf.adapter.home.Item3;
import com.qf.entity.HomeEntity;
import com.qf.eye.DescActivity;
import com.qf.eye.R;
import com.qf.kenlibrary.base.BaseFragment;
import com.qf.kenlibrary.util.L;
import com.qf.util.Constants;
import com.qf.util.JSONUtil;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by ken on 2017/3/27.
 * 精选页
 */
public class HomeFragment extends BaseFragment implements Item1.OnItemClickListener<HomeEntity.ItemListBean>{

    @Bind(R.id.recyclerview)
    public RecyclerView recyclerView;
    //多布局适配器
    private MultiItemTypeAdapter<HomeEntity> adapter;

    //头部控件对象
    private View headView;
    //头部控件的背景对象
    private ImageView headViewImage;
    private ViewGroup.LayoutParams layoutParams;
    //头部控件的原始高度
    private int headViewHeight;
    //头部控件缩放以后的高度
    private int headViewScaleHeight;

    private List<HomeEntity> datas;

    @Override
    protected int getContentID() {
        return R.layout.fragment_home;
    }

    //开始滑动时手指的y坐标
    private int by = -1;
    @Override
    protected void initView(View view) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //头部对象
        headView = LayoutInflater.from(getActivity()).inflate(R.layout.view_header, recyclerView, false);
        //获得头部的背景控件
        headViewImage = (ImageView) headView.findViewById(R.id.iv_header_background);
        layoutParams = headViewImage.getLayoutParams();

        headView.measure(0, 0);//测量头部控件的高度
        headViewScaleHeight = headViewHeight = headView.getMeasuredHeight();//获得头部控件的高度
        L.d("头部控件的高度：" + headViewHeight);

        //实现recyclerview向上滚动，保持头部控件不动的效果
        //设置recyclerview的滚动监听
        recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                headView.setY(0);
            }
        });

        //实现精选页下拉头部缩放的效果
        //setOnTouchListener
        //该监听中的回调方法onTouch方法，会先于控件中的onTouchEvent执行
        //如果onTouch方法返回true，则控件中的onTouchEvent方法则就不执行了
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action){
                    case MotionEvent.ACTION_MOVE:
                        if(isTop()){
                            if(by == -1) {
                                by = (int) event.getY();
                            }
                            int ey = (int) event.getY();
                            int my = ey - by;//滚动中两次滑动的位置的差
                            by = ey;

                            //如果往上滑动任然将事件交给recyclerview处理
                            if(my < 0 && headViewScaleHeight <= headViewHeight){
                                return false;
                            }

                            //处理缩放
                            headViewScaleHeight += (my/3);//计算缩放的高度
                            if(headViewScaleHeight < headViewHeight){
                                headViewScaleHeight = headViewHeight;
                            }

                            //修改背景图片的高度
                            layoutParams.height = headViewScaleHeight;
                            headViewImage.setLayoutParams(layoutParams);
                            return true;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        //重置
                        by = -1;
                        reset();
                        headViewScaleHeight = headViewHeight;
                        break;
                }
                return false;
            }
        });
    }

    /**
     * 判断recyclerview是否滑动到了头部
     * @return
     */
    private boolean isTop(){
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if(layoutManager instanceof LinearLayoutManager){
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            //获得当前第一个显示的item下标
            int position = linearLayoutManager.findFirstVisibleItemPosition();
            View view = linearLayoutManager.findViewByPosition(position);
            if(position == 0 && view.getTop() == 0){
                return true;
            }
        }
        return false;
    }

    /**
     * 重置头部控件
     */
    private void reset(){
        if(headViewScaleHeight > headViewHeight) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(headViewScaleHeight, headViewHeight);
            valueAnimator.setDuration(500);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    layoutParams.height = (int) value;
                    headViewImage.setLayoutParams(layoutParams);
                }
            });
            valueAnimator.start();
        }
    }

    /**
     * 加载数据
     */
    @Override
    protected void loadData() {
        OkHttpUtils
                .get()
                .url(Constants.URL_HOME)
                .build()
                .execute(new Callback<List<HomeEntity>>(){
                    @Override
                    public List<HomeEntity> parseNetworkResponse(Response response, int id) throws Exception {
                        //该方法在子线程中执行
                        String json = response.body().string();
                        List<HomeEntity> homeEntities =  JSONUtil.getHomeEntitys(json);
                        return homeEntities;
                    }

                    @Override
                    public void onResponse(List<HomeEntity> datas, int id) {
                        HomeFragment.this.datas = datas;
                        //该方法在主线程中执行
                        adapter = new MultiItemTypeAdapter<>(getActivity(), datas);
                        //加载布局1
                        adapter.addItemViewDelegate(new Item1(getActivity(), HomeFragment.this));
                        //加载布局2
                        adapter.addItemViewDelegate(new Item2(getActivity()));
                        //加载布局3
                        adapter.addItemViewDelegate(new Item3(getActivity(), getChildFragmentManager()));

                        //创建一个可以添加头尾的适配器
                        HeaderAndFooterWrapper headerAdapter = new HeaderAndFooterWrapper(adapter);
                        //添加头部
                        headerAdapter.addHeaderView(headView);

                        recyclerView.setAdapter(headerAdapter);
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                });
    }

    /**
     * 事件点击方法
     * @param view
     * @param position
     * @param itemListBean
     */
    @Override
    public void onItemClick(View view, int position, HomeEntity.ItemListBean itemListBean) {
        Intent intent = new Intent(getActivity(), DescActivity.class);
        //将数据集合传递到activity中
        ArrayList<HomeEntity.ItemListBean> itemList = (ArrayList<HomeEntity.ItemListBean>)this.datas.get(0).getItemList();
        intent.putExtra("datas", itemList);
        //传递当前点击的下标
        intent.putExtra("position", position - 2);

        //跳转到指定的页面
        startActivity(intent);
    }
}
