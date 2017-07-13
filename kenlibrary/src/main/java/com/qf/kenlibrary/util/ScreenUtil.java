package com.qf.kenlibrary.util;

import android.content.Context;

/**
 * Created by ken on 2017/3/27.
 */

public class ScreenUtil {

    /**
     * 获得屏幕的宽高
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }
}
