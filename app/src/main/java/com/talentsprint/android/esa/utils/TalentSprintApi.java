package com.talentsprint.android.esa.utils;

import com.talentsprint.android.esa.models.GetExamsObject;
import com.talentsprint.android.esa.models.HomeObject;
import com.talentsprint.android.esa.models.ProfileObject;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Anudeep on 5/8/2017.
 */

public interface TalentSprintApi {

    @GET(ApiUrls.HOME)
    Call<HomeObject> getHome(@Query(ApiUrls.ONE_SIGNAL) String oneSignalId);

    @GET(ApiUrls.GET_EXAMS)
    Call<GetExamsObject> getExams();

    @GET(ApiUrls.SET_EXAMS)
    Call<GetExamsObject> setExams(@Query(ApiUrls.EXAM) ArrayList<String> exams);

    @GET(ApiUrls.GET_PROFILE)
    Call<ProfileObject> getProfile();

    @GET(ApiUrls.LOGOUT)
    Call<JSONObject> logoutUser();
}
