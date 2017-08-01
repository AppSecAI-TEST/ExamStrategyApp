package com.talentsprint.android.esa.utils;

import com.google.gson.JsonObject;
import com.talentsprint.android.esa.models.ArticlesObject;
import com.talentsprint.android.esa.models.CurrentAffairsListObject;
import com.talentsprint.android.esa.models.GetContact;
import com.talentsprint.android.esa.models.GetExamsObject;
import com.talentsprint.android.esa.models.GetSubjectsObject;
import com.talentsprint.android.esa.models.HomeObject;
import com.talentsprint.android.esa.models.ProfileObject;
import com.talentsprint.android.esa.models.QuestionsObject;
import com.talentsprint.android.esa.models.StratergyObject;
import com.talentsprint.android.esa.models.SubTopicsObject;
import com.talentsprint.android.esa.models.TestPropertiesObject;
import com.talentsprint.android.esa.models.TestResultsObject;
import com.talentsprint.android.esa.models.TestReviewObject;
import com.talentsprint.android.esa.models.TopicsObject;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @GET(ApiUrls.DELETE_EXAMS)
    Call<JsonObject> deleteExams(@Query(ApiUrls.ID) String examId);

    @GET(ApiUrls.GET_MY_EXAMS)
    Call<GetExamsObject> getMyExams();

    @GET(ApiUrls.GET_TEST_PROPERTIES)
    Call<TestPropertiesObject> getTestProperties(@Query(ApiUrls.TASK_ID) String taskId);

    @GET(ApiUrls.GET_TEST_QUESTIONS)
    Call<QuestionsObject> getTestQuestions(@Query(ApiUrls.TASK_ID) String taskId);

    @GET(ApiUrls.GET_TEST_RESULTS)
    Call<TestResultsObject> getTestResults(@Query(ApiUrls.TASK_ID) String taskId,
                                           @Query(ApiUrls.TOTAL_TIME_TAKEN) long totalTimeTaken,
                                           @Query(ApiUrls.QUESTION) ArrayList<String> exams);

    @GET(ApiUrls.GET_REVIEW_ANSWERS)
    Call<TestReviewObject> getReviewAnswers(@Query(ApiUrls.TASK_ID) String taskId);

    @GET(ApiUrls.GET_STRATERGY)
    Call<StratergyObject> getStratergy();

    @GET(ApiUrls.GET_PAST_STRATERGY)
    Call<StratergyObject> getPastStratergy();

    @GET(ApiUrls.GET_PAST_STRATERGY)
    Call<StratergyObject> getPastStratergyWithPage(@Query(ApiUrls.MORE) int pageCount);

    @GET(ApiUrls.GET_PROFILE)
    Call<ProfileObject> getProfile();

    @Multipart
    @POST(ApiUrls.LOGIN)
    Call<JsonObject> loginFb(
            @Part(ApiUrls.FACEBOOK_ID) RequestBody fbId,
            @Part(ApiUrls.PROFILE_PIC_URL) RequestBody profilePic,
            @Part(ApiUrls.ONE_SIGNAL) RequestBody oneSignalId);

    @Multipart
    @POST(ApiUrls.LOGIN)
    Call<JsonObject> loginGoogle(
            @Part(ApiUrls.MAIL_ID) RequestBody mailId,
            @Part(ApiUrls.PROFILE_PIC_URL) RequestBody profilePic,
            @Part(ApiUrls.ONE_SIGNAL) RequestBody oneSignalId);

    @Multipart
    @POST(ApiUrls.LOGIN)
    Call<JsonObject> loginMobile(
            @Part(ApiUrls.MOBILE) RequestBody mobile,
            @Part(ApiUrls.ONE_SIGNAL) RequestBody oneSignalId);

    @POST(ApiUrls.REGISTER)
    Call<JsonObject> registerUser(@Query(ApiUrls.MAIL_ID) String mailId, @Query(ApiUrls.NAME) String name, @Query(ApiUrls
            .MOBILE) String mobile);

    @GET(ApiUrls.EDIT_PROFILE)
    Call<JSONObject> editProfile(@Query(ApiUrls.NAME) String name);

    @GET(ApiUrls.LOGOUT)
    Call<JSONObject> logoutUser();

    @GET(ApiUrls.GET_SUBJECTS)
    Call<GetSubjectsObject> getSubjects(@Query(ApiUrls.EXAM_NAME) String examName);

    @GET(ApiUrls.GET_TOPICS)
    Call<TopicsObject> getTopics(@Query(ApiUrls.EXAM_NAME) String examName,
                                 @Query(ApiUrls.SUBJECT_NAME) String subjectName);

    @GET(ApiUrls.GET_SUB_TOPICS)
    Call<SubTopicsObject> getSubTopics(@Query(ApiUrls.EXAM_NAME) String examName,
                                       @Query(ApiUrls.SUBJECT_NAME) String subjectName,
                                       @Query(ApiUrls.TOPIC_NAME) String topicName);

    @GET(ApiUrls.GET_ARTICLES)
    Call<ArticlesObject> getArticles(@Query(ApiUrls.EXAM_NAME) String examName,
                                     @Query(ApiUrls.SUBJECT_NAME) String subjectName,
                                     @Query(ApiUrls.TOPIC_NAME) String topicName,
                                     @Query(ApiUrls.SUB_TOPIC_NAME) String subTopicName);

    @GET(ApiUrls.GET_CURRENT_AFFAIRS)
    Call<CurrentAffairsListObject> getCurrentAffairs(@Query(ApiUrls.TOPIC_NAME) String topicName,
                                                     @Query(ApiUrls.DATE) String dateYYYYMMdd);

    @GET(ApiUrls.TASK_COMPLETE)
    Call<JsonObject> taskComplete(@Query(ApiUrls.TASK_ID) String taskId,
                                                     @Query(ApiUrls.ARTICLE_ID) String articleId);

    @GET(ApiUrls.GET_CONTACT)
    Call<GetContact> getContact();
}
