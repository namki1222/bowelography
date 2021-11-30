package com.rightcode.bowelography.activity;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.style.ForegroundColorSpan;
import android.text.style.LineBackgroundSpan;
import android.text.style.StyleSpan;
import android.view.View;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.adapter.CalendarRecyclerDataViewAdapter;
import com.rightcode.bowelography.adapter.CalendarRecyclerViewAdapter;
import com.rightcode.bowelography.adapter.ExtraReportRecyclerViewAdapter;
import com.rightcode.bowelography.databinding.ActivityCalenderBinding;
import com.rightcode.bowelography.dialog.CalenderPopupDialog;
import com.rightcode.bowelography.dialog.LoadingDialog;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CalendarCondtionResponse;
import com.rightcode.bowelography.network.Response.CalendarSpotResponse;
import com.rightcode.bowelography.network.Response.CalendardayResponse;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.model.calendar;
import com.rightcode.bowelography.network.model.calendar_ai;
import com.rightcode.bowelography.network.model.calendar_condtion;
import com.rightcode.bowelography.network.model.calendardate;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;
import com.rightcode.bowelography.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;

import lombok.Setter;

public class CalenderActivity extends BaseActivity<ActivityCalenderBinding> {
    ArrayList<CalendarDay> dates = new ArrayList<>();
    GregorianCalendar mCalendar;
    SimpleDateFormat sdf;


    PreferenceUtil pref = PreferenceUtil.getInstance(mContext);
    Calendar calendar = Calendar.getInstance();
    int todayYear = calendar.get(Calendar.YEAR);
    int todayMonth = calendar.get(Calendar.MONTH) + 1;
    int todayDay = calendar.get(Calendar.DAY_OF_MONTH);


    @Setter
    Date limit;

    @Setter
    CalendarRecyclerViewAdapter adapter;
    CalendarRecyclerDataViewAdapter dataAdapter;

    private final ClickCallbackListener callbackListener = (list, data, date) -> {
        dataBinding.tvToday.setText(date);
        //리포트 가져오는부분
        if (!list.isEmpty()) {
            boolean checkbox_iv = PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Calendar_boolean, false);
            dataBinding.tvPooCount.setText(Integer.toString(list.size()));
            dataBinding.rvTodayReport.setVisibility(View.VISIBLE);
            dataBinding.tvTodayText.setVisibility(View.GONE);
            dataBinding.ivAddReport.setVisibility(View.GONE);
            connectDataAdapter(list, checkbox_iv);
        } else {
            dataBinding.tvPooCount.setText("0");
            dataBinding.rvTodayReport.setVisibility(View.GONE);
            dataBinding.tvTodayText.setVisibility(View.VISIBLE);
            dataBinding.ivAddReport.setVisibility(View.VISIBLE);
        }
        // 컨디션 및 몸무게
        if (data != null) {
            dataBinding.llConditonNull.setVisibility(View.GONE);
            dataBinding.llConditon.setVisibility(View.VISIBLE);
            if (data.getCondition() != null) {
                dataBinding.tvConditionEmote.setText(data.getCondition());
            } else {
                dataBinding.tvConditionEmote.setText("-");
            }
            if (data.getWeight() != null) {
                dataBinding.tvConditionWeight.setText(data.getWeight().toString() + "kg");
            } else {
                dataBinding.tvConditionWeight.setText("- kg");
            }
        } else {
            dataBinding.llConditonNull.setVisibility(View.VISIBLE);
            dataBinding.llConditon.setVisibility(View.GONE);
            dataBinding.tvConditionEmote.setText(null);
            dataBinding.tvConditionWeight.setText(null);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoading();
        bindView(R.layout.activity_calender);
    }

    public interface ClickCallbackListener {
        void CallBack(ArrayList<calendardate> list, calendar_condtion data, String date);
    }

    @Override
    protected void initActivity() {

        boolean checkbox_iv = PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Calendar_boolean, false);
        if (!checkbox_iv) {
            dataBinding.ivCheckbox.setSelected(false);
        } else {
            dataBinding.ivCheckbox.setSelected(true);
        }
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("캘린더", view -> finishWithAnim(), "LEFT");
        mTopFragment.setDefaultFormat("캘린더", view -> goCalender(), "CALENDER");

        if (todayMonth == 13) {
            todayMonth = 1;
        }
        dataBinding.tvToday.setText(todayYear + "년 " + todayMonth + "월 " + todayDay + "일");
        calendar_api(todayYear, todayMonth, todayDay);


