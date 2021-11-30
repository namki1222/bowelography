package com.rightcode.bowelography;

import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;

import com.bumptech.glide.Glide;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.common.util.Utility;
import com.rightcode.bowelography.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class GlobalApplication extends Application implements Application.ActivityLifecycleCallbacks {

    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this, getString(R.string.kakao_app_key));
        Log.d(Utility.INSTANCE.getKeyHash(this)+"!!!!<-해쉬키");
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        Glide.get(this).trimMemory(TRIM_MEMORY_MODERATE);
    }


    @Override
    public void onTerminate() {
        unregisterActivityLifecycleCallbacks(this);

        super.onTerminate();
    }
}
