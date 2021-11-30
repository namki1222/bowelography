package com.rightcode.bowelography.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.ArrayListModel;
import com.rightcode.bowelography.activity.ReportExtraActivity;
import com.rightcode.bowelography.databinding.ItemExtraFavoriteBinding;
import com.rightcode.bowelography.network.model.favorite;
import com.rightcode.bowelography.util.Log;

import java.util.ArrayList;

import lombok.Setter;

public class ExtraReportRecyclerViewAdapter extends RecyclerView.Adapter<ExtraReportRecyclerViewAdapter.ViewHolder> {

    Context mContext;
    ArrayList<favorite> data ;
    @Setter
    RemoveOnclick listener;
    @Setter
    AddOnclick listenerAdd;

    public ExtraReportRecyclerViewAdapter(Context mContext, ArrayList<favorite> data) {
        this.mContext = mContext;
        this.data = data;

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_extra_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExtraReportRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }
    public void setListener(AddOnclick listener) {
        this.listenerAdd = (AddOnclick) listener;
    }
    public void setListener(RemoveOnclick listener) {
        this.listener = (RemoveOnclick) listener;
    }


    public interface RemoveOnclick{
        void onClick(Integer id);
    }
    public interface AddOnclick{
        void onClick(String contents,String category);
    }

    public class ViewHolder extends CommonViewHolder<ItemExtraFavoriteBinding, favorite> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(favorite item) {
            if(item.getCategory().equals("식사")){
                Glide.with(mContext).load(R.drawable.mini_emote1).into(dataBinding.ivEmote);
            }else if(item.getCategory().equals("음료")){
                Glide.with(mContext).load(R.drawable.mini_emote2).into(dataBinding.ivEmote);
            }else if(item.getCategory().equals("약")){
                Glide.with(mContext).load(R.drawable.mini_emote3).into(dataBinding.ivEmote);
            }else if(item.getCategory().equals("운동")){
                Glide.with(mContext).load(R.drawable.mini_emote4).into(dataBinding.ivEmote);
            }else if(item.getCategory().equals("기타")){
                Glide.with(mContext).load(R.drawable.mini_emote5).into(dataBinding.ivEmote);
            }
            dataBinding.ivExit.setOnClickListener(view->{
                listener.onClick(item.getId());

            });
            dataBinding.tvText.setText(item.getContents());
            itemView.setOnClickListener(view->{
                listenerAdd.onClick(item.getContents(),item.getCategory());
            });

        }
    }
}
