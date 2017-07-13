package com.qf.kenlibrary.util;

import android.util.Log;

/**
 * Created by ken on 2017/3/27.
 */

public class L {

    private static final String TAG = "print";
    private static final boolean OPEN = true;

    public static void d(String debug){
        if(OPEN) {
            Log.d(TAG, debug);
        }
    }

    public static void i(String info){
        if(OPEN) {
            Log.i(TAG, info);
        }
    }

    public static void e(String error){
        if(OPEN) {
            Log.e(TAG, error);
        }
    }
}
