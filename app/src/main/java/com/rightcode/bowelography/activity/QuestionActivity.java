package com.rightcode.bowelography.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rightcode.bowelography.R;
import com.rightcode.bowelography.activity.BaseActivity;
import com.rightcode.bowelography.databinding.ActivityQuestionBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.util.FragmentHelper;

public class QuestionActivity extends BaseActivity<ActivityQuestionBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_question);
    }

    @Override
    protected void initActivity() {
        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("문의하기", view -> finishWithAnim(),"LEFT");
    }

    @Override
    protected void initClickListener() {

    }
}