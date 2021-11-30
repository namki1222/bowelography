package com.rightcode.bowelography.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.BaseActivity;
import com.rightcode.bowelography.adapter.FaqRecyclerViewAdapter;
import com.rightcode.bowelography.adapter.NoticeRecyclerViewAdapter;
import com.rightcode.bowelography.databinding.ActivityFaqBinding;
import com.rightcode.bowelography.fragment.HomeFragment;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.FaqResponse;
import com.rightcode.bowelography.network.Response.UserIdresponse;
import com.rightcode.bowelography.network.model.FAQ;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

import java.util.ArrayList;

public class FAQActivity extends BaseActivity<ActivityFaqBinding> {

    FragmentManager fm;
    FaqRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_faq);
    }

    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("FAQ", view -> finishWithAnim(), "LEFT");

        ArrayList<FAQ> list = new ArrayList<>();
        adapter = new FaqRecyclerViewAdapter(mContext,list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        dataBinding.rvList.setLayoutManager(layoutManager);
        dataBinding.rvList.setAdapter(adapter);
        changeFAQContent("데이터");
        fm = getSupportFragmentManager();
        dataBinding.tvData.setSelected(true);

    }


    @Override
    protected void initClickListener() {
        dataBinding.swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.notifyDataSetChanged();
                dataBinding.swipeLayout.setRefreshing(false);
            }
        });
        dataBinding.tvData.setOnClickListener(view->{
            if(view.isSelected()){
                view.setSelected(false);
            }else{
                view.setSelected(true);
                changeFAQContent("데이터");
                dataBinding.tvCamera.setSelected(false);
                dataBinding.tvReport.setSelected(false);
                dataBinding.tvPay.setSelected(false);
                dataBinding.tvAnother.setSelected(false);
            }

        });
        dataBinding.tvCamera.setOnClickListener(view->{
            if(view.isSelected()){
                view.setSelected(false);
            }else{
                view.setSelected(true);
                changeFAQContent("기록/촬영");
                dataBinding.tvData.setSelected(false);
                dataBinding.tvReport.setSelected(false);
                dataBinding.tvPay.setSelected(false);
                dataBinding.tvAnother.setSelected(false);
            }

        });
        dataBinding.tvReport.setOnClickListener(view->{
            if(view.isSelected()){
                view.setSelected(false);
            }else{
                view.setSelected(true);
                changeFAQContent("분석/보고서");
                dataBinding.tvCamera.setSelected(false);
                dataBinding.tvData.setSelected(false);
                dataBinding.tvPay.setSelected(false);
                dataBinding.tvAnother.setSelected(false);
            }

        });
        dataBinding.tvPay.setOnClickListener(view->{
            if(view.isSelected()){
                view.setSelected(false);
            }else{
                view.setSelected(true);
                changeFAQContent("결제");
                dataBinding.tvCamera.setSelected(false);
                dataBinding.tvReport.setSelected(false);
                dataBinding.tvData.setSelected(false);
                dataBinding.tvAnother.setSelected(false);
            }

        });
        dataBinding.tvAnother.setOnClickListener(view->{
            if(view.isSelected()){
                view.setSelected(false);
            }else{
                view.setSelected(true);
                changeFAQContent("기타");
                dataBinding.tvCamera.setSelected(false);
                dataBinding.tvReport.setSelected(false);
                dataBinding.tvPay.setSelected(false);
                dataBinding.tvData.setSelected(false);
            }

        });


    }
    private void connectAdapter(ArrayList<FAQ> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
    public void changeFAQContent(String change){
        callApi(NetworkManager.getInstance(mContext).getApiService().faq_reload(0,0,change), new ApiResponseHandler<FaqResponse>() {
            @Override
            public void onSuccess(FaqResponse result) {
                connectAdapter(result.getList());
                Log.d("통신성공");

            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(FaqResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
}