package com.talentsprint.android.esa.utils;

import com.talentsprint.android.esa.TalentSprintApp;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Anudeep Reddy on 7/6/2017.
 */

public class InterceptorAddCookies implements Interceptor {
    boolean isCache;

    public InterceptorAddCookies(boolean isCache) {
        this.isCache = isCache;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) PreferenceManager.getStringSet(TalentSprintApp.appContext, AppConstants
                .COOKIES, new
                HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }
        if (!new ServiceManager(TalentSprintApp.appContext).isNetworkAvailable()) {
            if (isCache) {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                builder
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
            } else {
                throw (new IOException("No network available"));
            }
        } else {
            builder.header("Cache-Control", "public, max-age=" + 10/*seconds*/);
        }
        return chain.proceed(builder.build());
    }
}
