package com.rightcode.bowelography.activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityIntroBinding;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;

public class IntroActivity extends BaseActivity<ActivityIntroBinding> {
    Handler mHandler;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_intro);
    }
    @Override
    protected void initActivity() {
        mHandler = new Handler();
        mHandler.postDelayed(()->{
            String Token = PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Token, "");
            if(Token == ""||Token == null){
                Log.d(Token);
                Intent intent = new Intent(this, GuideActivity.class);
                intent.putExtra("activity","survey");
                startActivity(intent); //다음 액티비티 이동
                finish();
            }else{
                Log.d(Token);
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent); //다음 액티비티 이동
                finish();
            }
        }, 4000); //2000은 2초를 의미한다.
    }

    @Override
    protected void initClickListener() {

    }
}