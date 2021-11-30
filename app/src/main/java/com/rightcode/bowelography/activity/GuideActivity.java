package com.rightcode.bowelography.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.ViewPagerAdapter.GuideViewPagerAdapter;
import com.rightcode.bowelography.databinding.ActivityGuideBinding;
import com.rightcode.bowelography.util.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class GuideActivity extends BaseActivity<ActivityGuideBinding> {

    private Integer[] images = {R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4};
    String nextActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_guide);
    }
    @Override
    protected void initActivity() {
        ArrayList<Integer> image = new ArrayList<>(Arrays.asList(images));
        GuideViewPagerAdapter adapter = new GuideViewPagerAdapter(mContext, image);
        dataBinding.vpGuide.setAdapter(adapter);
        dataBinding.indicator.setViewPager2(dataBinding.vpGuide);
        /*dataBinding.vpGuide.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if(position==3){
                    Toast.makeText(mContext, "이벤트 발생 !! 4번째 가이드때 버튼 활성화 시키고싶으면 사용", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });*/
        nextActivity = getIntent().getStringExtra("activity");
        Log.d(nextActivity);
    }

    private void mainActivity() {
        if(nextActivity.equals("mypage")){
            finishWithAnim();
            nextActivity = null;

        }else if(nextActivity.equals("survey")){
            Intent intent = new Intent(mContext, SurveyActivity.class);
            startActivity(intent);
            finish();
            nextActivity = null;
        }
    }

    @Override
    protected void initClickListener() {
        dataBinding.tvExit.setOnClickListener(view -> mainActivity());
        dataBinding.tvStart.setOnClickListener(view -> mainActivity());

    }
}