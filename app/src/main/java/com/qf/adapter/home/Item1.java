package com.qf.adapter.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.qf.entity.HomeEntity;
import com.qf.eye.R;
import com.qf.kenlibrary.util.L;
import com.qf.kenlibrary.util.ScreenUtil;
import com.zhy.adapter.recyclerview.base.ItemViewDelegate;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by ken on 2017/3/27.
 * 首页多布局中的线性布局类型
 */
public class Item1 implements ItemViewDelegate<HomeEntity>{

    private Context context;

    private OnItemClickListener onItemClickListener;

    public Item1(Context context, OnItemClickListener onItemClickListener){
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 当前这种布局的布局id
     * @return
     */
    @Override
    public int getItemViewLayoutId() {
        return R.layout.item_homeitem1;
    }

    /**
     * 判断数据源是否符合这种布局
     * @param item
     * @param position
     * @return
     */
    @Override
    public boolean isForViewType(HomeEntity item, int position) {
        return item.getType().equals("feedSection");
    }

    /**
     * 数据绑定
     * @param holder
     * @param homeEntity
     * @param position
     */
    @Override
    public void convert(ViewHolder holder, HomeEntity homeEntity, int position) {
        LinearLayout ll = holder.getView(R.id.ll_item1);

        /*
        获得数据列表
         */
        final List<HomeEntity.ItemListBean> itemList = homeEntity.getItemList();
        for(int index = 0; index < itemList.size(); index++){
            HomeEntity.ItemListBean item = itemList.get(index);
            String type = item.getType();
            View view = LayoutInflater.from(context).inflate(R.layout.item_item1_layout, ll, false);
            view.setTag(index);

            switch (type){
                case "banner2": {
                    //广告的图片
                    TextView tv = (TextView) view.findViewById(R.id.tv_title);
                    tv.setVisibility(View.GONE);
                    TextView tvtype = (TextView) view.findViewById(R.id.tv_type);
                    tvtype.setVisibility(View.GONE);

                    //设置图片
                    final ImageView imageView = (ImageView) view.findViewById(R.id.iv_background);
                    downImg(item.getData().getImage(), imageView);
                    break;
                }
                case "horizontalScrollCard": {
                    //gif广告
                    TextView tv = (TextView) view.findViewById(R.id.tv_title);
                    tv.setVisibility(View.GONE);
                    TextView tvtype = (TextView) view.findViewById(R.id.tv_type);
                    tvtype.setVisibility(View.GONE);

                    //设置图片
                    final ImageView imageView = (ImageView) view.findViewById(R.id.iv_background);
                    downImg(item.getData().getItemList().get(0).getData().getImage(), imageView);

                    break;
                }
                case "video": {
                    //视频类型
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(onItemClickListener != null){
                                int index = (int)v.getTag();
                                onItemClickListener.onItemClick(v, index, itemList.get(index));
                            }
                        }
                    });

                    //设置view中的数据
                    TextView tv = (TextView) view.findViewById(R.id.tv_title);
                    tv.setText(item.getData().getTitle());

                    //设置类型
                    TextView tvtype = (TextView) view.findViewById(R.id.tv_type);
                    tvtype.setText(item.getData().getType());

                    //设置图片
                    final ImageView imageView = (ImageView) view.findViewById(R.id.iv_background);
                    downImg(item.getData().getCover().getFeed(), imageView);
                    break;
                }
            }

            //动态添加进线性布局
            ll.addView(view);
        }
    }

    /**
     * 加载图片
     * @param url
     */
    private void downImg(String url, final ImageView imageView){
        Glide
                .with(context.getApplicationContext())
                .load(url)
                .asBitmap()//提醒glide当前下载的是bitmap
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)//预加载
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //通过比例计算图片应该有的高度
                        int bitmapW = resource.getWidth();
                        int bitmapH = resource.getHeight();
                        int h = (int) ((double) bitmapH / bitmapW * ScreenUtil.getScreenWidth(context));
                        L.d(bitmapW + "   " + bitmapH + "----->" + h);
                        //设置imageview的高度
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                        layoutParams.height = h;
                        imageView.setLayoutParams(layoutParams);
                        //设置imageview显示的图片
                        imageView.setImageBitmap(resource);
                    }
                });
    }

    public interface OnItemClickListener<T>{
        void onItemClick(View view, int position, T t);
    }
}
