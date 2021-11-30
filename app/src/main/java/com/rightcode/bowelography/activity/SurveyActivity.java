package com.rightcode.bowelography.activity;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivitySurveyBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Request.JoinRequest;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.model.fcmrequest;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;
import com.rightcode.bowelography.util.ToastUtil;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SurveyActivity extends BaseActivity<ActivitySurveyBinding>{
    Integer Age = null; //나이 받아놓은 곳
    String Hasill = "";
    String ill = "";
    String secondill = "";
    String concern = "";  // 고민 받아놓은 곳
    JoinRequest joinRequestBody = new JoinRequest();

    //버튼 입력된 값들 넣어둠

    int select_text_color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_survey);
    }

    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("", view -> finishWithAnim(),"LEFT");
        select_text_color = ContextCompat.getColor(mContext, R.color.white);
    }


    @Override
    protected void initClickListener() {}
    public void onButtonClick(View view) {
        switch (view.getId()) {
            case R.id.tv_start: {
                if(dataBinding.tvDisease1.isSelected()){
                    if(ill.equals("")){

                        ill = dataBinding.tvDisease1.getText().toString();
                    }else{
                        ill = ill +"," +dataBinding.tvDisease1.getText().toString();
                    }
                }if(dataBinding.tvDisease2.isSelected()){
                    if(ill.equals("")){
                        ill = dataBinding.tvDisease2.getText().toString();
                    }else{
                        ill = ill + "," +dataBinding.tvDisease2.getText().toString();
                    }
                }if(dataBinding.tvDisease3.isSelected()){
                    if(ill.equals("")){
                        ill = dataBinding.tvDisease3.getText().toString();
                    }else{
                        ill = ill + "," +dataBinding.tvDisease3.getText().toString();
                    }
                }if(dataBinding.tvDisease4.isSelected()){
                    if(ill.equals("")){
                        ill = dataBinding.tvDisease4.getText().toString();
                    }else{
                        ill = ill + "," +dataBinding.tvDisease4.getText().toString();
                    }
                }if(dataBinding.tvDisease5.isSelected()){
                    if(ill.equals("")){
                        ill = dataBinding.tvDisease5.getText().toString();
                    }else{
                        ill = ill + "," +dataBinding.tvDisease5.getText().toString();
                    }
                }if(dataBinding.tvDisease11.isSelected()){
                    if(secondill.equals("")){
                        secondill = dataBinding.tvDisease11.getText().toString();
                    }else{
                        secondill = secondill + "," +dataBinding.tvDisease11.getText().toString();
                    }
                }if(dataBinding.tvDisease21.isSelected()){
                    if(secondill.equals("")){
                        secondill = dataBinding.tvDisease21.getText().toString();
                    }else{
                        secondill = secondill + "," +dataBinding.tvDisease21.getText().toString();
                    }
                }if(dataBinding.tvDisease31.isSelected()){
                    if(secondill.equals("")){
                        secondill = dataBinding.tvDisease31.getText().toString();
                    }else{
                        secondill = secondill + "," +dataBinding.tvDisease31.getText().toString();
                    }
                }if(dataBinding.tvDisease41.isSelected()){
                    if(secondill.equals("")){
                        secondill = dataBinding.tvDisease41.getText().toString();
                    }else{
                        secondill = secondill + "," +dataBinding.tvDisease41.getText().toString();
                    }
                }if(dataBinding.tvDisease51.isSelected()){
                    if(secondill.equals("")){
                        secondill = dataBinding.tvDisease51.getText().toString();
                    }else{
                        secondill = secondill + "," +dataBinding.tvDisease51.getText().toString();
                    }
                }if(dataBinding.tvWorryWater.isSelected()){
                    if(concern.equals("")){
                        concern = dataBinding.tvWorryWater.getText().toString();
                    }else{
                        concern = concern + "," +dataBinding.tvWorryWater.getText().toString();
                    }
                }if(dataBinding.tvWorryDiet.isSelected()){
                    if(concern.equals("")){
                        concern = dataBinding.tvWorryDiet.getText().toString();
                    }else{
                        concern = concern + "," +dataBinding.tvWorryDiet.getText().toString();
                    }
                }if(dataBinding.tvWorryGas.isSelected()){
                    if(concern.equals("")){
                        concern = dataBinding.tvWorryGas.getText().toString();
                    }else{
                        concern = concern + "," +dataBinding.tvWorryGas.getText().toString();
                    }
                }if(dataBinding.tvWorryIndigestion.isSelected()){
                    if(concern.equals("")){
                        concern = dataBinding.tvWorryIndigestion.getText().toString();
                    }else{
                        concern = concern + "," +dataBinding.tvWorryIndigestion.getText().toString();
                    }
                }if(dataBinding.tvWorryNoPoop.isSelected()){
                    if(concern.equals("")){
                        concern = dataBinding.tvWorryNoPoop.getText().toString();
                    }else{
                        concern = concern + "," +dataBinding.tvWorryNoPoop.getText().toString();
                    }
                }
                if(dataBinding.etOld.length()==0){
                    ToastUtil.show(mContext,"나이를 입력해주세요!");
                }else{
                    Age = Integer.parseInt(dataBinding.etOld.getText().toString());
                }
                joinRequestBody.setAge(Age);
                joinRequestBody.setHasIll(Hasill);
                if(Hasill.equals("아니오")){
                    joinRequestBody.setIll(null);
                    joinRequestBody.setOtherIll(null);
                    joinRequestBody.setSecondill(null);
                    joinRequestBody.setSecondotherIll(null);
                }else{
                    joinRequestBody.setIll(ill);
                    joinRequestBody.setOtherIll(dataBinding.etDisease.getText().toString());
                    joinRequestBody.setSecondill(secondill);
                    joinRequestBody.setSecondotherIll(dataBinding.etDisease1.getText().toString());
                }
                joinRequestBody.setConcern(concern);
                joinRequestBody.setOtherConcern(dataBinding.etWorry.getText().toString());
                joinRequestBody.setServiceAgreement(true);
                joinRequestBody.setPrivacyAgreement(true);
                if(dataBinding.checkbox2.isSelected()&&dataBinding.checkbox3.isSelected()&&!(Age==null)){
                    join();
                }
                break;
            }
            case R.id.tv_disease_yes: {
                if(view.isSelected()){
                    view.setSelected(false);
                    dataBinding.llDiseaseVisible.setVisibility(View.GONE);
                    dataBinding.tvDiseaseYes.setSelected(false);
                    Hasill = null;
                }else{
                    view.setSelected(true);
                    dataBinding.llDiseaseVisible.setVisibility(View.VISIBLE);
                    dataBinding.tvDiseaseNo.setSelected(false);
                }
                if(dataBinding.tvDiseaseYes.isSelected()&&dataBinding.tvDiseaseDoubt.isSelected()){
                    Hasill = "예,의심";
                }else if(!dataBinding.tvDiseaseYes.isSelected()&&dataBinding.tvDiseaseDoubt.isSelected()){
                    Hasill = "의심";
                }else if(dataBinding.tvDiseaseYes.isSelected()&&!dataBinding.tvDiseaseDoubt.isSelected()){
                    Hasill = "예";
                }else{
                    Hasill = null;
                }
                break;
            }
            case R.id.tv_disease_doubt: {
                if(view.isSelected()){
                    view.setSelected(false);
                    dataBinding.llDiseaseVisible1.setVisibility(View.GONE);
                    dataBinding.tvDiseaseDoubt.setSelected(false);
                    Hasill = null;
                }else{
                    view.setSelected(true);
                    dataBinding.llDiseaseVisible1.setVisibility(View.VISIBLE);
                    dataBinding.tvDiseaseNo.setSelected(false);
                }

                if(dataBinding.tvDiseaseYes.isSelected()&&dataBinding.tvDiseaseDoubt.isSelected()){
                    Hasill = "예,의심";
                }else if(!dataBinding.tvDiseaseYes.isSelected()&&dataBinding.tvDiseaseDoubt.isSelected()){
                    Hasill = "의심";
                }else if(dataBinding.tvDiseaseYes.isSelected()&&!dataBinding.tvDiseaseDoubt.isSelected()){
                    Hasill = "예";
                }else{
                    Hasill = null;
                }
                break;
            }
            case R.id.tv_disease_no: {
                if(dataBinding.tvDiseaseNo.isSelected()){
                    view.setSelected(false);
                    Hasill = null;
                }else if(!dataBinding.tvDiseaseNo.isSelected()){
                    view.setSelected(true);
                    dataBinding.tvDiseaseYes.setSelected(false);
                    dataBinding.tvDiseaseDoubt.setSelected(false);
                    dataBinding.llDiseaseVisible.setVisibility(View.GONE);
                    dataBinding.llDiseaseVisible1.setVisibility(View.GONE);
                    Hasill = "아니오";

                }
                break;
            }
            case R.id.tv_worry_water:
            case R.id.tv_disease_2:
            case R.id.tv_disease_1:
            case R.id.tv_disease_3:
            case R.id.tv_disease_4:
            case R.id.tv_disease_5:
            case R.id.tv_disease_1_1:
            case R.id.tv_disease_2_1:
            case R.id.tv_disease_3_1:
            case R.id.tv_disease_4_1:
            case R.id.tv_disease_5_1:
            case R.id.tv_worry_indigestion:
            case R.id.tv_worry_no_poop:
            case R.id.tv_worry_gas:
            case R.id.tv_worry_diet:
                buttonSelect(view);
                break;
            case R.id.checkbox_1:
                if(!view.isSelected()){
                    dataBinding.checkbox1.setSelected(true);
                    dataBinding.checkbox2.setSelected(true);
                    dataBinding.checkbox3.setSelected(true);
                    dataBinding.tvStart.setSelected(true);
                }else if(view.isSelected()){
                    dataBinding.checkbox1.setSelected(false);
                    dataBinding.checkbox2.setSelected(false);
                    dataBinding.checkbox3.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                }
                break;
            case R.id.checkbox_2:
            case R.id.checkbox_3:
                if(!view.isSelected()){
                    view.setSelected((true));
                }else if(view.isSelected()){
                    view.setSelected((false));
                    dataBinding.checkbox1.setSelected(false);
                }
                if(dataBinding.checkbox2.isSelected()&&dataBinding.checkbox3.isSelected()) {
                    dataBinding.tvStart.setSelected(true);
                }else {
                    dataBinding.tvStart.setSelected(false);
                }
                break;
            case R.id.tv_service_1:
                Intent intent = new Intent(mContext, ServiceNoticeActivity.class);
                intent.putExtra("id",1);
                startActivity(intent);
                break;
            case R.id.tv_service_2:
                Intent intent_2 = new Intent(mContext, ServiceNoticeActivity.class);
                intent_2.putExtra("id",0);
                startActivity(intent_2);
                break;
        }
    }
    private void join() {
        callApi(NetworkManager.getInstance(mContext).getApiService().join(joinRequestBody), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                PreferenceUtil pref = PreferenceUtil.getInstance(mContext);
                pref.put(PreferenceUtil.PreferenceKey.Token,result.token);
                getFCMToken();
                Intent intent = new Intent(mContext, MainActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                ToastUtil.show(mContext,t.getMessage());

            }
        });
    }
    public void buttonSelect(View view){
        if (!view.isSelected()) {
            view.setSelected(true);
        } else {
            view.setSelected(false);
        }
    }
    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("getting token fail");
                return;
            }
            String token = task.getResult();
            Log.d("FCM 토큰!!"+token);
            notificationTokenRegister(token);
        });
    }

    private void notificationTokenRegister(String token) {
        fcmrequest body = new fcmrequest();
        body.setPushToken(token);
        callApi(NetworkManager.getInstance(mContext).getApiService().notificationRegister(body), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                PreferenceUtil.getInstance(mContext).put(PreferenceUtil.PreferenceKey.FCMToken, token);
                Log.d("FCM Token register success " + token);
            }
            @Override
            public void onServerFail(CommonResponse result) {
            }
            @Override
            public void onNoResponse(CommonResponse result) {
            }
            @Override
            public void onBadRequest(Throwable t) {

            }
        });
    }
}