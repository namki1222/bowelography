package com.rightcode.bowelography.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityReportBinding;
import com.rightcode.bowelography.dialog.DatePickerDialog;
import com.rightcode.bowelography.dialog.NoPhotoDialog;
import com.rightcode.bowelography.dialog.TimerPickDialog;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.network.ApiResponseHandler;
import com.rightcode.bowelography.network.NetworkManager;
import com.rightcode.bowelography.network.Request.JoinRequest;
import com.rightcode.bowelography.network.Request.ReportRequest;
import com.rightcode.bowelography.network.Response.CommonResponse;
import com.rightcode.bowelography.network.Response.UserIdresponse;
import com.rightcode.bowelography.network.model.data;
import com.rightcode.bowelography.util.FragmentHelper;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;
import com.rightcode.bowelography.util.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MultipartBody;

public class ReportActivity extends BaseActivity<ActivityReportBinding> {

    TimerPickDialog timerPickDialog;
    int check = 0;
    Calendar cal = Calendar.getInstance();
    ReportRequest reportRequest = new ReportRequest();
    String time = null;
    File photo = null;
    String shape = null;
    String color = null;
    String mass = null;
    String hematochezia = null;
    String hematocheziaPosition = null;
    String appearanceEtc = null;
    String colic = null;
    String smell = null;
    String elapsedTime = null;
    String etc = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_report);
    }

    @Override
    protected void initActivity() {
        boolean ai = getIntent().getBooleanExtra("ai", false);
        File file = (File) getIntent().getSerializableExtra("photo");
        String ai_color = getIntent().getStringExtra("color");
        String ai_shape = getIntent().getStringExtra("shape");
        float ai_blood = getIntent().getFloatExtra("blood", 0);
        if (ai) {
            autoAi(ai_shape, ai_color, file, ai_blood);
        }
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("기록하기", view -> finishWithAnim(), "LEFT");
        String AM_PM;
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        if (cal.get(Calendar.AM_PM) == 0) {
            AM_PM = "AM";
        } else {
            AM_PM = "PM";
            if (hour == 0)
                hour = 12;
        }
        time = AM_PM + " " + hour + ":" + minute;
        dataBinding.tvTime.setText(AM_PM + " " + hour + ":" + minute);
        dataBinding.ivOutsideInformation.setSelected(true);
        dataBinding.ivOtherInformation.setSelected(true);

        timerPickDialog = new TimerPickDialog(mContext);
        timerPickDialog.setListener(date -> {
            dataBinding.tvTime.setText(date);
            time = dataBinding.tvTime.getText().toString();
        });
    }

    @Override
    protected void initClickListener() {
        dataBinding.timePicker.setOnClickListener(view -> {
            timerPickDialog.show(getSupportFragmentManager(), null);
        });
        dataBinding.ivPhotoAdd.setOnClickListener(view -> {
            boolean guide_check = PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Guid_photo_boolean, false);
            if (!guide_check) {
                Intent intent = new Intent(mContext, PhotoGuideActivity.class);
                startActivityForResult(intent, 1000);
            } else {
                Intent intent = new Intent(mContext, CameraActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        dataBinding.ivOutsideInformation.setOnClickListener(view -> {
            if (dataBinding.llOutside.getVisibility() == View.VISIBLE) {
                dataBinding.ivOutsideInformation.setSelected(false);
                dataBinding.llOutside.setVisibility(View.GONE);
            } else {
                dataBinding.llOutside.setVisibility(View.VISIBLE);
                dataBinding.ivOutsideInformation.setSelected(true);
            }

        });
        dataBinding.ivOtherInformation.setOnClickListener(view -> {
            if (dataBinding.llOther.getVisibility() == View.VISIBLE) {
                dataBinding.ivOtherInformation.setSelected(false);
                dataBinding.llOther.setVisibility(View.GONE);
            } else {
                dataBinding.llOther.setVisibility(View.VISIBLE);
                dataBinding.ivOtherInformation.setSelected(true);
            }
        });
        dataBinding.question1.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, InformationActivity.class);
            intent.putExtra("information", "outside");
            startActivity(intent);
        });
        dataBinding.question2.setOnClickListener(view -> {
            Intent intent = new Intent(mContext, InformationActivity.class);
            intent.putExtra("information", "other");
            startActivity(intent);
            finishWithAnim();
        });
        dataBinding.ivPhotoMinus.setOnClickListener(view -> {
            Glide.with(mContext).load(R.drawable.photo_add).into(dataBinding.ivPhotoAdd);
            dataBinding.ivPhotoMinus.setVisibility(View.GONE);
            dataBinding.ivPhotoAdd.setClickable(true);
        });
        dataBinding.tvStart.setOnClickListener(view -> {
            if (dataBinding.tvStart.isSelected()) {
                if (photo != null) {
                    ReportApi(true);
                    check = 0;
                } else {
                    NoPhotoDialog dialog = new NoPhotoDialog(mContext);
                    dialog.show(getSupportFragmentManager(), null);
                    dialog.setListener_no(() -> {
                        ReportApi(false);
                    });
                }
            }
        });
    }

    public void emoteclick(View view) {
        switch (view.getId()) {
            case R.id.iv_shape_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivShape2.setSelected(false);
                    dataBinding.ivShape3.setSelected(false);
                    dataBinding.ivShape4.setSelected(false);
                    dataBinding.ivShape5.setSelected(false);
                    dataBinding.ivShape6.setSelected(false);
                    dataBinding.ivShape7.setSelected(false);
                    shape = null;
                    shape = "딱딱";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    shape = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_shape_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivShape1.setSelected(false);
                    dataBinding.ivShape3.setSelected(false);
                    dataBinding.ivShape4.setSelected(false);
                    dataBinding.ivShape5.setSelected(false);
                    dataBinding.ivShape6.setSelected(false);
                    dataBinding.ivShape7.setSelected(false);
                    shape = null;
                    shape = "단단";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    shape = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_shape_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivShape2.setSelected(false);
                    dataBinding.ivShape1.setSelected(false);
                    dataBinding.ivShape4.setSelected(false);
                    dataBinding.ivShape5.setSelected(false);
                    dataBinding.ivShape6.setSelected(false);
                    dataBinding.ivShape7.setSelected(false);
                    shape = null;
                    shape = "건조";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    shape = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_shape_4:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivShape2.setSelected(false);
                    dataBinding.ivShape3.setSelected(false);
                    dataBinding.ivShape1.setSelected(false);
                    dataBinding.ivShape5.setSelected(false);
                    dataBinding.ivShape6.setSelected(false);
                    dataBinding.ivShape7.setSelected(false);
                    shape = null;
                    shape = "매끈";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    shape = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_shape_5:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivShape2.setSelected(false);
                    dataBinding.ivShape3.setSelected(false);
                    dataBinding.ivShape4.setSelected(false);
                    dataBinding.ivShape1.setSelected(false);
                    dataBinding.ivShape6.setSelected(false);
                    dataBinding.ivShape7.setSelected(false);
                    shape = null;
                    shape = "물렁";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    shape = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_shape_6:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivShape2.setSelected(false);
                    dataBinding.ivShape3.setSelected(false);
                    dataBinding.ivShape4.setSelected(false);
                    dataBinding.ivShape5.setSelected(false);
                    dataBinding.ivShape1.setSelected(false);
                    dataBinding.ivShape7.setSelected(false);
                    shape = null;
                    shape = "찰흙";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    shape = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_shape_7:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivShape2.setSelected(false);
                    dataBinding.ivShape3.setSelected(false);
                    dataBinding.ivShape4.setSelected(false);
                    dataBinding.ivShape5.setSelected(false);
                    dataBinding.ivShape6.setSelected(false);
                    dataBinding.ivShape1.setSelected(false);
                    shape = null;
                    shape = "물";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    shape = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_color_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivColor2.setSelected(false);
                    dataBinding.ivColor3.setSelected(false);
                    dataBinding.ivColor4.setSelected(false);
                    dataBinding.ivColor5.setSelected(false);
                    dataBinding.ivColor6.setSelected(false);
                    color = null;
                    color = "갈색";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    color = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_color_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivColor1.setSelected(false);
                    dataBinding.ivColor3.setSelected(false);
                    dataBinding.ivColor4.setSelected(false);
                    dataBinding.ivColor5.setSelected(false);
                    dataBinding.ivColor6.setSelected(false);
                    color = null;
                    color = "회색";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    color = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_color_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivColor2.setSelected(false);
                    dataBinding.ivColor1.setSelected(false);
                    dataBinding.ivColor4.setSelected(false);
                    dataBinding.ivColor5.setSelected(false);
                    dataBinding.ivColor6.setSelected(false);
                    color = null;
                    color = "노란색";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    color = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_color_4:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivColor2.setSelected(false);
                    dataBinding.ivColor3.setSelected(false);
                    dataBinding.ivColor1.setSelected(false);
                    dataBinding.ivColor5.setSelected(false);
                    dataBinding.ivColor6.setSelected(false);
                    color = null;
                    color = "빨간색";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    color = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_color_5:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivColor2.setSelected(false);
                    dataBinding.ivColor3.setSelected(false);
                    dataBinding.ivColor4.setSelected(false);
                    dataBinding.ivColor1.setSelected(false);
                    dataBinding.ivColor6.setSelected(false);
                    color = null;
                    color = "검정색";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    color = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_color_6:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivColor2.setSelected(false);
                    dataBinding.ivColor3.setSelected(false);
                    dataBinding.ivColor4.setSelected(false);
                    dataBinding.ivColor5.setSelected(false);
                    dataBinding.ivColor1.setSelected(false);
                    color = null;
                    color = "초록색";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    color = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_amount_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivAmount2.setSelected(false);
                    dataBinding.ivAmount3.setSelected(false);
                    mass = null;
                    mass = "적음";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    mass = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_amount_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivAmount1.setSelected(false);
                    dataBinding.ivAmount3.setSelected(false);
                    mass = null;
                    mass = "평범";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    mass = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_amount_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivAmount2.setSelected(false);
                    dataBinding.ivAmount1.setSelected(false);
                    mass = null;
                    mass = "많음";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    mass = null;
                }
                checkButtonOn();
                checkButtonOn2(check);
                break;
            case R.id.iv_emote1_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote12.setSelected(false);
                    dataBinding.ivEmote13.setSelected(false);
                    dataBinding.ivEmote14.setSelected(false);
                    colic = null;
                    colic = "없음";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    colic = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote1_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote11.setSelected(false);
                    dataBinding.ivEmote13.setSelected(false);
                    dataBinding.ivEmote14.setSelected(false);
                    colic = null;
                    colic = "약간";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    colic = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote1_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote12.setSelected(false);
                    dataBinding.ivEmote11.setSelected(false);
                    dataBinding.ivEmote14.setSelected(false);
                    colic = null;
                    colic = "중간";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    colic = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote1_4:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote12.setSelected(false);
                    dataBinding.ivEmote13.setSelected(false);
                    dataBinding.ivEmote11.setSelected(false);
                    colic = null;
                    colic = "고통";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    colic = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote2_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote22.setSelected(false);
                    dataBinding.ivEmote23.setSelected(false);
                    dataBinding.ivEmote24.setSelected(false);
                    smell = null;
                    smell = "특이사항 없음";

                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    smell = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote2_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote21.setSelected(false);
                    dataBinding.ivEmote23.setSelected(false);
                    dataBinding.ivEmote24.setSelected(false);
                    smell = null;
                    smell = "시큼";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    smell = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote2_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote22.setSelected(false);
                    dataBinding.ivEmote21.setSelected(false);
                    dataBinding.ivEmote24.setSelected(false);
                    smell = null;
                    smell = "생선";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    smell = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote2_4:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote22.setSelected(false);
                    dataBinding.ivEmote23.setSelected(false);
                    dataBinding.ivEmote21.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    smell = null;
                    smell = "지독";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    smell = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote3_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote32.setSelected(false);
                    dataBinding.ivEmote33.setSelected(false);
                    elapsedTime = null;
                    elapsedTime = "3분이내";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    elapsedTime = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_emote3_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote31.setSelected(false);
                    dataBinding.ivEmote33.setSelected(false);
                    elapsedTime = null;
                    elapsedTime = "3~10분";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    elapsedTime = null;
                }
                checkButtonOn();
                break;

            case R.id.iv_emote3_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivEmote32.setSelected(false);
                    dataBinding.ivEmote31.setSelected(false);
                    elapsedTime = null;
                    elapsedTime = "10분이상";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    elapsedTime = null;
                }
                checkButtonOn();
                break;
            case R.id.iv_blood_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivBlood2.setSelected(false);
                    dataBinding.ivBlood3.setSelected(false);
                    dataBinding.ivBlood4.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.GONE);
                    hematocheziaPosition = null;
                    hematochezia = null;
                    hematochezia = "없음";
                } else {
                    view.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.GONE);
                    hematocheziaPosition = null;
                    dataBinding.tvStart.setSelected(false);
                    hematochezia = null;
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_blood_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivBlood1.setSelected(false);
                    dataBinding.ivBlood3.setSelected(false);
                    dataBinding.ivBlood4.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.VISIBLE);
                    hematochezia = null;
                    hematochezia = "미량";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.GONE);
                    hematocheziaPosition = null;
                    hematochezia = null;
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_blood_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivBlood2.setSelected(false);
                    dataBinding.ivBlood1.setSelected(false);
                    dataBinding.ivBlood4.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.VISIBLE);
                    hematochezia = null;
                    hematochezia = "소량";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.GONE);
                    hematocheziaPosition = null;
                    hematochezia = null;
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_blood_4:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    dataBinding.ivBlood2.setSelected(false);
                    dataBinding.ivBlood3.setSelected(false);
                    dataBinding.ivBlood1.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.VISIBLE);
                    hematochezia = null;
                    hematochezia = "대량";
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    dataBinding.llBloodLocation.setVisibility(View.GONE);
                    hematocheziaPosition = null;
                    hematochezia = null;
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_another_1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (appearanceEtc != null) {
                        appearanceEtc = appearanceEtc + "," + "점액";
                    } else {
                        appearanceEtc = "점액";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivAnother2.isSelected()) {
                        appearanceEtc = "기름기";
                    } else if (dataBinding.ivAnother3.isSelected()) {
                        appearanceEtc = "둥둥뜸";
                    } else {
                        appearanceEtc = null;
                    }
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_another_2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (appearanceEtc != null) {
                        appearanceEtc = appearanceEtc + "," + "기름기";
                    } else {
                        appearanceEtc = "기름기";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivAnother1.isSelected()) {
                        appearanceEtc = "점액";
                    } else if (dataBinding.ivAnother3.isSelected()) {
                        appearanceEtc = "둥둥뜸";
                    } else {
                        appearanceEtc = null;
                    }
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_another_3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (appearanceEtc != null) {
                        appearanceEtc = appearanceEtc + "," + "둥둥뜸";
                    } else {
                        appearanceEtc = "둥둥뜸";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivAnother1.isSelected()) {
                        appearanceEtc = "점액";
                    } else if (dataBinding.ivAnother2.isSelected()) {
                        appearanceEtc = "기름기";
                    } else {
                        appearanceEtc = null;
                    }
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_another_another1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (etc != null) {
                        etc = etc + "," + "잔변감";
                    } else {
                        etc = "잔변감";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivAnotherAnother2.isSelected()) {
                        etc = "급박변";
                    } else if (dataBinding.ivAnotherAnother3.isSelected()) {
                        etc = "배변 후 복통";
                    } else {
                        etc = null;
                    }
                }
                checkButtonOn();
                break;
            case R.id.iv_another_another2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (etc != null) {
                        etc = etc + "," + "급박변";
                    } else {
                        etc = "잔변감";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivAnotherAnother1.isSelected()) {
                        etc = "잔변감";
                    } else if (dataBinding.ivAnotherAnother3.isSelected()) {
                        etc = "배변 후 복통";
                    } else {
                        etc = null;
                    }
                }
                checkButtonOn();
                break;
            case R.id.iv_another_another3:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (etc != null) {
                        etc = etc + "," + "배변 후 급통";
                    } else {
                        etc = "배변 후 급통";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivAnotherAnother1.isSelected()) {
                        etc = "잔변감";
                    } else if (dataBinding.ivAnotherAnother2.isSelected()) {
                        etc = "급박변";
                    } else {
                        etc = null;
                    }
                }
                checkButtonOn();
                break;
            case R.id.iv_checkbox_1:
                view.setSelected(!view.isSelected());
                break;
            case R.id.iv_blood_locate1:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (hematocheziaPosition != null) {
                        hematocheziaPosition = hematocheziaPosition + "," + "대변과 함께";
                    } else {
                        hematocheziaPosition = "대변과 함께";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivBloodLocate2.isSelected()) {
                        hematocheziaPosition = "대변과 따로";
                    } else {
                        hematocheziaPosition = null;
                    }
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
            case R.id.iv_blood_locate2:
                if (!view.isSelected()) {
                    view.setSelected(true);
                    if (hematocheziaPosition != null) {
                        hematocheziaPosition = hematocheziaPosition + "," + "대변과 따로";
                    } else {
                        hematocheziaPosition = "대변과 따로";
                    }
                } else {
                    view.setSelected(false);
                    dataBinding.tvStart.setSelected(false);
                    if (dataBinding.ivBloodLocate1.isSelected()) {
                        hematocheziaPosition = "대변과 함께";
                    } else {
                        hematocheziaPosition = null;
                    }
                }
                checkButtonOn2(check);
                checkButtonOn();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1000:
                    File file = (File) data.getExtras().get("guide");
                    String ai_color = (String) data.getExtras().get("color");
                    String ai_shape = (String) data.getExtras().get("shape");
                    Float ai_blood = (Float) data.getExtras().get("blood");
                    autoAi(ai_shape, ai_color, file, ai_blood);
            }
        }
    }


    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int px) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = px;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;

    }


    public void checkButtonOn() {
        if (time != null && shape != null && color != null && mass != null && hematochezia != null && colic != null && smell != null && elapsedTime != null){
            if (hematochezia.equals("없음")) {
                dataBinding.tvStart.setSelected(true);
            } else {
                if (hematocheziaPosition != null) {
                    dataBinding.tvStart.setSelected(true);
                }
            }
        }
//        if ((dataBinding.tvTime.getText()==null) && (dataBinding.ivPhotoMinus.getVisibility() == View.VISIBLE)) {{// 둘다 클릭시
//            Log.d(dataBinding.tvTime.getText());
//                if (dataBinding.ivShape1.isSelected() || dataBinding.ivShape2.isSelected() || dataBinding.ivShape3.isSelected() || dataBinding.ivShape4.isSelected() || dataBinding.ivShape5.isSelected() || dataBinding.ivShape6.isSelected() || dataBinding.ivShape7.isSelected())
//                    if (dataBinding.ivColor1.isSelected() || dataBinding.ivColor2.isSelected() || dataBinding.ivColor3.isSelected() || dataBinding.ivColor4.isSelected() || dataBinding.ivColor5.isSelected() || dataBinding.ivColor6.isSelected())
//                        if (dataBinding.ivAmount1.isSelected() || dataBinding.ivAmount2.isSelected() || dataBinding.ivAmount3.isSelected())
//                            if (dataBinding.ivBlood1.isSelected() || dataBinding.ivBlood2.isSelected() || dataBinding.ivBlood3.isSelected() || dataBinding.ivBlood4.isSelected())
//                                if (dataBinding.ivBloodLocate1.isSelected() || dataBinding.ivBloodLocate2.isSelected())
//                                    if (dataBinding.ivAnother1.isSelected() || dataBinding.ivAnother2.isSelected() || dataBinding.ivAnother3.isSelected())
//                                        if (dataBinding.ivEmote11.isSelected() || dataBinding.ivEmote12.isSelected() || dataBinding.ivEmote13.isSelected() || dataBinding.ivEmote14.isSelected())
//                                            if (dataBinding.ivEmote21.isSelected() || dataBinding.ivEmote22.isSelected() || dataBinding.ivEmote23.isSelected() || dataBinding.ivEmote24.isSelected())
//                                                if (dataBinding.ivEmote31.isSelected() || dataBinding.ivEmote32.isSelected() || dataBinding.ivEmote33.isSelected())
//                                                    if (dataBinding.ivAnotherAnother1.isSelected() || dataBinding.ivAnotherAnother2.isSelected() || dataBinding.ivAnotherAnother3.isSelected())
//                                                        dataBinding.tvStart.setSelected(true);
//
//            }
//        }

    }

    public void checkButtonOn2(int count) {
        if (count == 0) {
            if (dataBinding.ivShape1.isSelected() || dataBinding.ivShape2.isSelected() || dataBinding.ivShape3.isSelected() || dataBinding.ivShape4.isSelected() || dataBinding.ivShape5.isSelected() || dataBinding.ivShape6.isSelected() || dataBinding.ivShape7.isSelected())
                if (dataBinding.ivColor1.isSelected() || dataBinding.ivColor2.isSelected() || dataBinding.ivColor3.isSelected() || dataBinding.ivColor4.isSelected() || dataBinding.ivColor5.isSelected() || dataBinding.ivColor6.isSelected())
                    if (dataBinding.ivAmount1.isSelected() || dataBinding.ivAmount2.isSelected() || dataBinding.ivAmount3.isSelected())
                        if (dataBinding.ivBlood1.isSelected() || dataBinding.ivBlood2.isSelected() || dataBinding.ivBlood3.isSelected() || dataBinding.ivBlood4.isSelected())
                            if (dataBinding.llBloodLocation.getVisibility() == View.VISIBLE) {
                                if (dataBinding.ivBloodLocate1.isSelected() || dataBinding.ivBloodLocate2.isSelected()){
                                    if (dataBinding.ivAnother1.isSelected() || dataBinding.ivAnother2.isSelected() || dataBinding.ivAnother3.isSelected()) {
                                        dataBinding.ivOutsideInformation.setSelected(false);
                                        dataBinding.llOutside.setVisibility(View.GONE);
                                        check += 1;
                                    }
                                }
                            } else {
                                if (dataBinding.ivAnother1.isSelected() || dataBinding.ivAnother2.isSelected() || dataBinding.ivAnother3.isSelected()) {
                                    dataBinding.ivOutsideInformation.setSelected(false);
                                    dataBinding.llOutside.setVisibility(View.GONE);
                                    check += 1;
                                }
                            }

        }
    }

    public void ReportApi(boolean photoExist) {
        reportRequest.setTime(time);
        reportRequest.setColor(color);
        if (dataBinding.ivCheckbox1.isSelected()) {
            reportRequest.setChanged(true);
        } else {
            reportRequest.setChanged(false);
        }
        if(hematocheziaPosition==null){
            hematocheziaPosition="없음";
        }else if(hematocheziaPosition.equals(dataBinding.ivBloodLocate1.getText())){
            hematocheziaPosition="함께";
        }else if(hematocheziaPosition.equals(dataBinding.ivBloodLocate2.getText())){
            hematocheziaPosition="따로";
        }
        reportRequest.setAppearanceEtc(appearanceEtc);
        reportRequest.setColic(colic);
        reportRequest.setEtc(etc);
        reportRequest.setElapsedTime(elapsedTime);
        reportRequest.setHematochezia(hematochezia);
        reportRequest.setHematocheziaPosition(hematocheziaPosition);
        reportRequest.setSmell(smell);
        reportRequest.setShape(shape);
        reportRequest.setMass(mass);
        callApiWithLoading(NetworkManager.getInstance(mContext).getApiService().report(reportRequest), new ApiResponseHandler<UserIdresponse>() {
            @Override
            public void onSuccess(UserIdresponse result) {
                if (photoExist) {
                    int report_id = result.getData().getId();
                    showLoading();
                    camreaApi(report_id);
                } else {
                    Toast.makeText(mContext, "기록되었습니다.", Toast.LENGTH_SHORT).show();
                    finishWithAnim();
                }

            }

            @Override
            public void onServerFail(CommonResponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onNoResponse(UserIdresponse result) {
                ToastUtil.show(mContext,result.message);

            }

            @Override
            public void onBadRequest(Throwable t) {
                Log.d("!!!!!잘못됨!!");

            }
        });
    }

    public void camreaApi(int id) {
        Log.d(photo);
        MultipartBody.Part photo_body = transformImageToMultipart(photo);
        callApi(NetworkManager.getInstance(mContext).getApiService().report_photo(id, photo_body), new ApiResponseHandler<CommonResponse>() {
            @Override
            public void onSuccess(CommonResponse result) {
                Intent intent = new Intent(mContext, ReportExtraActivity.class);
                intent.putExtra("report_id", id);
                intent.putExtra("report_String", "report");
                hideLoading();
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
                Toast.makeText(mContext, "인터넷 연결을 확인해주세요!", Toast.LENGTH_SHORT).show();
                Log.d("!!!!!잘못됨!!");
            }
        });
    }

    public void autoAi(String aiShape, String aiColor, File aiUri, float aiBlood) {
        if (aiUri != null) {
            Glide.with(mContext).asBitmap().load(aiUri).override(45).apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(10
            ))).into(dataBinding.ivPhotoAdd);
            photo = aiUri;
        }
        dataBinding.ivPhotoMinus.setVisibility(View.VISIBLE);
        dataBinding.ivPhotoAdd.setClickable(false);
        if (aiBlood == 0) {
            dataBinding.ivBlood1.setSelected(true);
            dataBinding.ivBlood2.setSelected(false);
            dataBinding.ivBlood3.setSelected(false);
            dataBinding.ivBlood4.setSelected(false);
            dataBinding.llBloodLocation.setVisibility(View.GONE);
            hematocheziaPosition = null;
            hematochezia = null;
            hematochezia = "없음";
        } else if (aiBlood > 0 && aiBlood <= 0.3) {
            dataBinding.ivBlood2.setSelected(true);
            dataBinding.ivBlood1.setSelected(false);
            dataBinding.ivBlood3.setSelected(false);
            dataBinding.ivBlood4.setSelected(false);
            dataBinding.llBloodLocation.setVisibility(View.VISIBLE);
            hematochezia = null;
            hematochezia = "미량";
        } else if (aiBlood > 0.3 && aiBlood <= 0.6) {

            dataBinding.ivBlood3.setSelected(true);
            dataBinding.ivBlood2.setSelected(false);
            dataBinding.ivBlood1.setSelected(false);
            dataBinding.ivBlood4.setSelected(false);
            dataBinding.llBloodLocation.setVisibility(View.VISIBLE);
            hematochezia = null;
            hematochezia = "소량";
        } else if (aiBlood > 0.6 && aiBlood <= 1) {

            dataBinding.ivBlood4.setSelected(true);
            dataBinding.ivBlood2.setSelected(false);
            dataBinding.ivBlood3.setSelected(false);
            dataBinding.ivBlood1.setSelected(false);
            dataBinding.llBloodLocation.setVisibility(View.VISIBLE);
            hematochezia = null;
            hematochezia = "대량";
        }
        if (aiColor.equals("#cf4e11")) {
            dataBinding.ivColor5.setSelected(false);
            dataBinding.ivColor2.setSelected(false);
            dataBinding.ivColor3.setSelected(false);
            dataBinding.ivColor4.setSelected(true);
            dataBinding.ivColor1.setSelected(false);
            dataBinding.ivColor6.setSelected(false);
            color = "빨간색";
        } else if (aiColor.equals("#000000")) {
            dataBinding.ivColor5.setSelected(true);
            dataBinding.ivColor2.setSelected(false);
            dataBinding.ivColor3.setSelected(false);
            dataBinding.ivColor4.setSelected(false);
            dataBinding.ivColor1.setSelected(false);
            dataBinding.ivColor6.setSelected(false);
            color = "검정색";
        } else if (aiColor.equals("#d1c5b4")) {
            dataBinding.ivColor1.setSelected(false);
            dataBinding.ivColor2.setSelected(true);
            dataBinding.ivColor3.setSelected(false);
            dataBinding.ivColor4.setSelected(false);
            dataBinding.ivColor5.setSelected(false);
            dataBinding.ivColor6.setSelected(false);
            color = "회색";
        } else if (aiColor.equals("#dfb337")) {
            dataBinding.ivColor1.setSelected(false);
            dataBinding.ivColor2.setSelected(false);
            dataBinding.ivColor3.setSelected(true);
            dataBinding.ivColor4.setSelected(false);
            dataBinding.ivColor5.setSelected(false);
            dataBinding.ivColor6.setSelected(false);
            color = "노란색";
        } else if (aiColor.equals("#985a0e")) {
            dataBinding.ivColor1.setSelected(true);
            dataBinding.ivColor2.setSelected(false);
            dataBinding.ivColor3.setSelected(false);
            dataBinding.ivColor4.setSelected(false);
            dataBinding.ivColor5.setSelected(false);
            dataBinding.ivColor6.setSelected(false);
            color = "갈색";
        } else if (aiColor.equals("#2a5f11")) {
            dataBinding.ivColor1.setSelected(false);
            dataBinding.ivColor2.setSelected(false);
            dataBinding.ivColor3.setSelected(false);
            dataBinding.ivColor4.setSelected(false);
            dataBinding.ivColor5.setSelected(false);
            dataBinding.ivColor6.setSelected(true);
            color = "초록색";
        }
        if (aiShape.equals("hard_lump")) {//단단
            dataBinding.ivShape1.setSelected(false);
            dataBinding.ivShape2.setSelected(true);
            dataBinding.ivShape3.setSelected(false);
            dataBinding.ivShape4.setSelected(false);
            dataBinding.ivShape5.setSelected(false);
            dataBinding.ivShape6.setSelected(false);
            dataBinding.ivShape7.setSelected(false);
            shape = "단단";
        } else if (aiShape.equals("hard_div")) { //딱딱
            dataBinding.ivShape1.setSelected(true);
            dataBinding.ivShape2.setSelected(false);
            dataBinding.ivShape3.setSelected(false);
            dataBinding.ivShape4.setSelected(false);
            dataBinding.ivShape5.setSelected(false);
            dataBinding.ivShape6.setSelected(false);
            dataBinding.ivShape7.setSelected(false);
            shape = "딱딱";
        } else if (aiShape.equals("clay")) { //찰흙

            dataBinding.ivShape1.setSelected(false);
            dataBinding.ivShape2.setSelected(false);
            dataBinding.ivShape3.setSelected(false);
            dataBinding.ivShape4.setSelected(false);
            dataBinding.ivShape5.setSelected(false);
            dataBinding.ivShape6.setSelected(true);
            dataBinding.ivShape7.setSelected(false);
            shape = "찰흙";
        } else if (aiShape.equals("moist")) {//매끈

            dataBinding.ivShape1.setSelected(false);
            dataBinding.ivShape2.setSelected(false);
            dataBinding.ivShape3.setSelected(false);
            dataBinding.ivShape4.setSelected(true);
            dataBinding.ivShape5.setSelected(false);
            dataBinding.ivShape6.setSelected(false);
            dataBinding.ivShape7.setSelected(false);
            shape = "매끈";
        } else if (aiShape.equals("dry")) {//건조

            dataBinding.ivShape1.setSelected(false);
            dataBinding.ivShape2.setSelected(false);
            dataBinding.ivShape3.setSelected(true);
            dataBinding.ivShape4.setSelected(false);
            dataBinding.ivShape5.setSelected(false);
            dataBinding.ivShape6.setSelected(false);
            dataBinding.ivShape7.setSelected(false);
            shape = "건조";
        } else if (aiShape.equals("watery")) {//물

            dataBinding.ivShape1.setSelected(false);
            dataBinding.ivShape2.setSelected(false);
            dataBinding.ivShape3.setSelected(false);
            dataBinding.ivShape4.setSelected(false);
            dataBinding.ivShape5.setSelected(false);
            dataBinding.ivShape6.setSelected(false);
            dataBinding.ivShape7.setSelected(true);
            shape = "물";
        } else if (aiShape.equals("soft_div")) {//물렁

            dataBinding.ivShape1.setSelected(false);
            dataBinding.ivShape2.setSelected(false);
            dataBinding.ivShape3.setSelected(false);
            dataBinding.ivShape4.setSelected(false);
            dataBinding.ivShape5.setSelected(true);
            dataBinding.ivShape6.setSelected(false);
            dataBinding.ivShape7.setSelected(false);
            shape = "물렁";
        }
        if (aiUri != null) {
            Glide.with(mContext).asBitmap().load(aiUri).override(45).apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(10
            ))).into(dataBinding.ivPhotoAdd);
            photo = aiUri;
        }
        dataBinding.ivPhotoMinus.setVisibility(View.VISIBLE);
        dataBinding.ivPhotoAdd.setClickable(false);
        checkButtonOn();
        checkButtonOn2(check);
    }

}