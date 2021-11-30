package com.rightcode.bowelography.activity;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewPagerAdapter.PhotoGuideViewPagerAdapter;
import com.rightcode.bowelography.databinding.ActivityPhotoGuideBinding;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import lombok.NonNull;

public class PhotoGuideActivity extends BaseActivity<ActivityPhotoGuideBinding> {

    PreferenceUtil pref = PreferenceUtil.getInstance(mContext);
    String  nextActivity;
    private final Integer[] images = {R.drawable.camera_guide1, R.drawable.camera_guide2, R.drawable.guide_3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_photo_guide);
    }
    @Override
    protected void initActivity() {
        ArrayList<Integer> image = new ArrayList<>(Arrays.asList(images));
        PhotoGuideViewPagerAdapter adapter = new PhotoGuideViewPagerAdapter(mContext, image);
        dataBinding.vpGuide.setAdapter(adapter);
        dataBinding.indicator.setViewPager2(dataBinding.vpGuide);
        nextActivity = getIntent().getStringExtra("activity");
    }
    @Override
    protected void initClickListener() {


        dataBinding.tvStart.setOnClickListener(view -> {
            if(nextActivity.equals("mypage")){
                finishWithAnim();
                nextActivity = null;
            }else{
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "권한 설정 완료");
                        String photo_ai = getIntent().getStringExtra("photo");
                        Intent intent = new Intent(mContext, CameraActivity.class);
                        intent.putExtra("photo" , photo_ai); //사용자에게 입력받은값 넣기
                        startActivityForResult(intent,2000);
                    } else {
                        Log.d(TAG, "권한 설정 요청");
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
            }
        });
        dataBinding.tvExit.setOnClickListener(view -> {
            finishWithAnim();
        });
        dataBinding.llCheckbox.setOnClickListener(view->{
            if(dataBinding.ivCheckbox.isSelected()){
                pref.put(PreferenceUtil.PreferenceKey.Guid_photo_boolean,false);
                dataBinding.ivCheckbox.setSelected(false);
            }else{
                pref.put(PreferenceUtil.PreferenceKey.Guid_photo_boolean,true);
                dataBinding.ivCheckbox.setSelected(true);
            }

        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == 0) {
                String photo_ai = getIntent().getStringExtra("photo");
                Intent intent = new Intent(mContext, CameraActivity.class);
                intent.putExtra("photo" , photo_ai); //사용자에게 입력받은값 넣기
                startActivityForResult(intent,2000);
            } else {
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 2000) {
                File photo = (File) data.getExtras().get("guide");
                String ai_color = (String) data.getExtras().get("color");
                String ai_shape = (String) data.getExtras().get("shape");
                Float ai_blood = (Float) data.getExtras().get("blood");
                Log.d(photo + "!!!!");
                Intent intentR = new Intent();
                intentR.putExtra("blood", ai_blood);
                intentR.putExtra("shape", ai_shape);
                intentR.putExtra("color", ai_color);
                intentR.putExtra("guide", photo);//사용자에게 입력받은값 넣기
                setResult(RESULT_OK, intentR); //결과를 저장
                finishWithAnim();
            }
        }
    }
}