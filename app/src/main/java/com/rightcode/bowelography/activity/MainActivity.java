package com.rightcode.bowelography.activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.rightcode.bowelography.fragment.HomeFragment;
import com.rightcode.bowelography.R;
import com.rightcode.bowelography.databinding.ActivityMainBinding;
import com.rightcode.bowelography.fragment.MypageFragment;
import com.rightcode.bowelography.util.PreferenceUtil;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    FragmentManager fm;
    FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView(R.layout.activity_main);
    }
    @Override
    protected void initActivity() {
        fm = getSupportFragmentManager();
        replaceFragment(HomeFragment.newInstance());
        dataBinding.bottomNavigationHome.setSelected(true);
    }
    @Override
    protected void initClickListener() {
        dataBinding.bottomNavigationHome.setOnClickListener(view->{
            replaceFragment(HomeFragment.newInstance());
            dataBinding.bottomNavigationHome.setSelected(true);
            dataBinding.bottomNavigationMypage.setSelected(false);
        });
        dataBinding.bottomNavigationCamera.setOnClickListener(view->{
            boolean guide_check = PreferenceUtil.getInstance(mContext).get(PreferenceUtil.PreferenceKey.Guid_photo_boolean,false);
            if(guide_check==false){
                Intent intent = new Intent(mContext,PhotoGuideActivity.class);
                intent.putExtra("photo","ai");
                startActivity(intent);
            }else{
                Intent intent = new Intent(mContext,CameraActivity.class);
                intent.putExtra("photo","ai");
                startActivity(intent);
            }
        });

        dataBinding.bottomNavigationMypage.setOnClickListener(view->{
            replaceFragment(MypageFragment.newInstance());
            dataBinding.bottomNavigationHome.setSelected(false);
            dataBinding.bottomNavigationMypage.setSelected(true);
        });

    }
    private void replaceFragment(Fragment fragment) {
        transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}