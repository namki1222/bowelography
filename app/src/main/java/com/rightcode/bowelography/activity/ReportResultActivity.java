package com.rightcode.bowelography.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.kakao.sdk.link.LinkClient;
import com.kakao.sdk.template.Button;
import com.kakao.sdk.template.Content;
import com.kakao.sdk.template.FeedTemplate;
import com.kakao.sdk.template.Link;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityReportResultBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.model.data;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportResultActivity extends BaseActivity<ActivityReportResultBinding> {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    int score_result = 0;
    String contents = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_report_result);
    }
    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("분석결과", view -> finishWithAnim(),"RIGHT");
        score_result = getIntent().getIntExtra("score",0);
        contents = getIntent().getStringExtra("contents");
        dataBinding.tvResultScore.setText(score_result+"점");
        dataBinding.tvTextContent.setText(contents);

    }

    @Override
    protected void initClickListener() {
        dataBinding.tvStart.setOnClickListener(view->{
            verifyStoragePermissions(this);
        });

    }
    public void captureShare(){
        dataBinding.captureView.buildDrawingCache();
        Bitmap captureview = dataBinding.captureView.getDrawingCache();

        Log.d(captureview);
//        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        Uri uri = getImageUri(this, captureview);
//        intent.setType("image/*");
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
//        startActivity(chooser);
        shareToKakaoTalk(uri);

//

//        try{
//
//            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/capture.jpg");
//            captureview.compress(Bitmap.CompressFormat.JPEG,100,fos);
//            fos.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Toast.makeText(getApplicationContext(), "Capture", Toast.LENGTH_LONG).show();
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        sharingIntent.setType("image/jpg");
//        Uri uri = Uri.parse("file://" + Environment.getExternalStorageDirectory().toString()+"/capture.jpeg");
//        sharingIntent.putExtra(Intent.EXTRA_STREAM,uri);
//        startActivity(Intent.createChooser(sharingIntent,"Share 팝업"));
    }

    private Uri getImageUri(Context context, Bitmap inImage){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                context.getContentResolver(),
                inImage,
                "Title",
                null
        );
        return Uri.parse(path);
    }
    public  void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
        if(permission == PackageManager.PERMISSION_GRANTED)
            captureShare();
    }
    private void shareToKakaoTalk(Uri uri) {
        List<Button> buttons = new ArrayList<>();
        buttons.add(new Button("앱으로 보기", new Link("https://developers.kakao.com", "https://developers.kakao.com")));
        FeedTemplate feedTemplate = new FeedTemplate(new Content("오늘의 배변상태!",uri.toString(),    //메시지 제목, 이미지 url
                new Link("market://details?id=com.rightcode.bowelography"),"오늘의 배변 상태입니다!",                   //메시지 링크, 메시지 설명
                300,300),null,buttons,"앱 설치하기");
        LinkClient.getInstance().defaultTemplate(mContext, feedTemplate, (linkResult, throwable) -> {
            if (throwable != null) {
                ToastUtil.show(mContext, "공유하기 실패");
                throwable.printStackTrace();
            } else if (linkResult != null) {
                Log.d(linkResult.getIntent());
                mContext.startActivity(linkResult.getIntent());
            }

            return null;
        });
    }
}