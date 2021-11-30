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
import com.rightcode.bowelography.activity.NoticeContentActivity;
import com.rightcode.bowelography.databinding.ItemAdvertiseViewpagerBinding;
import com.rightcode.bowelography.databinding.ItemNoticeListBinding;
import com.rightcode.bowelography.network.model.advertisement_detail;
import com.rightcode.bowelography.network.model.notice;
import com.rightcode.bowelography.util.Log;

import java.util.ArrayList;

import lombok.Setter;

public class AdvertisementDetailViewAdapter extends RecyclerView.Adapter<AdvertisementDetailViewAdapter.ViewHolder> {

    Context mContext;
    @Setter
    ArrayList<advertisement_detail> data ;

    public AdvertisementDetailViewAdapter(Context mContext, ArrayList<advertisement_detail> data) {
        this.mContext = mContext;
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advertise_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertisementDetailViewAdapter.ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends CommonViewHolder<ItemAdvertiseViewpagerBinding, advertisement_detail> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(advertisement_detail item) {
            Glide.with(mContext).load(item.getDetailImage()).into(dataBinding.ivThumbnail);
        }
    }
}
