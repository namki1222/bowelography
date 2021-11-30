package com.rightcode.bowelography.network;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rightcode.bowelography.Features;
import com.rightcode.bowelography.util.Log;
import com.rightcode.bowelography.util.PreferenceUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkManager_ai {

    public static final String COM_REAL_DOMAIN = "http://58.141.183.46:8000/";

    private static Context context;
    Retrofit retrofit;
    private volatile static NetworkManager_ai sIntance;
    private NetworkApi api;

    public NetworkManager_ai(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        retrofit = new Retrofit.Builder()
                .baseUrl(COM_REAL_DOMAIN)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(buildClient())
                .build();

        api = retrofit.create(NetworkApi.class);
    }

    private OkHttpClient buildClient(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(chain -> {
            Request newRequest;
            newRequest = chain.request();

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

    public static NetworkManager_ai getInstance(Context context){
        NetworkManager_ai.context = context;
        if(sIntance == null){
            sIntance = new NetworkManager_ai();
        }
        return sIntance;
    }

    public NetworkApi getApiService(){
        return api;
    }



}
