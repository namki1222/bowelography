package com.rightcode.bowelography.dialog;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.PhotoGuideActivity;
import com.rightcode.bowelography.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CameraPopupDialog extends Activity {
    String camera_string=null;

    ImageView emote1,emote2;
    TextView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView(R.layout.activity_camera_popup);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        emote1 = findViewById(R.id.camera_emote_1);
        emote2 = findViewById(R.id.camera_emote_2);
        start = findViewById(R.id.tv_start);
    }
    public void favorite_click(View view) {
        switch (view.getId()) {
            case R.id.camera_emote_1:
                if (!view.isSelected()) {
                    camera_string = "emote_1";
                    emote2.setSelected(false);
                    view.setSelected(true);
                } else {
                    camera_string = null;
                    view.setSelected(false);
                }
                break;
            case R.id.camera_emote_2:
                if (!view.isSelected()) {
                    camera_string = "emote_2";
                    emote1.setSelected(false);
                    view.setSelected(true);
                } else {
                    camera_string = null;
                    view.setSelected(false);
                }
                break;
            case R.id.tv_start:
                Log.d(camera_string+"!!!!!!");
                if (camera_string==null) {
                    finish();
                }else if(camera_string.equals("emote_1")) {
                    Intent intent = new Intent(this, PhotoGuideActivity.class);
                    startActivityForResult(intent, 5000);
                    setResult(RESULT_OK, intent);
                }else if(camera_string.equals("emote_2")) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, 6000);
                }
                break;
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case 5000:
                    Bitmap bm = (Bitmap) data.getExtras().get("sendText");
                    Intent intentR = new Intent();
                    intentR.putExtra("guide" , bm); //사용자에게 입력받은값 넣기
                    setResult(RESULT_OK,intentR); //결과를 저장
                    finish();
                    break;
                case 6000:
                    Uri selectedImageUri = data.getData();
                    Intent intent = new Intent();
                    intent.putExtra("album" , selectedImageUri); //사용자에게 입력받은값 넣기
                    setResult(RESULT_OK,intent); //결과를 저장
                    finish();
                    break;
            }
        }
    }
}