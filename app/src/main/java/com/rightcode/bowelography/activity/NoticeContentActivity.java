package com.rightcode.bowelography.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.BaseActivity;
import com.rightcode.bowelography.databinding.ActivityNoticeContentBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.ExtraReportresponse;
import com.rightcode.bowelography.network.Response.NoticeContentResponse;
import com.rightcode.bowelography.network.Response.NoticeResponse;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

import java.text.SimpleDateFormat;

public class NoticeContentActivity extends BaseActivity<ActivityNoticeContentBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_notice_content);
    }

    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("공지사항", view -> finishWithAnim(),"LEFT");
        reload();
    }

    @Override
    protected void initClickListener() {
        dataBinding.swipeLayout.setOnRefreshListener(() -> {
            reload();
            dataBinding.swipeLayout.setRefreshing(false);
        });
    }

    public void reload(){
        Integer data = getIntent().getIntExtra("id",0);
        Log.d(data);
        callApi(NetworkManager.getInstance(mContext).getApiService().notice_reload(data), new ApiResponseHandler<NoticeContentResponse>() {
            @Override
            public void onSuccess(NoticeContentResponse result) {
                dataBinding.tvTitle.setText(result.getData().getSubject());
                dataBinding.tvContent.setText(result.getData().getContent());
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(NoticeContentResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
}