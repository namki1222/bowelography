package com.rightcode.bowelography.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.rightcode.bowelography.R;

public class FavoritePopupDialog extends Activity {
    String emote_string=null;

    ImageView emote1,emote2,emote3,emote4,emote5;
    TextView start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature( Window.FEATURE_NO_TITLE );

        setContentView(R.layout.activity_favorite_popup);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        emote1 = findViewById(R.id.favorite_emote_1);
        emote2 = findViewById(R.id.favorite_emote_2);
        emote3 = findViewById(R.id.favorite_emote_3);
        emote4 = findViewById(R.id.favorite_emote_4);
        emote5 = findViewById(R.id.favorite_emote_5);
        start = findViewById(R.id.tv_start);

    }
    public void favorite_click(View view) {
        switch (view.getId()) {
            case R.id.favorite_emote_1:
                emote_string = "emote_1";
                if (!view.isSelected()) {
                    emote5.setSelected(false);
                    emote2.setSelected(false);
                    emote3.setSelected(false);
                    emote4.setSelected(false);
                    view.setSelected(true);
                } else {
                    view.setSelected(false);
                }
                break;
            case R.id.favorite_emote_2:
            emote_string = "emote_2";
            if (!view.isSelected()) {
                emote5.setSelected(false);
                emote1.setSelected(false);
                emote3.setSelected(false);
                emote4.setSelected(false);
                view.setSelected(true);
            } else {
                view.setSelected(false);
            }
            break;
            case R.id.favorite_emote_3:
                emote_string = "emote_3";
                if(!view.isSelected()){
                    emote5.setSelected(false);
                    emote2.setSelected(false);
                    emote1.setSelected(false);
                    emote4.setSelected(false);
                    view.setSelected(true);
                }else{
                    view.setSelected(false);
                }
                break;
            case R.id.favorite_emote_4:
                emote_string = "emote_4";
                if(!view.isSelected()){
                    emote5.setSelected(false);
                    emote2.setSelected(false);
                    emote3.setSelected(false);
                    emote1.setSelected(false);
                    view.setSelected(true);
                }else{
                    view.setSelected(false);
                }
                break;
            case R.id.favorite_emote_5:
                emote_string = "emote_5";
                if(!view.isSelected()){
                    emote1.setSelected(false);
                    emote2.setSelected(false);
                    emote3.setSelected(false);
                    emote4.setSelected(false);
                    view.setSelected(true);
                }else{
                    view.setSelected(false);
                }
                break;
            case R.id.tv_start:
                Intent intent = new Intent();
                if(emote_string == null || emote_string.equals("")){
                    finish();
                }else{
                    intent.putExtra("result", emote_string);
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
}