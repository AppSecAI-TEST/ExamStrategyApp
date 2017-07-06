package com.talentsprint.android.esa.utils;

import com.talentsprint.android.esa.models.HomeObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Anudeep on 5/8/2017.
 */

public interface TalentSprintApi {

    @GET(ApiUrls.HOME)
    Call<HomeObject> getHome(@Query(ApiUrls.ONE_SIGNAL) String oneSignalId);
}
