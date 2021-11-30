package com.rightcode.bowelography.dialog;

import static androidx.fragment.app.DialogFragment.STYLE_NORMAL;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.ReportExtraActivity;
import com.rightcode.bowelography.adapter.CalendarDialogRecyclerViewAdapter;
import com.rightcode.bowelography.databinding.DialogDatePickerBinding;
import com.rightcode.bowelography.databinding.DialogNoPhotoBinding;
import com.rightcode.bowelography.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import lombok.Setter;

public class NoPhotoDialog extends BaseDialogFragment<DialogNoPhotoBinding> {

    @Setter
    NopicListener listener_no;
    public NoPhotoDialog(Context mContext) {
        super(mContext);
    }

    @Override
    public void initialize() {
        binding.tvCancel.setOnClickListener(view -> {
            listener_no.onSelect();
            dismiss();
        });

        binding.tvStart.setOnClickListener(view -> {
            dismiss();
        });
    }
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme);
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_no_photo, container, false);
        initialize();

        return binding.getRoot();
    }
    public interface NopicListener {
        void onSelect();
    }
}
