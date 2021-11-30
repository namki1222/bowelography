package com.rightcode.bowelography.ViewPagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.databinding.ItemGuideViewpagerBinding;

import java.util.ArrayList;

public class PhotoGuideViewPagerAdapter extends RecyclerView.Adapter<PhotoGuideViewPagerAdapter.ViewHolder> {

    Context mContext;
    ArrayList<Integer> images;

    public PhotoGuideViewPagerAdapter(Context mContext, ArrayList<Integer> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_guide_viewpager, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(images.get(position));
    }

    @Override
    public int getItemCount() {
        return images != null ? images.size() : 0;
    }

    public class ViewHolder extends CommonViewHolder<ItemGuideViewpagerBinding, Integer> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(Integer item) {
            Glide.with(mContext).load(item).into(dataBinding.imgGuide);
        }
    }
}
