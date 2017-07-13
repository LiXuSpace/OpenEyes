package com.qf.adapter.home;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qf.entity.HomeEntity;
import com.qf.eye.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by ken on 2017/3/27.
 * 精选页的第2种类型item
 */
public class Item2 implements ItemViewDelegate<HomeEntity> {

    private Context context;
    public Item2(Context context){
        this.context = context;
    }

    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_homeitem2;
    }

    @Override
    public boolean isForViewType(HomeEntity item, int position) {
        return item.getType().equals("lightTopicSection");
    }

    @Override
    public void convert(ViewHolder holder, HomeEntity homeEntity, int position) {

        //设置当前item最上面的那个图片
        ImageView ivBackground = holder.getView(R.id.iv_background);
        Glide
                .with(context.getApplicationContext())
                .load(homeEntity.getItemList().get(0).getData().getHeader().getCover())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(ivBackground);

        //设置横向的recyclerview
        RecyclerView recyclerView = holder.getView(R.id.recyclerview_h);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        List<HomeEntity.ItemListBean> list = homeEntity.getItemList().get(0).getData().getItemList();
        CommonAdapter<HomeEntity.ItemListBean> commonAdapter = new CommonAdapter<HomeEntity.ItemListBean>(context, R.layout.item_item2_layout, list.subList(0, list.size() - 1)) {//忽略最后一个数据结果
            @Override
            protected void convert(ViewHolder holder, HomeEntity.ItemListBean itemListBean, int position) {
                //绑定标题
                holder.setText(R.id.tv_title, itemListBean.getData().getTitle());
                //绑定副标题
                holder.setText(R.id.tv_type, itemListBean.getData().getType());

                //加载图片
                ImageView iv = holder.getView(R.id.iv_header);
                Glide
                        .with(context.getApplicationContext())
                        .load(itemListBean.getData().getCover().getFeed())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .thumbnail(0.1f)
                        .into(iv);
            }
        };
        recyclerView.setAdapter(commonAdapter);
    }
}
