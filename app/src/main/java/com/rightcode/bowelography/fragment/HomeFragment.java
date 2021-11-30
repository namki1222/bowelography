package com.rightcode.bowelography.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewPagerAdapter.AdvertiseViewPagerAdapter;
import com.rightcode.bowelography.ViewPagerAdapter.PhotoGuideViewPagerAdapter;
import com.rightcode.bowelography.activity.CalenderActivity;
import com.rightcode.bowelography.activity.GuideActivity;
import com.rightcode.bowelography.activity.ReportActivity;
import com.rightcode.bowelography.activity.ReportExtraActivity;
import com.rightcode.bowelography.activity.StatisticsActivity;
import com.rightcode.bowelography.databinding.FragmentHomeBinding;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Request.ConditionRequest;
import com.rightcode.bowelography.network.Request.ConditionRequest_2;
import com.rightcode.bowelography.network.Response.AdvertisementResponse;
import com.rightcode.bowelography.network.Response.CalendarCondtionResponse;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.UserIdresponse;
import com.rightcode.bowelography.network.model.advertisement;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;
import com.rightcode.bowelography.util.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends BaseFragment<FragmentHomeBinding> {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int date = cal.get(Calendar.DAY_OF_MONTH);
    boolean change = false;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    long mNow;
    private Date mDate;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 8000;//처음 시작시 6초딜레
    final long PERIOD_MS = 5000; // 4초에 한번씩 이


    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        load_home(year, month + 1, date);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return bindView(R.layout.fragment_home, inflater, container);
    }

    @Override
    protected void initBinding() {
        binding.tvToday.setText(gettime());
    }

    @Override
    protected void initFragment() {
        ad_home_banner();
    }

    @Override
    protected void initClickListener() {
        binding.tvAddCondition.setOnClickListener(view -> {
            if (change) {
                Toast.makeText(activity, "몸무게를 수정하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(activity, "몸무게를 추가하였습니다.", Toast.LENGTH_SHORT).show();

                change = true;
                binding.tvAddCondition.setSelected(true);
                binding.tvAddCondition.setText("+수정");
            }
            add_condition_2();
        });
        binding.ivEmote1.setOnClickListener(view -> {
            if (!view.isSelected()) {
                view.setSelected(true);
                binding.ivEmote2.setSelected(false);
                binding.ivEmote3.setSelected(false);
                binding.ivEmote4.setSelected(false);
                binding.ivEmote5.setSelected(false);
                add_condition("우울");
            } else {
                view.setSelected(false);
                add_condition(null);

            }
        });
        binding.ivEmote2.setOnClickListener(view -> {
            if (!view.isSelected()) {
                view.setSelected(true);
                binding.ivEmote1.setSelected(false);
                binding.ivEmote3.setSelected(false);
                binding.ivEmote4.setSelected(false);
                binding.ivEmote5.setSelected(false);
                add_condition("짜증");
            } else {
                view.setSelected(false);
                add_condition(null);
            }
        });
        binding.ivEmote3.setOnClickListener(view -> {
            if (!view.isSelected()) {
                view.setSelected(true);
                binding.ivEmote2.setSelected(false);
                binding.ivEmote1.setSelected(false);
                binding.ivEmote4.setSelected(false);
                binding.ivEmote5.setSelected(false);
                add_condition("보통");
            } else {
                view.setSelected(false);
                add_condition(null);
            }
        });
        binding.ivEmote4.setOnClickListener(view -> {
            if (!view.isSelected()) {
                view.setSelected(true);
                binding.ivEmote2.setSelected(false);
                binding.ivEmote3.setSelected(false);
                binding.ivEmote1.setSelected(false);
                binding.ivEmote5.setSelected(false);
                add_condition("신남");
            } else {
                view.setSelected(false);
                add_condition(null);
            }
        });
        binding.ivEmote5.setOnClickListener(view -> {
            if (!view.isSelected()) {
                view.setSelected(true);
                binding.ivEmote2.setSelected(false);
                binding.ivEmote3.setSelected(false);
                binding.ivEmote4.setSelected(false);
                binding.ivEmote1.setSelected(false);
                add_condition("행복");
            } else {
                view.setSelected(false);
                add_condition(null);
            }
        });
        binding.ivReport.setOnClickListener(view -> {
            Intent intent = new Intent(activity, ReportActivity.class);
            startActivity(intent);
        });
        binding.ivCalender.setOnClickListener(view -> {
            Intent intent = new Intent(activity, CalenderActivity.class);
            startActivity(intent);
        });
        binding.ivStatistics.setOnClickListener(view -> {
            Intent intent = new Intent(activity, StatisticsActivity.class);
            startActivity(intent);
        });
    }

    public String gettime() {
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        return mFormat.format(mDate);
    }

    public void add_condition(String condition) {
        ConditionRequest data = new ConditionRequest();

        data.setCondition(condition);
        callApi(NetworkManager.getInstance(activity).getApiService().Condition_add(data), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(activity, result.message);

            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(activity, result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }

    public void add_condition_2() {
        ConditionRequest_2 data = new ConditionRequest_2();
        if (binding.etKg.getText().toString().isEmpty()) {
            data.setWeight(0);
        } else {
            data.setWeight(Integer.parseInt(binding.etKg.getText().toString()));
        }
        callApi(NetworkManager.getInstance(activity).getApiService().Condition_add_2(data), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(activity, result.message);
            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(activity, result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }

    public void ad_home_banner() {
        callApi(NetworkManager.getInstance(activity).getApiService().ad_banner(), new ApiResponseHandler<AdvertisementResponse>() {
            @Override
            public void onSuccess(AdvertisementResponse result) {
                if (result.getList().size() != 0) {
                    ArrayList<advertisement> list = result.getList();
                    AdvertiseViewPagerAdapter adapter = new AdvertiseViewPagerAdapter(activity, result.getList());
                    binding.rvAdvertisement.setAdapter(adapter);
                    auto_viewpager(list.size());
                }
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(activity, result.message);
            }

            @Override
            public void onNoResponse(AdvertisementResponse result) {
                ToastUtil.show(activity, result.message);
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");
            }
        });
    }

    public void auto_viewpager(int count) {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            @Override
            public void run() {
                if (currentPage == count) {
                    currentPage = 0;
                }
                binding.rvAdvertisement.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);
    }

    public void load_home(Integer Year, Integer Month, Integer Day) {

        callApi(NetworkManager.getInstance(activity).getApiService().calendar_condtion(Year, Month, Day), new ApiResponseHandler<CalendarCondtionResponse>() {
            @Override
            public void onSuccess(CalendarCondtionResponse result) {
                Log.d(result + "!!!!!");
                if (result.getData() != null) {
                    if (result.getData().getCondition() != null) {
                        if (result.getData().getCondition().equals("우울")) {
                            binding.ivEmote1.setSelected(true);
                        } else if (result.getData().getCondition().equals("짜증")) {
                            binding.ivEmote2.setSelected(true);
                        } else if (result.getData().getCondition().equals("보통")) {
                            binding.ivEmote3.setSelected(true);
                        } else if (result.getData().getCondition().equals("신남")) {
                            binding.ivEmote4.setSelected(true);
                        } else if (result.getData().getCondition().equals("행복")) {
                            binding.ivEmote5.setSelected(true);
                        }
                    }
                    if (result.getData().getWeight() != null) {
                        binding.etKg.setText(result.getData().getWeight().toString());
                        binding.tvAddCondition.setSelected(true);
                        binding.tvAddCondition.setText("+수정");
                    }
                }
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(activity, result.getMessage());
            }

            @Override
            public void onNoResponse(CalendarCondtionResponse result) {
                ToastUtil.show(activity, result.getMessage());
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
}