package com.rightcode.bowelography.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewHolder.CommonViewHolder;
import com.rightcode.bowelography.activity.ReportExtraActivity;
import com.rightcode.bowelography.databinding.ItemListTodayPooBinding;
import com.rightcode.bowelography.dialog.CalenderPopupDialog;
import com.rightcode.bowelography.dialog.FavoritePopupDialog;
import com.rightcode.bowelography.network.model.calendar_additional;
import com.rightcode.bowelography.network.model.calendardate;
import com.rightcode.bowelography.util.Log;

import java.util.ArrayList;

import lombok.Setter;

public class CalendarRecyclerDataViewAdapter extends RecyclerView.Adapter<CalendarRecyclerDataViewAdapter.ViewHolder> {

    Context mContext;
    boolean more_on = false;
    boolean view_change = false;
    boolean rv_change = false;

    @Setter
    ArrayList<calendardate> data ;
    @Setter
    boolean iv_checkbox;

    @Setter
    HideimageListener listener;

    public CalendarRecyclerDataViewAdapter(Context mContext, ArrayList<calendardate> data, boolean iv_checkbox) {
        this.mContext = mContext;
        this.data = data;
        this.iv_checkbox = iv_checkbox;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_today_poo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBind(data.get(position));
    }

    @Override
    public int getItemCount() {
        if(!more_on){
            Log.d("접기 누르기");
            return 1;
        }else{
            return data != null ? data.size() : 0;
        }
    }
    public void setListener(HideimageListener listener) {
        this.listener = listener;
    }
    public interface HideimageListener{
        void onClick(String thumnail);
    }
    public class ViewHolder extends CommonViewHolder<ItemListTodayPooBinding, calendardate> {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(calendardate item) {
            dataBinding.rvList.setVisibility(View.GONE);
            CalendarReportViewAdapter adapter = new CalendarReportViewAdapter(mContext,item.getAdditionalRecords(),item.getId());
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false);
            dataBinding.rvList.setLayoutManager(layoutManager);
            dataBinding.rvList.setAdapter(adapter);
            if(!iv_checkbox){
                Log.d(item.getThumbnail()+"섬네일!!!!");
                if(item.getThumbnail()!=null){
                    Glide.with(mContext).load(item.getThumbnail()).override(40).apply(new RequestOptions().transform(new CenterCrop(),new RoundedCorners(5
                    ))).into(dataBinding.ivThumbnail);
                    dataBinding.ivThumbnail.setOnClickListener(view->{
                        listener.onClick(item.getThumbnail());

                    });
                }else{
                    Glide.with(mContext).load(R.drawable.poo_blind).into(dataBinding.ivThumbnail);
                }
            }else{
                Glide.with(mContext).load(R.drawable.poo_blind).into(dataBinding.ivThumbnail);
            }

            if (item.getShape().equals("딱딱")) {
                dataBinding.ivShapeColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_color_1));
            } else if (item.getShape().equals("단단")) {
                dataBinding.ivShapeColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_color_2));
            } else if (item.getShape().equals("건조")) {
                dataBinding.ivShapeColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_color_3));
            } else if (item.getShape().equals("매끈")) {
                dataBinding.ivShapeColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_color_4));
            } else if (item.getShape().equals("물렁")) {
                dataBinding.ivShapeColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_color_5));
            } else if (item.getShape().equals("찰흙")) {
                dataBinding.ivShapeColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_color_6));
            } else if (item.getShape().equals("물")) {
                dataBinding.ivShapeColor.setImageDrawable(mContext.getResources().getDrawable(R.drawable.shape_color_7));
            }
            if (item.getColor().equals("갈색")) {
                dataBinding.ivShapeColor.setColorFilter(Color.parseColor("#cb5f00"));
            } else if (item.getColor().equals("회색")) {
                dataBinding.ivShapeColor.setColorFilter(Color.parseColor("#d3c9c1"));
            } else if (item.getColor().equals("노란색")) {
                dataBinding.ivShapeColor.setColorFilter(Color.parseColor("#d78e00"));
            } else if (item.getColor().equals("빨간색")) {
                dataBinding.ivShapeColor.setColorFilter(Color.parseColor("#cb3c00"));
            } else if (item.getColor().equals("검정색")) {
                dataBinding.ivShapeColor.setColorFilter(Color.parseColor("#45403d"));
            } else if (item.getColor().equals("초록색")) {
                dataBinding.ivShapeColor.setColorFilter(Color.parseColor("#496108"));
            }
            dataBinding.tvTime.setText(item.getTime());
            dataBinding.tvScore.setText(item.getScore() + "점 >");
            dataBinding.tvMass.setText(item.getMass());
            dataBinding.tvColic.setText(item.getColic());
            dataBinding.tvEtc.setText(item.getEtc());
            dataBinding.tvElapsedTime.setText(item.getElapsedTime());
            dataBinding.tvHematochezia.setText(item.getHematochezia());
            dataBinding.tvHematocheziaPosition.setText(item.getHematocheziaPosition());
            dataBinding.tvSmell.setText(item.getSmell());
            dataBinding.tvOutsideEtc.setText(item.getAppearanceEtc());
            if (item.getAdditionalRecords().size()>0 || data.size()>1) {
                dataBinding.tvMore.setText("+ 더보기");
                dataBinding.tvMore.setVisibility(View.VISIBLE);
                dataBinding.tvMore.setOnClickListener(view->{
                    dataBinding.rvList.setVisibility(View.VISIBLE);
                    view_change = true;
                    more_on = true;
                    rv_change = true;
                    notifyDataSetChanged();
                });
                if (view_change) {
                    if(item.getAdditionalRecords().size()>0) {
                        dataBinding.tvMore.setVisibility(View.VISIBLE);
                        dataBinding.tvMore.setText("접기");
                    }else{
                        dataBinding.tvMore.setVisibility(View.GONE);
                    }
                }
                if(rv_change){
                    dataBinding.rvList.setVisibility(View.VISIBLE);
                }
            }
            if(getAdapterPosition() == (data.size() - 1) && getAdapterPosition()!=0) {
                dataBinding.tvMore.setVisibility(View.VISIBLE);
                dataBinding.tvMore.setText("접기");
                dataBinding.tvMore.setOnClickListener(view -> {
                    dataBinding.rvList.setVisibility(View.GONE);
                    more_on = false;
                    view_change = false;
                    rv_change = false;
                    notifyDataSetChanged();
                });
            }
            if(view_change==true&&(dataBinding.tvMore.getVisibility()==View.VISIBLE)){
                dataBinding.tvMore.setOnClickListener(view -> {
                    dataBinding.rvList.setVisibility(View.GONE);
                    more_on = false;
                    view_change = false;
                    rv_change = false;
                    notifyDataSetChanged();
                });
            }

        }
    }
}
