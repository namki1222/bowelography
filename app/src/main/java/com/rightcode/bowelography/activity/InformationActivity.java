package com.rightcode.bowelography.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.rightcode.bowelography.fragment.OtherFragment;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityInformationBinding;
import com.rightcode.bowelography.fragment.TopFragment;
import com.rightcode.bowelography.fragment.outsideFragment;
import com.rightcode.bowelography.util.FragmentHelper;

public class InformationActivity extends BaseActivity<ActivityInformationBinding> {

    FragmentManager fm;
    FragmentTransaction transaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_information);
    }
    @Override
    protected void initActivity() {
        fm = getSupportFragmentManager();

        TopFragment mTopFragment = (TopFragment) FragmentHelper.findFragmentByTag(getSupportFragmentManager(), "TopFragment");
        mTopFragment.setDefaultFormat("기타정보", view -> finishWithAnim(),"RIGHT");

        String str = getIntent().getStringExtra("information");
        if(str.equals("outside")){
            dataBinding.tvOutside.setSelected(true);
            dataBinding.tvOther.setSelected(false);
            replaceFragment(outsideFragment.newInstance());
        }else if(str.equals("other")){
            dataBinding.tvOutside.setSelected(false);
            dataBinding.tvOther.setSelected(true);
            replaceFragment(OtherFragment.newInstance());
        }
    }

    @Override
    protected void initClickListener() {
        dataBinding.tvOutside.setOnClickListener(view->{
            dataBinding.tvOutside.setSelected(true);
            dataBinding.tvOther.setSelected(false);
            replaceFragment(outsideFragment.newInstance());

        });
        dataBinding.tvOther.setOnClickListener(view->{
            dataBinding.tvOutside.setSelected(false);
            dataBinding.tvOther.setSelected(true);
            replaceFragment(OtherFragment.newInstance());

        });

    }

    private void replaceFragment(Fragment fragment) {
        transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}