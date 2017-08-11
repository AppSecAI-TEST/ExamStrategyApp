package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.TestResultsObject;
import com.talentsprint.android.esa.models.TestReviewObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.TalentSprintApi;
import com.talentsprint.apps.talentsprint.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizResultFragment extends Fragment implements View.OnClickListener {

    private DashboardActivityInterface dashboardInterface;
    private TestResultsObject testResultsObject;
    private TextView testTopic;
    private TextView testSubTopic;
    private TextView percentage;
    private TextView timeTaken;
    private TextView total;
    private TextView correctl;
    private TextView wrong;
    private TextView skipped;
    private TextView extraTakenText;
    private TextView extraTime;
    private TextView feedback;
    private TextView done;
    private TextView reviewAnswers;
    private String taskId;

    public QuizResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashboardInterface = (DashboardActivityInterface) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_test_result, container, false);
        dashboardInterface.setCurveVisibility(false);
        testResultsObject = (TestResultsObject) getArguments().getSerializable(AppConstants.TEST_RESULT);
        taskId = getArguments().getString(AppConstants.TASK_ID);
        findViews(fragmentView);
        try {
            setValues();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        done.setOnClickListener(this);
        reviewAnswers.setOnClickListener(this);
        return fragmentView;
    }

    private void setValues() throws Exception {
        testTopic.setText(testResultsObject.getTestproperties().getSubject());
        String topicsString = "";
        if (testResultsObject.getTestproperties().getTopic() != null && testResultsObject.getTestproperties().getTopic().length
                () > 0) {
            topicsString = testResultsObject.getTestproperties().getTopic();
            if (testResultsObject.getTestproperties().getSubTopic() != null && testResultsObject.getTestproperties().getSubTopic()
                    .length
                            () > 0)
                topicsString = topicsString + " | " + testResultsObject.getTestproperties()
                        .getSubTopic();
        } else {
            topicsString = "";
        }
        testSubTopic.setText(topicsString);
        percentage.setText(testResultsObject.getScore());
        timeTaken.setText(testResultsObject.getTimeTaken());
        total.setText(Integer.toString(testResultsObject.getTotal()));
        correctl.setText(Integer.toString(testResultsObject.getCorrect()));
        wrong.setText(Integer.toString(testResultsObject.getWrong()));
        skipped.setText(Integer.toString(testResultsObject.getTotal() - testResultsObject.getCorrect() - testResultsObject
                .getWrong()));
        if (testResultsObject.getExtraTime() == null || testResultsObject.getExtraTime().equalsIgnoreCase("00:00:00")) {
            extraTime.setText("Well Done");
            extraTakenText.setVisibility(View.INVISIBLE);
        } else {
            extraTime.setText(testResultsObject.getExtraTime());
        }
        feedback.setText(testResultsObject.getSpeed());
    }

    private void findViews(View fragmentView) {
        testTopic = fragmentView.findViewById(R.id.testTopic);
        testSubTopic = fragmentView.findViewById(R.id.testSubTopic);
        percentage = fragmentView.findViewById(R.id.percentage);
        timeTaken = fragmentView.findViewById(R.id.timeTaken);
        total = fragmentView.findViewById(R.id.total);
        correctl = fragmentView.findViewById(R.id.correctl);
        wrong = fragmentView.findViewById(R.id.wrong);
        skipped = fragmentView.findViewById(R.id.skipped);
        extraTakenText = fragmentView.findViewById(R.id.extraTakenText);
        extraTime = fragmentView.findViewById(R.id.extraTime);
        feedback = fragmentView.findViewById(R.id.feedback);
        done = fragmentView.findViewById(R.id.done);
        reviewAnswers = fragmentView.findViewById(R.id.reviewAnswers);
    }

    @Override
    public void onClick(View view) {
        if (view == done) {
            dashboardInterface.examAdded();
        } else if (view == reviewAnswers) {
            getReviewAnswers();
        }
    }

    private void getReviewAnswers() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        long totalTime = 0;
        Call<TestReviewObject> stratergy = apiService.getReviewAnswers(taskId);
        stratergy.enqueue(new Callback<TestReviewObject>() {
            @Override
            public void onResponse(Call<TestReviewObject> call, Response<TestReviewObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    TestReviewObject testResultsObject = response.body();
                    QuestionsReviewFragment questionsReviewFragment = new QuestionsReviewFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConstants.QUIZ_RESULT, testResultsObject);
                    questionsReviewFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, questionsReviewFragment, AppConstants.QUIZ_QUESTIONS)
                            .addToBackStack(null).commit();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<TestReviewObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
