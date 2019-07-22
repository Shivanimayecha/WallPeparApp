package com.hd.nature.ghkwallpaper.retrofit;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public class RetroClient {

    private static final String TAG = "RetroClient";
    private static String url = "http://techvirtualgames.com/krsn_app/";

    public static Retrofit getRetrofitInstance() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();

        //  url = "https://techvirtualgames.com/krsn_app/android_app/App_list/api/";


        Log.e(TAG, "getRetrofitInstance: " + url);
        return new Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addConverterFactory(new ToStringConverterFactory())
                .build();
    }

    public static ApiService getApiService() {
        return getRetrofitInstance().create(ApiService.class);
    }

}
