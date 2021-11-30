package com.rightcode.bowelography.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.databinding.ItemDateBinding;
import com.rightcode.bowelography.databinding.ItemEmptyDateBinding;
import com.rightcode.bowelography.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

public class CalendarDialogRecyclerViewAdapter extends RecyclerView.Adapter<CommonViewHolder<? extends ViewDataBinding, ?>> {

    private final int EMPTY = 0;
    private final int DATE = 1;

    @Getter
    String selectedDate;

    @Setter
    ArrayList<Object> calendar;

    @Setter
    Date limit;

    Context mContext;

    public CalendarDialogRecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
        if (selectedDate == null) {
            selectedDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
    }

    @NonNull
    @Override
    public CommonViewHolder<? extends ViewDataBinding, ?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_empty_date, parent, false);
            return new EmptyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date, parent, false);
            return new DateViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull CommonViewHolder<? extends ViewDataBinding, ?> holder, int position) {
        if (holder instanceof EmptyViewHolder) {
            ((EmptyViewHolder)holder).onBind((String) calendar.get(position));
        } else {
            ((DateViewHolder)holder).onBind((GregorianCalendar) calendar.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return calendar != null ? calendar.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = calendar.get(position);
        if (item instanceof String) {
            return EMPTY;
        } else {
            return DATE;
        }
    }

    public class DateViewHolder extends CommonViewHolder<ItemDateBinding, GregorianCalendar> {

        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(GregorianCalendar item) {
            dataBinding.itemDay.setText(String.format(Locale.KOREA,"%d", item.get(Calendar.DAY_OF_MONTH)));

            String s = new SimpleDateFormat("yyyy-MM-dd").format(item.getTime());
            if (s.equals(selectedDate) && mContext != null) {
                dataBinding.itemLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.color_app_color_circle));
                dataBinding.itemDay.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                dataBinding.itemLayout.setBackground(null);
                dataBinding.itemDay.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }

            itemView.setOnClickListener(v -> {
                if (limit != null && !validDate(s)) {
                    ToastUtil.show(mContext, "유효한 날짜가 아닙니다.");
                    return;
                }
                selectedDate = s;
                notifyDataSetChanged();
            });
        }
    }

    private boolean validDate(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date selected = sdf.parse(s);
            return selected.compareTo(limit) <= 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static class EmptyViewHolder extends CommonViewHolder<ItemEmptyDateBinding, String> {

        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(String item) {

        }
    }
}
