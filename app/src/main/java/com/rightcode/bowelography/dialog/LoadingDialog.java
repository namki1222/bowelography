package com.rightcode.bowelography.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.view.View;
import android.widget.ProgressBar;

import androidx.core.content.ContextCompat;


import com.rightcode.bowelography.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoadingDialog extends Dialog {


    @BindView(R.id.dialog_background)
    View vBackground;
    @BindView(R.id.progress)
    ProgressBar pbProgress;

    public LoadingDialog(Context context) {
        super(context, R.style.Dialog_AppTheme_Transparent);
        setContentView(R.layout.dialog_loading);
        ButterKnife.bind(this);
    }

    @Override
    public void setCanceledOnTouchOutside(boolean cancel) {
        super.setCanceledOnTouchOutside(cancel);
        if (cancel) {
            vBackground.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            vBackground.setOnClickListener(null);
        }
    }

    public void setBackgroundResource(int resId) {
        vBackground.setBackgroundResource(resId);
    }

    public void setProgressColor(int resId) {
        int color = ContextCompat.getColor(getContext(), resId);
        pbProgress.getIndeterminateDrawable().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }
}
