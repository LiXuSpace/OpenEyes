package com.qf.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by ken on 2017/3/28.
 */

public class GlideUtil {

    public static void downImg(Context context, String url, ImageView iv){
        Glide
                .with(context.getApplicationContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.1f)
                .into(iv);
    }
}
