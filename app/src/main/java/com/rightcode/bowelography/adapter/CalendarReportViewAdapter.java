package com.rightcode.bowelography.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.activity.ReportExtraActivity;
import com.rightcode.bowelography.databinding.ItemFaqListBinding;
import com.rightcode.bowelography.databinding.ItemReportListBinding;
import com.rightcode.bowelography.network.model.FAQ;
import com.rightcode.bowelography.network.model.calendar_additional;
import com.rightcode.bowelography.util.Log;

import java.util.ArrayList;

public class CalendarReportViewAdapter extends RecyclerView.Adapter<CalendarReportViewAdapter.ViewHolder> {

    Context mContext;
    ArrayList<calendar_additional> data;
    int id;

    public CalendarReportViewAdapter(Context mContext, ArrayList<calendar_additional> data,int id) {
        this.mContext = mContext;
        this.data = data;
        this.id = id;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends CommonViewHolder<ItemReportListBinding, calendar_additional> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(calendar_additional item) {
            Log.d("!!!리사이클러뷰!!");
                dataBinding.reportDetailContent1.setText(item.getContents());
                if (item.getCategory().equals("식사")) {
                    Glide.with(mContext).load(R.drawable.mini_emote1).into(dataBinding.reportEmote1);
                } else if (item.getCategory().equals("음료")) {
                    Glide.with(mContext).load(R.drawable.mini_emote2).into(dataBinding.reportEmote1);
                } else if (item.getCategory().equals("약")) {
                    Glide.with(mContext).load(R.drawable.mini_emote3).into(dataBinding.reportEmote1);
                } else if (item.getCategory().equals("운동")) {
                    Glide.with(mContext).load(R.drawable.mini_emote4).into(dataBinding.reportEmote1);
                } else if (item.getCategory().equals("기타")) {
                    Glide.with(mContext).load(R.drawable.mini_emote5).into(dataBinding.reportEmote1);
                }
            dataBinding.reportDetail1.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, ReportExtraActivity.class);
                intent.putExtra("report_id", id);
                intent.putExtra("report_String", "finish");
                mContext.startActivity(intent);
            });


        }
    }
}
