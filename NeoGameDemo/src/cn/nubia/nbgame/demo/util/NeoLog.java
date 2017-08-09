package cn.nubia.nbgame.demo.util;

import android.util.Log;

/**
 * Created by liruchun on 2017/2/10.
 */
public class NeoLog {
    private static final String TAG = "NeoGame";

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void d(String tag, String message) {
        Log.d(TAG, formatMessage(tag, message));
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void i(String tag, String message) {
        Log.i(TAG, formatMessage(tag, message));
    }

    public static void e(String message) {
        Log.e(TAG, message);
    }

    public static void e(String tag, String message) {
        Log.e(TAG, formatMessage(tag, message));
    }

    private static String formatMessage(String customTag, String message) {
        return String.format("[%s] %s", customTag, message);
    }
}
