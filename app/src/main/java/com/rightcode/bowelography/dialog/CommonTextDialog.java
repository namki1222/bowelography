package com.rightcode.bowelography.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.rightcode.bowelography.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import lombok.Setter;

public class CommonTextDialog extends DialogFragment {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_negative)
    TextView tvNegative;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.tv_positive)
    TextView tvPositive;

    Context mContext;

    @Setter
    String title;
    @Setter
    String message;

    String negativeMessage;
    String positiveMessage;

    View.OnClickListener positiveListener;
    View.OnClickListener negativeListener;

    public CommonTextDialog(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(getContext(), R.layout.dialog_common_text, null);

        builder.setView(view);
        ButterKnife.bind(this, view);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        initialize();

        return dialog;
    }

    private void initialize() {
        if (title != null) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        if (message != null) {
            tvContent.setVisibility(View.VISIBLE);
            tvContent.setText(message);
        }
        if (negativeMessage != null && negativeListener == null) {
            tvNegative.setText(negativeMessage);
            tvNegative.setOnClickListener(v -> dismiss());
            tvNegative.setVisibility(View.VISIBLE);
        } else if (negativeMessage != null && negativeListener != null) {
            tvNegative.setText(negativeMessage);
            tvNegative.setOnClickListener(negativeListener);
            tvNegative.setVisibility(View.VISIBLE);
        }
        if (positiveMessage != null && positiveListener == null) {
            tvPositive.setText(positiveMessage);
            tvPositive.setOnClickListener(v -> dismiss());
            tvPositive.setVisibility(View.VISIBLE);
        } else if (positiveListener != null && positiveListener != null) {
            tvPositive.setText(positiveMessage);
            tvPositive.setOnClickListener(positiveListener);
            tvPositive.setVisibility(View.VISIBLE);
        }

        if (positiveMessage != null && negativeMessage != null) {
            divider.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        resize();
        super.onResume();
    }
    public void show() {
        FragmentManager fm;
        if (mContext instanceof FragmentActivity) {
            fm = ((FragmentActivity) mContext).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(this, "null");
            ft.commitAllowingStateLoss();
        }
    }

    public void setNegativeButton(String text, View.OnClickListener listener) {
        negativeMessage = text;
        negativeListener = listener;
    }

    public void setNegativeButton(String text) {
        negativeMessage = text;
    }

    public void setPositiveButton(String text) {
        positiveMessage = text;
    }

    public void setPositiveButton(String text, View.OnClickListener listener) {
        positiveMessage = text;
        positiveListener = listener;
    }

    private void resize() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        int deviceWidth = size.x;
        params.width = (int) (deviceWidth * 0.8);

        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
    }

}
