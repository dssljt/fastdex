package com.example.colze.utils;

import android.util.Log;

/**
 * Created by tong on 14/12/21.
 */
public class Logger {
    public static boolean DEBUG = true;

    public static void d(String msg) {
        d(Logger.class.getSimpleName(),msg);
    }

    public static void d(String tag,String msg) {
        if (DEBUG) {
            Log.d(tag,msg);
        }
    }
}
