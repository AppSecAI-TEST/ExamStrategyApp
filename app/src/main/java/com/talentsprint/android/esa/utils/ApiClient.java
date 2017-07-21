package com.talentsprint.android.esa.utils;

import com.talentsprint.android.esa.TalentSprintApp;

import java.io.File;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anudeep on 5/8/2017.
 */

public class ApiClient {

    public static final String BASE_URL = ApiUrls.BASE_URL;
    private static Retrofit retrofitCached = null;
    private static Retrofit retrofitUnCached = null;

    public static Retrofit getCacheClient() {
        if (retrofitCached == null) {
            //setup cache
            File httpCacheDirectory = new File(TalentSprintApp.appContext.getCacheDir(), "responses");
            int cacheSize = 20 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            OkHttpClient client = new OkHttpClient.Builder().cache(cache)
                    .addInterceptor(new InterceptorAddCookies(true))
                    .addInterceptor(new InterceptorReceivedCookies(true))
                    .build();
            retrofitCached = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        }
        return retrofitCached;
   /*
        if (retrofitCached == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new InterceptorAddCookies(false))
                    .addInterceptor(new InterceptorReceivedCookies())
                    .build();
            retrofitCached = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        }
        return retrofitCached;
    */
    }

    public static Retrofit getCacheClient(boolean isCache) {
        if (retrofitUnCached == null) {
            //setup cache
            File httpCacheDirectory = new File(TalentSprintApp.appContext.getCacheDir(), "responses");
            int cacheSize = 10 * 1024 * 1024; // 10 MiB
            Cache cache = new Cache(httpCacheDirectory, cacheSize);
            OkHttpClient client = new OkHttpClient.Builder().cache(cache)
                    .addInterceptor(new InterceptorAddCookies(isCache))
                    .addInterceptor(new InterceptorReceivedCookies(isCache))
                    .build();
            retrofitUnCached = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()).client(client)
                    .build();
        }
        return retrofitUnCached;
    }
}
