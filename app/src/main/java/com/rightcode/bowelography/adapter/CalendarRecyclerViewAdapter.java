package com.rightcode.bowelography.adapter;

import android.content.Context;
import android.os.Handler;
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
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.activity.CalenderActivity.ClickCallbackListener;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CalendarCondtionResponse;
import com.rightcode.bowelography.network.Response.CalendardayResponse;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.model.calendar_condtion;
import com.rightcode.bowelography.network.model.calendar_main;
import com.rightcode.bowelography.network.model.calendardate;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import lombok.Getter;
import lombok.Setter;

public class CalendarRecyclerViewAdapter extends RecyclerView.Adapter<CommonViewHolder<? extends ViewDataBinding, ?>>{

    private final int EMPTY = 0;
    private final int DATE = 1;
    private ClickCallbackListener callbackListener;

    @Getter
    String selectedDate;
    @Getter

    @Setter
    ArrayList<Object> calendar;
    @Setter
    Date limit;

    calendar_main calendar_list;
    Context mContext;
    public void setCallbackListener(ClickCallbackListener callbackListener){
        this.callbackListener = callbackListener;
    }

    public CalendarRecyclerViewAdapter(Context mContext, calendar_main list) {
        this.mContext = mContext;
        this.calendar_list = list;
        if (selectedDate == null) {
            selectedDate = new SimpleDateFormat("yyyy-M-d").format(new Date());
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
            ((DateViewHolder)holder).onBind((GregorianCalendar) calendar.get(position),callbackListener);
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
    public class DateViewHolder extends CommonViewHolder<ItemDateBinding, GregorianCalendar> implements View.OnClickListener{
        private ClickCallbackListener callbackListener;
        ArrayList<calendardate> list;
        calendar_condtion data;
        String s;
        String year;
        String month;
        String day;
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            setListener();
        }

        @Override
        public void onBind(GregorianCalendar item) {
        }

        public void onBind(GregorianCalendar item,ClickCallbackListener callbackListener) {
            this.callbackListener = callbackListener;
            boolean normal_check = false;
            boolean ai_check = false;
            ArrayList<String> normal_date = new ArrayList<>();
            ArrayList<String> ai_date = new ArrayList<>();
            dataBinding.itemDay.setText(String.format(Locale.KOREA,"%d", item.get(Calendar.DAY_OF_MONTH)));

            dataBinding.ivNormalSpot.setVisibility(View.INVISIBLE);
            dataBinding.ivAiSpot.setVisibility(View.INVISIBLE);
            dataBinding.ivNormalSpotSolo.setVisibility(View.INVISIBLE);
            dataBinding.ivAiSpotSolo.setVisibility(View.INVISIBLE);

            s = new SimpleDateFormat("yyyy-M-d").format(item.getTime());
            if(s !=null){
                if(calendar_list.getAi()!=null){
                    for(int count = 0; count <calendar_list.getAi().size();count++){
                        ai_date.add(count,calendar_list.getAi().get(count).getYear() + "-" + calendar_list.getAi().get(count).getMonth() + "-" + calendar_list.getAi().get(count).getDate());
                        if (s.equals(ai_date.get(count))) {
                            dataBinding.ivNormalSpot.setVisibility(View.GONE);
                            dataBinding.ivAiSpot.setVisibility(View.GONE);
                            dataBinding.ivNormalSpotSolo.setVisibility(View.GONE);
                            dataBinding.ivAiSpotSolo.setVisibility(View.VISIBLE);
                            ai_check = true;
                        }
                    }
                }
                if(calendar_list.getNormal()!=null) {
                    for (int count = 0; count < calendar_list.getNormal().size(); count++) {
                        normal_date.add(count, calendar_list.getNormal().get(count).getYear() + "-" + calendar_list.getNormal().get(count).getMonth() + "-" + calendar_list.getNormal().get(count).getDate());
                        if (s.equals(normal_date.get(count))) {
                            dataBinding.ivNormalSpot.setVisibility(View.GONE);
                            dataBinding.ivAiSpot.setVisibility(View.GONE);
                            dataBinding.ivNormalSpotSolo.setVisibility(View.VISIBLE);
                            dataBinding.ivAiSpotSolo.setVisibility(View.GONE);
                            normal_check = true;
                        }
                    }
                }
                if(ai_check==true&&normal_check==true){
                    dataBinding.ivNormalSpot.setVisibility(View.VISIBLE);
                    dataBinding.ivAiSpot.setVisibility(View.VISIBLE);
                    dataBinding.ivNormalSpotSolo.setVisibility(View.GONE);
                    dataBinding.ivAiSpotSolo.setVisibility(View.GONE);
                }
            }
            year = new SimpleDateFormat("yyyy").format(item.getTime());
            month = new SimpleDateFormat("M").format(item.getTime());
            day = new SimpleDateFormat("d").format(item.getTime());
            if (s.equals(selectedDate) && mContext != null) {
                dataBinding.itemLayout.setBackground(ContextCompat.getDrawable(mContext, R.drawable.color_app_color_circle));
                dataBinding.itemDay.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                dataBinding.itemLayout.setBackground(null);
                dataBinding.itemDay.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            }
        }
        public void report_api(String s_year,String s_month,String s_day){
            int year = Integer.parseInt(s_year);
            int month = Integer.parseInt(s_month);
            int day = Integer.parseInt(s_day);
            callApi(NetworkManager.getInstance(mContext).getApiService().calendar_day_data(year, month, day), new ApiResponseHandler<CalendardayResponse>() {
                @Override
                public void onSuccess(CalendardayResponse result) {
                        String date =year + "년 " + month + "월 "+day+"일";
                        list = result.getList();
                    callbackListener.CallBack(list,data,date);
                }

                @Override
                public void onServerFail(CommonResponse result) {
                    ToastUtil.show(mContext,result.message);
                }

                @Override
                public void onNoResponse(CalendardayResponse result) {
                    ToastUtil.show(mContext,result.message);
                }

                @Override
                public void onBadRequest(Throwable t) {
                    Log.d("!!!!!잘못됨!!");

                }
            });
            callApi(NetworkManager.getInstance(mContext).getApiService().calendar_condtion(year, month, day), new ApiResponseHandler<CalendarCondtionResponse>() {
                @Override
                public void onSuccess(CalendarCondtionResponse result) {
                    data = result.getData();
                }

                @Override
                public void onServerFail(CommonResponse result) {
                    ToastUtil.show(mContext,result.message);
                }

                @Override
                public void onNoResponse(CalendarCondtionResponse result) {
                    ToastUtil.show(mContext,result.message);
                }

                @Override
                public void onBadRequest(Throwable t) {
                    Log.d("!!!!!잘못됨!!");

                }
            });
        }

        @Override
        public void onClick(View v) {
            if (limit != null && !validDate(s)) {
                ToastUtil.show(mContext, "유효한 날짜가 아닙니다.");
                return;
            }
            selectedDate = s;
            report_api(year,month,day);
            notifyDataSetChanged();
        }
        public void setListener(){
            itemView.setOnClickListener(this);
        }
    }

    private boolean validDate(String s) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
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
