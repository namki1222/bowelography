package com.rightcode.bowelography.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewbinding.ViewBinding;

import com.rightcode.bowelography.activity.BaseActivity;


public abstract class BaseDialogFragment<T extends ViewBinding> extends DialogFragment {

    protected Context mContext;

    protected T binding;

    protected double ratio = 0.8;

    public BaseDialogFragment(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onResume() {
        super.onResume();
        resize();
    }

    public abstract void initialize();

    public void resize() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        int deviceWidth = size.x;
        params.width = (int) (deviceWidth * ratio);

        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

    public Dialog createDialog(T binding) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = binding.getRoot();

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initialize();

        return dialog;
    }

    public void startActivity(Intent intent) {
        if (mContext instanceof BaseActivity) {
            ((BaseActivity) mContext).startActivity(intent);
        } else {
            mContext.startActivity(intent);
        }
    }

    public void show() {
        FragmentManager fm;
        if (mContext instanceof FragmentActivity) {
            fm = ((FragmentActivity) mContext).getSupportFragmentManager();
            show(fm, "null");
        }
    }
}
