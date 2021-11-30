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
        mTopFragment.setDefaultFormat("통계", view -> finishWithAnim(), "LEFT");
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
        dataBinding.tvLineCount1.setText("총 " + total_count + "회");
        ArrayList blood_values = new ArrayList<>();
        final ArrayList<String> blood_labels = new ArrayList<>();
        blood_labels.add("없음");
        blood_labels.add("미량");
        blood_labels.add("소량");
        blood_labels.add("대량");

        blood_values.add(new BarEntry(0, data.getStatistic().getHematochezia().get없음()));
        blood_values.add(new BarEntry(1, data.getStatistic().getHematochezia().get미량()));
        blood_values.add(new BarEntry(2, data.getStatistic().getHematochezia().get소량()));
        blood_values.add(new BarEntry(3, data.getStatistic().getHematochezia().get대량()));
        setlineChart(dataBinding.lineChart1, blood_labels, blood_values, maxCount(data.getStatistic().getHematochezia().get없음(),data.getStatistic().getHematochezia().get미량(),data.getStatistic().getHematochezia().get미량(),data.getStatistic().getHematochezia().get미량()));

        dataBinding.tvLineCount2.setText("총 " + total_count + "회");
        ArrayList mass_values = new ArrayList<>();
        final ArrayList<String> mass_labels = new ArrayList<>();
        mass_labels.add("적음");
        mass_labels.add("적당");
        mass_labels.add("많음");
        mass_values.add(new BarEntry(0, (float) data.getStatistic().getMass().get적음()));
        mass_values.add(new BarEntry(1, (float) data.getStatistic().getMass().get평범()));
        mass_values.add(new BarEntry(2, (float) data.getStatistic().getMass().get많음()));
        setlineChart(dataBinding.lineChart2, mass_labels, mass_values, maxCount(data.getStatistic().getMass().get적음(),data.getStatistic().getMass().get평범(),data.getStatistic().getMass().get많음(),0));

        dataBinding.tvLineCount3.setText("총 " + total_count + "회");
        ArrayList colic_values = new ArrayList<>();
        final ArrayList<String> colic_labels = new ArrayList<>();
        colic_labels.add("없음");
        colic_labels.add("약간");
        colic_labels.add("중간");
        colic_labels.add("고통");
        colic_values.add(new BarEntry(0, data.getStatistic().getColic().get없음()));
        colic_values.add(new BarEntry(1, data.getStatistic().getColic().get약간()));
        colic_values.add(new BarEntry(2, data.getStatistic().getColic().get중간()));
        colic_values.add(new BarEntry(3, data.getStatistic().getColic().get고통()));
        setlineChart(dataBinding.lineChart3, colic_labels, colic_values, maxCount(data.getStatistic().getColic().get없음(),data.getStatistic().getColic().get약간(),data.getStatistic().getColic().get중간(),data.getStatistic().getColic().get고통()));

        dataBinding.tvLineCount4.setText("총 " + total_count + "회");
        ArrayList etc_values = new ArrayList<>();
        final ArrayList<String> etc_labels = new ArrayList<>();
        etc_labels.add("점액");
        etc_labels.add("기름기");
        etc_labels.add("둥둥뜸");
        etc_values.add(new BarEntry(0, (float) data.getStatistic().getAppearanceEtc().get점액()));
        etc_values.add(new BarEntry(1, (float) data.getStatistic().getAppearanceEtc().get기름기()));
        etc_values.add(new BarEntry(2, (float) data.getStatistic().getAppearanceEtc().get둥둥뜸()));
        setlineChart(dataBinding.lineChart4, etc_labels, etc_values, maxCount(data.getStatistic().getAppearanceEtc().get점액(),data.getStatistic().getAppearanceEtc().get기름기(),data.getStatistic().getAppearanceEtc().get둥둥뜸(),0));

        dataBinding.tvLineCount5.setText("총 " + total_count + "회");
        ArrayList etc_1_values = new ArrayList<>();
        final ArrayList<String> etc_1_labels = new ArrayList<>();
        etc_1_labels.add("잔변감");
        etc_1_labels.add("급박변");
        etc_1_labels.add("급통");
        etc_1_values.add(new BarEntry(0, (float) data.getStatistic().getEtc().get잔변감()));
        etc_1_values.add(new BarEntry(1, (float) data.getStatistic().getEtc().get급박변()));
        etc_1_values.add(new BarEntry(2, (float) data.getStatistic().getEtc().get배변_후_급통()));
        setlineChart(dataBinding.lineChart5, etc_1_labels, etc_1_values, maxCount(data.getStatistic().getEtc().get잔변감(),data.getStatistic().getEtc().get급박변(),data.getStatistic().getEtc().get배변_후_급통(),0));
    }

    private void setPieChart(Statistic api_list) {
        Statistic data = api_list;
        String Avg;
        //모양 piechart
        dataBinding.pieChart.clearChart();
        dataBinding.tvPiechart1.setText(data.getCountRecords().toString() + "회");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get딱딱());
        dataBinding.tvPie11.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get단단());
        dataBinding.tvPie12.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get건조());
        dataBinding.tvPie13.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get매끈());
        dataBinding.tvPie14.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get물렁());
        dataBinding.tvPie15.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get찰흙());
        dataBinding.tvPie16.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getShape().get물());
        dataBinding.tvPie17.setText(" " + Avg + "%");


        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get딱딱(), Color.parseColor("#ff6f74")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get단단(), Color.parseColor("#ffa557")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get건조(), Color.parseColor("#ffe65f")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get매끈(), Color.parseColor("#df74ff")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get물렁(), Color.parseColor("#88edff")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get찰흙(), Color.parseColor("#f5ff7e")));
        dataBinding.pieChart.addPieSlice(new PieModel("", data.getStatistic().getShape().get물(), Color.parseColor("#bfbfbf")));

        dataBinding.pieChart2.clearChart();

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get갈색());
        dataBinding.tvPie21.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get회색());
        dataBinding.tvPie22.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get노란색());
        dataBinding.tvPie23.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get빨간색());
        dataBinding.tvPie24.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get검정색());
        dataBinding.tvPie25.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getColor().get초록색());
        dataBinding.tvPie26.setText(" " + Avg + "%");

        dataBinding.tvPiechart2.setText(data.getCountRecords().toString() + "회");
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get갈색(), Color.parseColor("#cb5f00")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get회색(), Color.parseColor("#d3c9c1")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get노란색(), Color.parseColor("#d78e00")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get빨간색(), Color.parseColor("#cb3c00")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get검정색(), Color.parseColor("#45403d")));
        dataBinding.pieChart2.addPieSlice(new PieModel("", data.getStatistic().getColor().get초록색(), Color.parseColor("#496108")));

        dataBinding.pieChart3.clearChart();

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get특이사항_없음());
        dataBinding.tvPie31.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get시큼());
        dataBinding.tvPie32.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get생선());
        dataBinding.tvPie33.setText(" " + Avg + "%");

        Avg = percent(data.getPercent(), data.getStatistic().getSmell().get지독());
        dataBinding.tvPie34.setText(" " + Avg + "%");

        dataBinding.tvPiechart3.setText(data.getCountRecords().toString() + "회");
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get특이사항_없음(), Color.parseColor("#ff8e92")));
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get시큼(), Color.parseColor("#ffb97c")));
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get생선(), Color.parseColor("#ffef98")));
        dataBinding.pieChart3.addPieSlice(new PieModel("", data.getStatistic().getSmell().get지독(), Color.parseColor("#edb2ff")));
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
        Legend legend = a.getLegend(); //레전드 설정 (차트 밑에 색과 라벨을 나타내는 설정)
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
                Log.d("성공");
                if (result.getData().getCountRecords() != 0) {
                    dataBinding.ivNoStatitic.setVisibility(View.GONE);
                    dataBinding.llStatistic.setVisibility(View.VISIBLE);
                    Statistic data = result.getData();
                    dataBinding.tvScore.setText(result.getData().getAverage().get(0).getAverage().toString() + "점");
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
                Log.d("!!!!!잘못됨!!");

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
        for (int i = 0; i < num.size(); i++) //for문을 배열개수만큼 돌림
        {
            if (max < num.get(i)) {
                max = num.get(i);
            }
        }
        return max;
    }
}