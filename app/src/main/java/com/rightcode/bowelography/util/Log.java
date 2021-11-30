package com.rightcode.bowelography.util;


import static com.rightcode.bowelography.Features.SHOW_LOG;

import com.rightcode.bowelography.Features;

public class Log {
    private static final String TAG = "rightCode-Tag";

    public static void v() {
        if(SHOW_LOG)
        android.util.Log.v(TAG, buildMessage(""));
    }

    public static void d() {
        if(SHOW_LOG)
        android.util.Log.d(TAG, buildMessage(""));
    }

    public static void i() {
        if(SHOW_LOG)
        android.util.Log.i(TAG, buildMessage(""));
    }

    public static void w() {
        if(SHOW_LOG)
        android.util.Log.w(TAG, buildMessage(""));
    }

    public static void e() {
        if(SHOW_LOG)
        android.util.Log.e(TAG, buildMessage(""));
    }

    public static void v(Object message) {
        if(SHOW_LOG)
        android.util.Log.v(TAG, buildMessage(null, message));
    }

    public static void d(Object message) {
        if(SHOW_LOG)
        android.util.Log.d(TAG, buildMessage(null, message));
    }

    public static void i(Object message) {
        if(SHOW_LOG)
        android.util.Log.i(TAG, buildMessage(null, message));
    }

    public static void w(Object message) {
        if(SHOW_LOG)
        android.util.Log.w(TAG, buildMessage(null, message));
    }

    public static void e(Object message) {
        if(SHOW_LOG)
        android.util.Log.e(TAG, buildMessage(null, message));
    }

    public static void d(String format, Object... message) {
        if (Features.TEST_ONLY && Features.SHOW_LOG) {
            android.util.Log.d(TAG, buildMessage(format, message));
        }
    }


    private static String buildMessage(String format, Object... message) {
        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[4];
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append(stackTraceElement.getFileName());
        sb.append(" ");
        sb.append(stackTraceElement.getLineNumber());

        if (format == null || format.isEmpty()) {
            if (message == null || message.length <= 0) {
                sb.append("] >> ");
                sb.append(stackTraceElement.getMethodName());
            } else if (message.length > 1) {
                sb.append("] ");
                sb.append(message);
            } else {
                sb.append("] ");
                sb.append(message[0]);
            }
        } else if (message == null || message.length <= 0) {
            sb.append(format);
        } else {
            sb.append("] ");
            sb.append(String.format(format, message));
        }

        return sb.toString();
    }
}
