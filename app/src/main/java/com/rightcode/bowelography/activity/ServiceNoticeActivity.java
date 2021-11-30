package com.rightcode.bowelography.activity;

import android.os.Bundle;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityServiceNoticeBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;

import okio.Utf8;

public class ServiceNoticeActivity extends BaseActivity<ActivityServiceNoticeBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_service_notice);
    }
    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("서비스 이용약관", view -> finishWithAnim(), "LEFT");
        dataBinding.wvText.getSettings().setJavaScriptEnabled(true);
        Integer data = getIntent().getIntExtra("id",0);
        if(data == 1){
            dataBinding.wvText.loadUrl("http://15.164.123.1:3000/privacy-agreement.html");
        }else{
            dataBinding.wvText.loadUrl("http://15.164.123.1:3000/privacy.html");
        }
    }
    @Override
    protected void initClickListener() {}
}