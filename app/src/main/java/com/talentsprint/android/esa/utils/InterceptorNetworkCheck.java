package com.talentsprint.android.esa.utils;

import com.talentsprint.android.esa.TalentSprintApp;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by Anudeep Reddy on 7/6/2017.
 */

public class InterceptorNetworkCheck implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!new ServiceManager(TalentSprintApp.appContext).isNetworkAvailable()) {
            throw (new IOException("No network available"));
        }
        return null;
    }
}