        if (mCalendar == null) {
            mCalendar = new GregorianCalendar();
            mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        }
        if (sdf == null) {
            sdf = new SimpleDateFormat("yyyy.M");
        }


    }

    @Override
    protected void initClickListener() {
        dataBinding.ivAddReport.setOnClickListener(view -> {
            Intent intent = new Intent(this, ReportActivity.class);
            overridePendingTransition(0, 0);
            startActivity(intent);
        });
        dataBinding.ivPrevious.setOnClickListener(view -> {
            previousMonth();
        });

        dataBinding.ivNext.setOnClickListener(view -> {
            nextMonth();
        });
        dataBinding.btnCheckbox.setOnClickListener(view -> {
            if (!dataBinding.ivCheckbox.isSelected()) {
                dataBinding.ivCheckbox.setSelected(true);
                adapterPhotoReload(true);
            } else {
                dataBinding.ivCheckbox.setSelected(false);
                adapterPhotoReload(false);
            }
        });
    }

    private void goCalender() {
        Intent intent = new Intent(mContext, StatisticsActivity.class);
        startActivity(intent);
    }

    public void nextMonth() {
        mCalendar.add(Calendar.MONTH, 1);
        dataBinding.tvMonth.setText(sdf.format(mCalendar.getTime()));
        connectAdapter();
//        calendar_api(year, month);
    }

    public void previousMonth() {
        mCalendar.add(Calendar.MONTH, -1);
        dataBinding.tvMonth.setText(sdf.format(mCalendar.getTime()));
        connectAdapter();
//        calendar_api(year, month);

    }

    private ArrayList<Object> getCalendar() {
        ArrayList<Object> calendarList = new ArrayList<>();

        int dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        int max = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        for (int i = 0; i < dayOfWeek; i++) {
            String EMPTY = "empty";
            calendarList.add(EMPTY);
        }

        for (int i = 1; i <= max; i++) {
            calendarList.add(new GregorianCalendar(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), i));
        }

        return calendarList;
    }

    private void connectAdapter() {
        adapter.setCalendar(getCalendar());
        adapter.notifyDataSetChanged();
    }

    private void connectDataAdapter(ArrayList<calendardate> data, boolean check) {
        dataAdapter.setData(data);
        dataAdapter.setIv_checkbox(check);
        dataAdapter.notifyDataSetChanged();
    }

    private void adapterPhotoReload(boolean check) {
        dataAdapter.setIv_checkbox(check);
        pref.put(PreferenceUtil.PreferenceKey.Calendar_boolean, check);
        dataAdapter.notifyDataSetChanged();
    }

    public void calendar_api(int year, int month, int day) { //부가기록 보여주는 곳
        callApi(NetworkManager.getInstance(mContext).getApiService().calendar_change(year, month), new ApiResponseHandler<CalendarSpotResponse>() {
            @Override
            public void onSuccess(CalendarSpotResponse result) {
                StaggeredGridLayoutManager GridlayoutManager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);
                dataBinding.rvSchedule.setLayoutManager(GridlayoutManager);
                adapter = new CalendarRecyclerViewAdapter(mContext, result.getList());
                adapter.setCallbackListener(callbackListener);
                adapter.setLimit(limit);
                connectAdapter();
                dataBinding.rvSchedule.setAdapter(adapter);
                dataBinding.tvMonth.setText(sdf.format(mCalendar.getTime()));
                calendarReload(year, month, day);
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext, result.message);
            }

            @Override
            public void onNoResponse(CalendarSpotResponse result) {
                ToastUtil.show(mContext, result.message);
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });

    }

    public void calendarReload(int year, int month, int day) {
        callApi(NetworkManager.getInstance(mContext).getApiService().calendar_day_data(year, month, day), new ApiResponseHandler<CalendardayResponse>() {
            @Override
            public void onSuccess(CalendardayResponse result) {
                boolean checkbox_iv = PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Calendar_boolean, false);
                dataAdapter = new CalendarRecyclerDataViewAdapter(mContext, result.getList(), checkbox_iv);
                LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
                dataBinding.rvTodayReport.setLayoutManager(layoutManager);
                dataBinding.rvTodayReport.setAdapter(dataAdapter);
                if (!result.getList().isEmpty()) {
                    dataBinding.tvPooCount.setText(Integer.toString(result.getList().size()));
                    dataBinding.rvTodayReport.setVisibility(View.VISIBLE);
                    dataBinding.tvTodayText.setVisibility(View.GONE);
                    dataBinding.ivAddReport.setVisibility(View.GONE);
                    dataAdapter.setListener(thumbnail -> {
                        Intent intent = new Intent(mContext, CalenderPopupDialog.class);
                        intent.putExtra("thumbnail", thumbnail);
                        startActivity(intent);
                        overridePendingTransition(0, 0);
                    });
                } else {
                    dataBinding.tvPooCount.setText("0");
                    dataBinding.rvTodayReport.setVisibility(View.GONE);
                    dataBinding.tvTodayText.setVisibility(View.VISIBLE);
                    dataBinding.ivAddReport.setVisibility(View.VISIBLE);
                }
                hideLoading();
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext, result.message);
            }

            @Override
            public void onNoResponse(CalendardayResponse result) {
                ToastUtil.show(mContext, result.message);
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
        callApi(NetworkManager.getInstance(mContext).getApiService().calendar_condtion(year, month, day), new ApiResponseHandler<CalendarCondtionResponse>() {
            @Override
            public void onSuccess(CalendarCondtionResponse result) {
                if (result.getData() != null) {
                    dataBinding.llConditonNull.setVisibility(View.GONE);
                    dataBinding.llConditon.setVisibility(View.VISIBLE);
                    if (result.getData().getCondition() != null) {
                        dataBinding.tvConditionEmote.setText(result.getData().getCondition());
                    } else {
                        dataBinding.tvConditionEmote.setText("-");
                    }
                    if (result.getData().getWeight() != null) {
                        dataBinding.tvConditionWeight.setText(result.getData().getWeight().toString() + "kg");
                    } else {
                        dataBinding.tvConditionWeight.setText("- kg");
                    }
                } else {
                    dataBinding.llConditonNull.setVisibility(View.VISIBLE);
                    dataBinding.llConditon.setVisibility(View.GONE);
                    dataBinding.tvConditionEmote.setText(null);
                    dataBinding.tvConditionWeight.setText(null);

                }
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext, result.message);
            }

            @Override
            public void onNoResponse(CalendarCondtionResponse result) {
                ToastUtil.show(mContext, result.message);
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }

}
