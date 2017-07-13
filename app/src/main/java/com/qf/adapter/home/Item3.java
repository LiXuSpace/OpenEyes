package com.qf.adapter.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qf.entity.HomeEntity;
import com.qf.eye.R;
import com.qf.kenlibrary.base.BaseFragment;
import com.qf.kenlibrary.widget.looperviewpager.LoopViewPager;
import com.qf.kenlibrary.widget.tabview.TabView;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import butterknife.Bind;

/**
 * Created by ken on 2017/3/28.
 * 第三个包含viewpager的item
 */
public class Item3 implements ItemViewDelegate<HomeEntity> {


    private Context context;
    private FragmentManager fragmentManager;
    private Adapter adapter;
    private static int count = 1;

    public Item3(Context context, FragmentManager fragmentManager) {
        this.context = context;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_homeitem3;
    }

    @Override
    public boolean isForViewType(HomeEntity item, int position) {
        return item.getType().equals("categorySection");
    }

    @Override
    public void convert(ViewHolder holder, HomeEntity homeEntity, int position) {
        //设置标题
        holder.setText(R.id.tv_title, homeEntity.getItemList().get(0).getData().getHeader().getTitle());
        //设置副标题
        holder.setText(R.id.tv_desc, homeEntity.getItemList().get(0).getData().getHeader().getSubTitle());

        //设置viewpager
//        ViewPager vp = holder.getView(R.id.vp);
        FrameLayout fl = holder.getView(R.id.fl);
        ViewPager vp = (ViewPager) fl.getChildAt(0);
        vp.setId(count++);
        vp.setOffscreenPageLimit(5);

        adapter = new Adapter(fragmentManager, homeEntity.getItemList().get(0).getData().getItemList());
        vp.setAdapter(adapter);

        TabView tv = holder.getView(R.id.tabview);
        tv.setCount(adapter.getCount());
        tv.setViewPager(vp);
    }


    //FragmentStatePagerAdapter
    //FragmentPagerAdapter

    /**
     * Viewpager的适配器
     * FragmentPagerAdapter
     * FragmentStatePagerAdapter
     * 使用FragmentPagerAdapter，只是会销毁fragment的视图，fragment的对象不会被销毁
     * 使用FragmentStatePagerAdapter，会销毁fragment的视图和对象
     * <p>
     * FragmentPagerAdapter：如果一个viewpager页面不多，而且内容不会发生变化时，应该用这个适配器
     * FragmentStatePagerAdapter：如果viewpager页面很多，或者内容会频繁变化时，应该用这个适配器
     */
    private static class Adapter extends FragmentStatePagerAdapter {

        private List<HomeEntity.ItemListBean> datas;

        public Adapter(FragmentManager fm, List<HomeEntity.ItemListBean> datas) {
            super(fm);
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            position = LoopViewPager.toRealPosition(position, getCount());
            return Item3Fragment.getInstance(datas.get(position % getCount()));
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    /**
     * viewpager中的fragment对象
     */
    public static class Item3Fragment extends BaseFragment {

        @Bind(R.id.imageView)
        public ImageView iv;
        @Bind(R.id.title)
        public TextView tvTitle;
        @Bind(R.id.type)
        public TextView tvType;

        /**
         * 静态工厂方法
         *
         * @return
         */
        public static Item3Fragment getInstance(HomeEntity.ItemListBean itemBean) {
            Item3Fragment item3Fragment = new Item3Fragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("data", itemBean);
            item3Fragment.setArguments(bundle);
            return item3Fragment;
        }

        @Override
        protected int getContentID() {
            return R.layout.item_item3_layout;
        }

        @Override
        protected void bindData(Bundle bundle) {
            HomeEntity.ItemListBean itemListBean = (HomeEntity.ItemListBean) bundle.getSerializable("data");
            tvTitle.setText(itemListBean.getData().getTitle());
            tvType.setText(itemListBean.getData().getType());
            //加载图片
            Glide
                    .with(getActivity())
                    .load(itemListBean.getData().getCover().getFeed())
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.1f)
                    .into(iv);

        }
    }
}
