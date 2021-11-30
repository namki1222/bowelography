package com.rightcode.bowelography.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.databinding.ItemFaqListBinding;
import com.rightcode.bowelography.network.model.FAQ;

import java.util.ArrayList;

import lombok.Setter;

public class FaqRecyclerViewAdapter extends RecyclerView.Adapter<FaqRecyclerViewAdapter.ViewHolder> {

    Context mContext;

    @Setter
    ArrayList<FAQ> data ;

    public FaqRecyclerViewAdapter(Context mContext, ArrayList<FAQ> data) {
        this.mContext = mContext;
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faq_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FaqRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public class ViewHolder extends CommonViewHolder<ItemFaqListBinding, FAQ> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(FAQ item) {
            dataBinding.tvTitle.setText("0"+getAdapterPosition()+". "+item.getSubject());
            dataBinding.llBtn.setOnClickListener(view->{
                if(dataBinding.ivSelect.isSelected()){
                    dataBinding.ivSelect.setSelected(false);
                    dataBinding.tvContent.setVisibility(View.GONE);
                }else{
                    dataBinding.ivSelect.setSelected(true);
                    dataBinding.tvContent.setVisibility(View.VISIBLE);
                    dataBinding.tvContent.setText(item.getContent());
                }
            });
        }
    }
}
