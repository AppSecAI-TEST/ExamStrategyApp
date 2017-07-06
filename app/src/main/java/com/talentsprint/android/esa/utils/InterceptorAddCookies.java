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
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) PreferenceManager.getStringSet(TalentSprintApp.appContext, AppConstants
                .COOKIES, new
                HashSet<String>());
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
        }
        return chain.proceed(builder.build());
    }
}
