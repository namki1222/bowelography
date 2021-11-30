package com.rightcode.bowelography.network;

import android.content.Context;


import com.rightcode.bowelography.Features;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager {

    public static final String COM_REAL_DOMAIN = "http://15.164.123.1:3000/";

    private static Context context;
    Retrofit retrofit;
    private volatile static NetworkManager sIntance;
    private com.rightcode.bowelography.network.NetworkApi api;

    public NetworkManager(){
        retrofit = new Retrofit.Builder()
                .baseUrl(COM_REAL_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(buildClient())
                .build();

        api = retrofit.create(com.rightcode.bowelography.network.NetworkApi.class);
    }

    private OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(chain -> {
            String token = PreferenceUtil.getInstance(context).get(PreferenceUtil.PreferenceKey.Token, ""); //TODO: 토큰 PreferenceUtil에서 받아오기
            Log.d(token+"!!!!");
            Request newRequest;
            if(token != null && !token.equals("")){
                newRequest = chain.request().newBuilder().addHeader("Authorization", "bearer " + token).build();
            }else newRequest = chain.request();

            return chain.proceed(newRequest);
        });
        if (Features.TEST_ONLY && Features.SHOW_NETWORK_LOG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }

        builder.retryOnConnectionFailure(true);

        return builder.build();
    }

    public static NetworkManager getInstance(Context context){
        NetworkManager.context = context;
        if(sIntance == null){
            sIntance = new NetworkManager();
        }
        return sIntance;
    }

    public com.rightcode.bowelography.network.NetworkApi getApiService(){
        return api;
    }



}
