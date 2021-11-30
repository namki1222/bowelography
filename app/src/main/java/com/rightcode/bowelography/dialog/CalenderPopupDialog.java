package com.rightcode.bowelography.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.PhotoGuideActivity;
import com.rightcode.bowelography.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CalenderPopupDialog extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_calender_popup);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView thumnail_iv = findViewById(R.id.camera_emote_1);

        String thumnail = getIntent().getStringExtra("thumbnail");
        Glide.with(this).load(thumnail).override(Target.SIZE_ORIGINAL).apply(new RequestOptions().transform(new RoundedCorners(100))).into(thumnail_iv);

    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

}