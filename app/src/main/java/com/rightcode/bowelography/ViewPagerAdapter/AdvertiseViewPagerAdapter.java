package com.rightcode.bowelography.ViewPagerAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.activity.AdvertisementDetailActivity;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.databinding.ItemAdvertiseViewpagerBinding;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.AdvertisementdetailResponse;
import com.rightcode.bowelography.network.Response.CalendarCondtionResponse;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.model.advertisement;
import com.rightcode.bowelography.util.Log;

import java.util.ArrayList;

public class AdvertiseViewPagerAdapter extends RecyclerView.Adapter<AdvertiseViewPagerAdapter.ViewHolder> {

    Context mContext;
    String thumnail;
    ArrayList<advertisement> data ;
    public AdvertiseViewPagerAdapter(Context mContext,ArrayList<advertisement> data) {
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
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends CommonViewHolder<ItemAdvertiseViewpagerBinding, advertisement> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(advertisement item) {
            Glide.with(mContext).load(item.getThumbnail()).into(dataBinding.ivThumbnail);
            itemView.setOnClickListener(view->{
                Intent intent = new Intent(mContext, AdvertisementDetailActivity.class);
                intent.putExtra("id",item.getId());
                mContext.startActivity(intent);
            });
        }

    }
}
