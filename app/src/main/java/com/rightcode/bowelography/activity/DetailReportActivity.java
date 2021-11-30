package com.rightcode.bowelography.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityDetailReportBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Request.ConditionRequest_2;
import com.rightcode.bowelography.network.Request.Precision_analysisRequest;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.NoticeContentResponse;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

public class DetailReportActivity extends BaseActivity<ActivityDetailReportBinding> {
    String residence= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_detail_report);
    }

    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("정밀 분석 의뢰", view -> finishWithAnim(),"LEFT");

    }
    public void areaClick(View view){
        switch (view.getId()) {
            case R.id.tv_start:
                send_api();
                break;
            case R.id.tv_area_1:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea1.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_2:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea2.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_3:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea3.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_4:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea4.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_5:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea5.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_6:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea6.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_7:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea7.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_8:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea8.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_9:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea9.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_10:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    dataBinding.tvArea11.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea10.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
            case R.id.tv_area_11:
                if(!view.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvArea2.setSelected(false);
                    dataBinding.tvArea3.setSelected(false);
                    dataBinding.tvArea4.setSelected(false);
                    dataBinding.tvArea5.setSelected(false);
                    dataBinding.tvArea6.setSelected(false);
                    dataBinding.tvArea7.setSelected(false);
                    dataBinding.tvArea8.setSelected(false);
                    dataBinding.tvArea9.setSelected(false);
                    dataBinding.tvArea10.setSelected(false);
                    dataBinding.tvArea1.setSelected(false);
                    residence = null;
                    residence = dataBinding.tvArea11.getText().toString();
                }else{
                    view.setSelected(false);
                    residence = null;
                }
                break;
        }

    }
    @Override
    protected void initClickListener() {
    }



    public void send_api(){
        String email= dataBinding.etEmail.getText().toString();
        String phoneNumber= dataBinding.etPhone.getText().toString();
        Precision_analysisRequest data = new Precision_analysisRequest();
        data.setResidence(residence);
        data.setEmail(email);
        data.setPhoneNumber(phoneNumber);
        Log.d(data.getEmail() +"!!"+data.getPhoneNumber()+"!!"+data.getResidence());
        callApi(NetworkManager.getInstance(mContext).getApiService().precision_analysis(data), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                Toast.makeText(mContext, "정밀 분석 의뢰 요청하였습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onServerFail(CommonResponse result) {
                Toast.makeText(mContext, "빈칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(mContext,result.getMessage());

            }

            @Override
            public void onBadRequest(Throwable t) {
                Toast.makeText(mContext, "인터넷을 연결하고 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            }
        });
    }
}