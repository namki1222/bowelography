package com.rightcode.bowelography.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.bumptech.glide.Glide;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityResultAnimeBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.ResultResponse;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

public class ResultAnimeActivity extends BaseActivity<ActivityResultAnimeBinding> {
    Handler mHandler;
    int reportUserId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_result_anime);
    }
    @Override
    protected void initActivity() {
        reportUserId = getIntent().getIntExtra("report_id",0);
        scoreApi(reportUserId);

    }

    @Override
    protected void initClickListener() {

    }
    public void scoreApi(int pagd_id){
        callApi(NetworkManager.getInstance(mContext).getApiService().record_result(pagd_id), new ApiResponseHandler<ResultResponse>() {
            @Override
            public void onSuccess(ResultResponse result) {
                Glide.with(mContext).load(R.drawable.bowelofraphy).into(dataBinding.ivAnime);
                mHandler = new Handler();
                mHandler.postDelayed(()->{
                    Intent intent = new Intent(mContext, ReportResultActivity.class);
                    intent.putExtra("score",result.getData().getScore());
                    intent.putExtra("contents",result.getData().getContent());
                    startActivity(intent); //다음 액티비티 이동
                    finish();
                }, 4000); //2000은 2초를 의미한다.
            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.getMessage());

            }

            @Override
            public void onNoResponse(ResultResponse result) {
                ToastUtil.show(mContext,result.getMessage());

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }
}