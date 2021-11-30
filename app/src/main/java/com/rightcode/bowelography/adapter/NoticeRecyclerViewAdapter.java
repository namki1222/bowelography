package com.rightcode.bowelography.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rightcode.bowelography.activity.NoticeContentActivity;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.databinding.ItemNoticeListBinding;
import com.rightcode.bowelography.network.model.notice;
import com.rightcode.bowelography.util.Log;

import java.util.ArrayList;

import lombok.Setter;

public class NoticeRecyclerViewAdapter extends RecyclerView.Adapter<NoticeRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    @Setter
    ArrayList<notice> data ;

    public NoticeRecyclerViewAdapter(Context mContext, ArrayList<notice> data) {
        this.mContext = mContext;
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends CommonViewHolder<ItemNoticeListBinding, notice> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(notice item) {
            dataBinding.tvTitle.setText(item.getSubject());
            dataBinding.tvContent.setText(item.getContent());
            itemView.setOnClickListener(view->{
                Intent intent = new Intent(mContext, NoticeContentActivity.class);
                Log.d(item.getId());
                intent.putExtra("id",item.getId());
                startActivity(mContext,intent);
            });

        }
    }
}
