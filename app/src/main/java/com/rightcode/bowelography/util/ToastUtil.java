package com.rightcode.bowelography.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

import com.rightcode.bowelography.R;


public class ToastUtil {

    public static Toast show(Context context) {
        return show(context, context.getString(R.string.error_msg));
    }

    public static Toast show(Context context, int textResourceId) {
        return show(context, context.getString(textResourceId));
    }

    public static Toast show(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();
        return toast;
    }
}