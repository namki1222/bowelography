package com.rightcode.bowelography;


import android.content.Context;

import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;


public class Features {
    public enum ConnectServer {
        REAL,
        QA
    }

    public static final boolean PASS_STANDARDS = false;
//    public static final ApiEnums.Country CONNECT_COUNTRY = null;
//    public static final ApiEnums.Country SERVICE_COUNTRY = null;

    /**
     * 배포용 빌드(android:testOnly in AndroidManifest.xml) 여부
     * ref) https://developer.android.com/guide/topics/manifest/application-element.html
     */
    public static boolean TEST_ONLY = true;
    /**
     * Log.java 클래스의 메소드를 사용한 로그를 출력할지 여부
     */
    public static final boolean SHOW_LOG = true;
    /**
     * 네트워크 관련 로그를 출력할지 여부
     */
    public static final boolean SHOW_NETWORK_LOG = true;

    public static final boolean BLOCKING_REVIEW_WRITE_BY_BANNED = true;

    public static ConnectServer getServer(Context context) {
        String name = PreferenceUtil.getInstance(context).get(PreferenceUtil.PreferenceKey.ConnectServer, ConnectServer.REAL.name());

        ConnectServer server;
        try {
            server = ConnectServer.valueOf(name);
        } catch (Exception e) {
            Log.e(e);
            server = ConnectServer.REAL;
        }

        Log.d(String.format("-- return ( server : %s ) ", server.name()));
        return server;
    }
}