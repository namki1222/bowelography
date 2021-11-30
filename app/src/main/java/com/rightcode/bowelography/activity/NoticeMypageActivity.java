package com.rightcode.bowelography.activity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.adapter.NoticeRecyclerViewAdapter;
import com.rightcode.bowelography.databinding.ActivityNoticeMypageBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.NoticeResponse;
import com.rightcode.bowelography.network.model.FAQ;
import com.rightcode.bowelography.network.model.notice;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

import java.util.ArrayList;

public class NoticeMypageActivity extends BaseActivity<ActivityNoticeMypageBinding> {

    NoticeRecyclerViewAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_notice_mypage);
    }
    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("공지사항", view -> finishWithAnim(),"LEFT");
        ArrayList<notice> data = new ArrayList<>();


        adapter = new NoticeRecyclerViewAdapter(mContext,data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        dataBinding.rvList.setLayoutManager(layoutManager);
        dataBinding.rvList.setAdapter(adapter);
        String activity = getIntent().getStringExtra("activity");
        if(activity == null){
            noticeReload();
        }else if(activity.equals("personal")){
            noticeReloadPersonal();
        }
    }
    public void setAdapter(ArrayList<notice> data){
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void initClickListener() {
        dataBinding.swipeLayout.setOnRefreshListener(() -> {
            noticeReload();
            dataBinding.swipeLayout.setRefreshing(false);
        });
    }
    public void noticeReload(){
        callApi(NetworkManager.getInstance(mContext).getApiService().notice_reload_list(0,0), new ApiResponseHandler<NoticeResponse>() {
            @Override
            public void onSuccess(NoticeResponse result) {
                if(!result.getList().isEmpty()){
                    setAdapter(result.getList());
                }
                Log.d(result.getList());
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onNoResponse(NoticeResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
    public void noticeReloadPersonal(){
        callApi(NetworkManager.getInstance(mContext).getApiService().notice_reload_user_list(0,0), new ApiResponseHandler<NoticeResponse>() {
            @Override
            public void onSuccess(NoticeResponse result) {
                if(!result.getList().isEmpty()){
                    setAdapter(result.getList());
                }
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onNoResponse(NoticeResponse result) {
                ToastUtil.show(mContext,result.message);
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
}