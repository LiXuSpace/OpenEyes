package com.qf.eye;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.qf.entity.HomeEntity;
import com.qf.kenlibrary.base.BaseActivity;
import com.qf.kenlibrary.base.BaseFragment;
import com.qf.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by ken on 2017/3/28.
 * 视频展示页的activity
 */
public class DescActivity extends BaseActivity {

    @Bind(R.id.vp_videos)
    public ViewPager vpVideos;
    private Adapter adapter;

    private ArrayList<HomeEntity.ItemListBean> datas;

    @Override
    protected int getContentID() {
        return R.layout.activity_desc;
    }

    @Override
    protected void initView() {
        Intent intent = getIntent();

        //获得选中的position
        final int position = intent.getIntExtra("position", -1);
        vpVideos.post(new Runnable() {
            @Override
            public void run() {
                vpVideos.setCurrentItem(position);
            }
        });


        //获得页面显示的viewpager数据集合
        datas = (ArrayList<HomeEntity.ItemListBean>) intent.getSerializableExtra("datas");
        adapter = new Adapter(getSupportFragmentManager(), datas.subList(2, datas.size()));
        vpVideos.setAdapter(adapter);
    }


    /**
     * 适配器
     */
    private static class Adapter extends FragmentPagerAdapter{

        private List<HomeEntity.ItemListBean> datas;

        public Adapter(FragmentManager fm, List<HomeEntity.ItemListBean> datas) {
            super(fm);
            this.datas = datas;
        }

        @Override
        public Fragment getItem(int position) {
            return VideoFragment.getInstance(datas.get(position));
        }

        @Override
        public int getCount() {
            return datas.size();
        }
    }

    /**
     * ViewPager中的fragment
     */
    public static class VideoFragment extends BaseFragment {

        @Bind(R.id.iv_header)
        public ImageView ivHeader;
        @Bind(R.id.iv_background)
        public ImageView ivBackGround;
        @Bind(R.id.tv_title)
        public TextView tvTitle;
        @Bind(R.id.tv_type)
        public TextView tvType;
        @Bind(R.id.tv_content)
        public TextView tvContent;
        @Bind(R.id.tv_gz)
        public TextView tvgz;
        @Bind(R.id.tv_fx)
        public TextView tvfx;
        @Bind(R.id.tv_pl)
        public TextView tvpl;

        public static VideoFragment getInstance(HomeEntity.ItemListBean data) {
            VideoFragment vf = new VideoFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("datas", data);
            vf.setArguments(bundle);
            return vf;
        }

        @Override
        protected int getContentID() {
            return R.layout.fragment_video_viewpager;
        }

        @Override
        protected void bindData(Bundle bundle) {
            HomeEntity.ItemListBean data = (HomeEntity.ItemListBean) bundle.getSerializable("datas");
            tvTitle.setText(data.getData().getTitle());
            tvType.setText(data.getData().getType());
            tvgz.setText(data.getData().getConsumption().getCollectionCount() + "");
            tvfx.setText(data.getData().getConsumption().getShareCount() + "");
            tvpl.setText(data.getData().getConsumption().getReplyCount() + "");

            GlideUtil.downImg(getActivity(), data.getData().getCover().getFeed(), ivHeader);
            GlideUtil.downImg(getActivity(), data.getData().getCover().getBlurred(), ivBackGround);
        }
    }
}
