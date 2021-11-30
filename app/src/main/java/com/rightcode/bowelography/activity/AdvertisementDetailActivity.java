package com.rightcode.bowelography.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.BaseActivity;
import com.rightcode.bowelography.adapter.AdvertisementDetailViewAdapter;
import com.rightcode.bowelography.adapter.CalendarReportViewAdapter;
import com.rightcode.bowelography.databinding.ActivityAdvertisementDetailBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.AdvertisementdetailResponse;
import com.rightcode.bowelography.network.Response.CalendarCondtionResponse;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

public class AdvertisementDetailActivity extends BaseActivity<ActivityAdvertisementDetailBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_advertisement_detail);
    }

    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("광고상세", view -> finishWithAnim(),"LEFT");
        int id = getIntent().getIntExtra("id",0);
        detailApi(id);

    }

    @Override
    protected void initClickListener() {

    }
    public void detailApi(int id){
        callApi(NetworkManager.getInstance(mContext).getApiService().ad_detail(id), new ApiResponseHandler<AdvertisementdetailResponse>() {
            @Override
            public void onSuccess(AdvertisementdetailResponse result) {
                if (result.getData()!= null) {
                    if(result.getData().getDetailImage()==null){
                        dataBinding.webview.setVisibility(View.VISIBLE);
                        dataBinding.ivThumbnail.setVisibility(View.GONE);
                        dataBinding.webview.loadUrl(result.getData().getUrl());
                    }else{
                        Glide.with(mContext).load(result.getData().getDetailImage()).into(dataBinding.ivThumbnail);
//                        AdvertisementDetailViewAdapter adapter = new AdvertisementDetailViewAdapter(mContext,result.getData());
//                        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
//                        dataBinding.rvList.setLayoutManager(layoutManager);
//                        dataBinding.rvList.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.getMessage());
            }

            @Override
            public void onNoResponse(AdvertisementdetailResponse result) {
                ToastUtil.show(mContext,result.getMessage());
            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
}