package com.rightcode.bowelography.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityStatisticsBinding;
import com.rightcode.bowelography.databinding.DialogDatePickerBinding;
import com.rightcode.bowelography.dialog.DatePickerDialog;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.StatisticResponse;
import com.rightcode.bowelography.network.Response.UserIdresponse;
import com.rightcode.bowelography.network.model.Statistic;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;


import org.eazegraph.lib.models.PieModel;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class StatisticsActivity extends BaseActivity<ActivityStatisticsBinding> {
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String beforeMonth,afterMonth;
    String getTime = sdf.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_statistics);
    }

    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("??????", view -> finishWithAnim(), "LEFT");
        dataBinding.tvOneMonth.setSelected(true);
        dataBinding.tvSecondDate.setText(getTime);
        Calendar mon = Calendar.getInstance();
        mon.add(Calendar.MONTH, -1);
        beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());
        dataBinding.tvFirstDate.setText(beforeMonth);
        getdata(beforeMonth, getTime);
    }

    @Override
    protected void initClickListener() {
        dataBinding.ll1.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(mContext);
            dialog.show(getSupportFragmentManager(), null);
            dialog.setListener((day) -> {
                this.beforeMonth = day;
                Log.d(beforeMonth + "!!!!");
                dataBinding.tvFirstDate.setText(beforeMonth);
                dataBinding.tvOneMonth.setSelected(false);
                dataBinding.tvThreeMonth.setSelected(false);
                dataBinding.tvSixMonth.setSelected(false);
                dataBinding.tvTwelveMonth.setSelected(false);
                getdata(dataBinding.tvFirstDate.getText().toString(), dataBinding.tvSecondDate.getText().toString());
            });
        });
        dataBinding.ll2.setOnClickListener(view -> {
            DatePickerDialog dialog = new DatePickerDialog(mContext);
            dialog.show(getSupportFragmentManager(), null);
            dialog.setListener((day) -> {
                this.afterMonth = day;
                Log.d(afterMonth + "!!!!");
                dataBinding.tvSecondDate.setText(afterMonth);
                dataBinding.tvOneMonth.setSelected(false);
                dataBinding.tvThreeMonth.setSelected(false);
                dataBinding.tvSixMonth.setSelected(false);
                dataBinding.tvTwelveMonth.setSelected(false);
                getdata(dataBinding.tvFirstDate.getText().toString(), dataBinding.tvSecondDate.getText().toString());
            });
        });
        dataBinding.tvOneMonth.setOnClickListener(view -> {
            if (view.isSelected()) {
                view.setSelected(false);
            } else {
                view.setSelected(true);
                dataBinding.tvThreeMonth.setSelected(false);
                dataBinding.tvSixMonth.setSelected(false);
                dataBinding.tvTwelveMonth.setSelected(false);

                dataBinding.tvSecondDate.setText(getTime);
                Calendar mon = Calendar.getInstance();
                mon.add(Calendar.MONTH, -1);
                String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());

                dataBinding.tvFirstDate.setText(beforeMonth);
                getdata(beforeMonth, getTime);

            }

        });
        dataBinding.tvThreeMonth.setOnClickListener(view -> {
            if (view.isSelected()) {
                view.setSelected(false);
            } else {
                view.setSelected(true);
                dataBinding.tvOneMonth.setSelected(false);
                dataBinding.tvSixMonth.setSelected(false);
                dataBinding.tvTwelveMonth.setSelected(false);

                dataBinding.tvSecondDate.setText(getTime);

                Calendar mon = Calendar.getInstance();
                mon.add(Calendar.MONTH, -3);
                String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());

                dataBinding.tvFirstDate.setText(beforeMonth);
                getdata(beforeMonth, getTime);
            }

        });
        dataBinding.tvSixMonth.setOnClickListener(view -> {
            if (view.isSelected()) {
                view.setSelected(false);
            } else {
                view.setSelected(true);
                dataBinding.tvThreeMonth.setSelected(false);
                dataBinding.tvOneMonth.setSelected(false);
                dataBinding.tvTwelveMonth.setSelected(false);

                dataBinding.tvSecondDate.setText(getTime);

                Calendar mon = Calendar.getInstance();
                mon.add(Calendar.MONTH, -6);
                String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());

                dataBinding.tvFirstDate.setText(beforeMonth);
                getdata(beforeMonth, getTime);
            }

        });
        dataBinding.tvTwelveMonth.setOnClickListener(view -> {
            if (view.isSelected()) {
                view.setSelected(false);
            } else {
                view.setSelected(true);
                dataBinding.tvThreeMonth.setSelected(false);
                dataBinding.tvSixMonth.setSelected(false);
                dataBinding.tvOneMonth.setSelected(false);

                dataBinding.tvSecondDate.setText(getTime);

                Calendar mon = Calendar.getInstance();
                mon.add(Calendar.MONTH, -12);
                String beforeMonth = new java.text.SimpleDateFormat("yyyy-MM-dd").format(mon.getTime());

                dataBinding.tvFirstDate.setText(beforeMonth);
                getdata(beforeMonth, getTime);
            }

        });

    }


    private void setlineChart(Statistic api_list) {
        Statistic data = api_list;
        int total_count = data.getCountRecords();
        dataBinding.tvLineCount1.setText("??? " + total_count + "???");
        ArrayList blood_values = new ArrayList<>();
        final ArrayList<String> blood_labels = new ArrayList<>();
        blood_labels.add("??????");
        blood_labels.add("??????");
        blood_labels.add("??????");
        blood_labels.add("??????");

        blood_values.add(new BarEntry(0, data.getStatistic().getHematochezia().get??????()));
        blood_values.add(new BarEntry(1, data.getStatistic().getHematochezia().get??????()));
        blood_values.add(new BarEntry(2, data.getStatistic().getHematochezia().get??????()));
        blood_values.add(new BarEntry(3, data.getStatistic().getHematochezia().get??????()));
        setlineChart(dataBinding.lineChart1, blood_labels, blood_values, maxCount(data.getStatistic().getHematochezia().get??????(),data.getStatistic().getHematochezia().get??????(),data.getStatistic().getHematochezia().get??????(),data.getStatistic().getHematochezia().get??????()));

        dataBinding.tvLineCount2.setText("??? " + total_count + "???");
        ArrayList mass_values = new ArrayList<>();
        final ArrayList<String> mass_labels = new ArrayList<>();
        mass_labels.add("??????");
        mass_labels.add("??????");
        mass_labels.add("??????");
        mass_values.add(new BarEntry(0, (float) data.getStatistic().getMass().get??????()));
        mass_values.add(new BarEntry(1, (float) data.getStatistic().getMass().get??????()));
        mass_values.add(new BarEntry(2, (float) data.getStatistic().getMass().get??????()));
        setlineChart(dataBinding.lineChart2, mass_labels, mass_values, maxCount(data.getStatistic().getMass().get??????(),data.getStatistic().getMass().get??????(),data.getStatistic().getMass().get??????(),0));

        dataBinding.tvLineCount3.setText("??? " + total_count + "???");
        ArrayList colic_values = new ArrayList<>();
        final ArrayList<String> colic_labels = new ArrayList<>();
        colic_labels.add("??????");
        colic_labels.add("??????");
        colic_labels.add("??????");
        colic_labels.add("??????");
        colic_values.add(new BarEntry(0, data.getStatistic().getColic().get??????()));
        colic_values.add(new BarEntry(1, data.getStatistic().getColic().get??????()));
        colic_values.add(new BarEntry(2, data.getStatistic().getColic().get??????()));
        colic_values.add(new BarEntry(3, data.getStatistic().getColic().get??????()));
        setlineChart(dataBinding.lineChart3, colic_labels, colic_values, maxCount(data.getStatistic().getColic().get??????(),data.getStatistic().getColic().get??????(),data.getStatistic().getColic().get??????(),data.getStatistic().getColic().get??????()));

        dataBinding.tvLineCount4.setText("??? " + total_count + "???");
        ArrayList etc_values = new ArrayList<>();
        final ArrayList<String> etc_labels = new ArrayList<>();
        etc_labels.add("??????");
        etc_labels.add("?????????");
        etc_labels.add("?????????");
        etc_values.add(new BarEntry(0, (float) data.getStatistic().getAppearanceEtc().get??????()));
        etc_values.add(new BarEntry(1, (float) data.getStatistic().getAppearanceEtc().get?????????()));
        etc_values.add(new BarEntry(2, (float) data.getStatistic().getAppearanceEtc().get?????????()));
        setlineChart(dataBinding.lineChart4, etc_labels, etc_values, maxCount(data.getStatistic().getAppearanceEtc().get??????(),data.getStatistic().getAppearanceEtc().get?????????(),data.getStatistic().getAppearanceEtc().get?????????(),0));

        dataBinding.tvLineCount5.setText("??? " + total_count + "???");
        ArrayList etc_1_values = new ArrayList<>();
        final ArrayList<String> etc_1_labels = new ArrayList<>();
        etc_1_labels.add("?????????");
        etc_1_labels.add("?????????");
        etc_1_labels.add("??????");
        etc_1_values.add(new BarEntry(0, (float) data.getStatistic().getEtc().get?????????()));
        etc_1_values.add(new BarEntry(1, (float) data.getStatistic().getEtc().get?????????()));
        etc_1_values.add(new BarEntry(2, (float) data.getStatistic().getEtc().get??????_???_??????()));
        setlineChart(dataBinding.lineChart5, etc_1_labels, etc_1_values, maxCount(data.getStatistic().getEtc().get?????????(),data.getStatistic().getEtc().get?????????(),data.getStatistic().getEtc().get??????_???_??????(),0));
    }

    private void setPieChart(Statistic api_list) {
        Statistic data = api_list;
        String Avg;
        //?????? piechart
        dataBinding.pieChart.clearChart();
        dataBinding.tvPiechart1.setText(data.getCountRecords().toString() + "???");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get??????());
        dataBinding.tvPie11.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get??????());
        dataBinding.tvPie12.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get??????());
        dataBinding.tvPie13.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get??????());
        dataBinding.tvPie14.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get??????());
        dataBinding.tvPie15.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get??????());
        dataBinding.tvPie16.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get???());
        dataBinding.tvPie17.setText(" " + Avg + "%");


        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get??????(), Color.parseColor("#ff6f74")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get??????(), Color.parseColor("#ffa557")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get??????(), Color.parseColor("#ffe65f")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get??????(), Color.parseColor("#df74ff")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get??????(), Color.parseColor("#88edff")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get??????(), Color.parseColor("#f5ff7e")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get???(), Color.parseColor("#bfbfbf")));

        dataBinding.pieChart2.clearChart();

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get??????());
        dataBinding.tvPie21.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get??????());
        dataBinding.tvPie22.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get?????????());
        dataBinding.tvPie23.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get?????????());
        dataBinding.tvPie24.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get?????????());
        dataBinding.tvPie25.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get?????????());
        dataBinding.tvPie26.setText(" " + Avg + "%");

        dataBinding.tvPiechart2.setText(data.getCountRecords().toString() + "???");
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get??????(), Color.parseColor("#cb5f00")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get??????(), Color.parseColor("#d3c9c1")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get?????????(), Color.parseColor("#d78e00")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get?????????(), Color.parseColor("#cb3c00")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get?????????(), Color.parseColor("#45403d")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get?????????(), Color.parseColor("#496108")));

        dataBinding.pieChart3.clearChart();

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get????????????_??????());
        dataBinding.tvPie31.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get??????());
        dataBinding.tvPie32.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get??????());
        dataBinding.tvPie33.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get??????());
        dataBinding.tvPie34.setText(" " + Avg + "%");

        dataBinding.tvPiechart3.setText(data.getCountRecords().toString() + "???");
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get????????????_??????(), Color.parseColor("#ff8e92")));
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get??????(), Color.parseColor("#ffb97c")));
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get??????(), Color.parseColor("#ffef98")));
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get??????(), Color.parseColor("#edb2ff")));
    }

    private void setlineChart(LineChart a, ArrayList labels, ArrayList values, int count) {
        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);
        data.setValueFormatter(new LargeValueFormatter());

        // black lines and points
        set1.setDrawValues(false);
        set1.setColor(Color.parseColor("#ff6e75"));
        set1.setCircleColor(Color.parseColor("#ff6e75"));
        set1.setLineWidth(3);
        set1.setCircleRadius(6);
        set1.setCircleHoleRadius(3);
        Legend legend = a.getLegend(); //????????? ?????? (?????? ?????? ?????? ????????? ???????????? ??????)
        legend.setEnabled(false);

        XAxis xAxis = a.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));
        xAxis.setLabelCount(labels.size(), true);


        YAxis yLAxis = a.getAxisLeft();
        YAxis yRAxis = a.getAxisRight();
        yLAxis.setDrawAxisLine(false);
        yLAxis.setDrawGridLines(false);
        yLAxis.setDrawLabels(false);
        yRAxis.setLabelCount(4, true);
        yRAxis.setAxisMinimum(0);
        yRAxis.setDrawGridLines(false);


        a.setDescription(null);
        a.setTouchEnabled(false);
        a.setPinchZoom(false);

        // set data
        a.setData(data);
        a.invalidate();
    }

    public void getdata(String start, String end) {
        callApi(NetworkManager.getInstance(mContext).getApiService().user_statistic(start, end), new ApiResponseHandler<StatisticResponse>() {
            @Override
            public void onSuccess(StatisticResponse result) {
                Log.d("??????");
                if (result.getData().getCountRecords() != 0) {
                    dataBinding.ivNoStatitic.setVisibility(View.GONE);
                    dataBinding.llStatistic.setVisibility(View.VISIBLE);
                    Statistic data = result.getData();
                    dataBinding.tvScore.setText(result.getData().getAverage().get(0).getAverage().toString() + "???");
                    setPieChart(data);
                    setlineChart(data);
                } else {
                    dataBinding.ivNoStatitic.setVisibility(View.VISIBLE);
                    dataBinding.llStatistic.setVisibility(View.GONE);
                }
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(StatisticResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!?????????!!");

            }
        });
    }

    public String percent(float total, int num) {
        if (num == 0) {
            return "0";
        } else {
            double percent = num * total;
            Long result = Math.round(percent);
            return result.toString();
        }
    }

    public Integer maxCount(int a, int b, int c, int d) {
        ArrayList<Integer> num = new ArrayList<Integer>();
        num.add(a);
        num.add(b);
        num.add(c);
        num.add(d);
        int max = 0;
        for (int i = 0; i < num.size(); i++) //for?????? ?????????????????? ??????
        {
            if (max < num.get(i)) {
                max = num.get(i);
            }
        }
        return max;
    }
}