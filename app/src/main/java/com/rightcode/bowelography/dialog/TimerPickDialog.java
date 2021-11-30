package com.rightcode.bowelography.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.DialogTimerPickerBinding;
import com.rightcode.bowelography.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Setter;

public class TimerPickDialog extends BottomSheetDialogFragment {

    Context mContext;
    DialogTimerPickerBinding binding;

    @Setter
    OnTimerPickListener listener;

    String type;
    int hour = 12;
    int min = 00;

    public TimerPickDialog(Context mContext) {
        this.mContext = mContext;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_timer_picker, container, false);

        initialize();
        return binding.getRoot();
    }

    private void initialize() {
        if (type == null) {
            type = "AM";
        }
        binding.numberPickerType.setMinValue(0);
        binding.numberPickerType.setMaxValue(1);
        binding.numberPickerType.setDisplayedValues(new String[] {"AM", "PM"});

        binding.numberPickerHour.setMinValue(1);
        binding.numberPickerHour.setMaxValue(12);

        binding.numberPickerMinute.setMinValue(0);
        binding.numberPickerMinute.setMaxValue(59);

        binding.tvCancel.setOnClickListener(view -> {
            dismiss();
        });

        binding.tvConfirm.setOnClickListener(view -> {
            listener.onPick(calDate());
            dismiss();
        });

        binding.numberPickerHour.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
            hour = newValue;
        });

        binding.numberPickerMinute.setOnValueChangedListener((numberPicker, oldValue, newValue) -> {
            min = newValue;
        });

        binding.numberPickerType.setOnValueChangedListener((numberPicker, i, i1) -> {
            if (i1 == 0) {
                type = "AM";
            } else {
                type = "PM";
            }
        });

        int _type = type.equals("AM") ? 0 : 1;
        binding.numberPickerType.setValue(_type);
        binding.numberPickerHour.setValue(hour);
        binding.numberPickerMinute.setValue(min);
    }

    private String calDate() {
            if (type.equals("PM")) {
                if (hour < 12) {
                    if(hour == 0){
                        hour = 12;
                    }
                }
            } else {
                if (hour == 12) {
                    hour -= 12;
                }
            }
            if(min==1||min==2||min==3||min==4||min==5||min==6||min==7||min==8||min==9){
                String t = type + " " + hour + ":0" + min;
                return t;
            }else{
                String t = type + " " + hour + ":" + min;
                return t;
            }
    }
    public interface OnTimerPickListener {
        void onPick(String date);
    }

}
