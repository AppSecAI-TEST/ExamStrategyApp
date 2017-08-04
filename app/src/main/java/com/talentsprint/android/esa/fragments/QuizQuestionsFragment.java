package com.talentsprint.android.esa.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.interfaces.QuizInterface;
import com.talentsprint.android.esa.models.PreviousAnswers;
import com.talentsprint.android.esa.models.QuestionsObject;
import com.talentsprint.android.esa.models.RealmString;
import com.talentsprint.android.esa.models.TestResultsObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizQuestionsFragment extends Fragment implements View.OnClickListener, QuizInterface {

    private DashboardActivityInterface dashboardInterface;

    private TextView totalTimer;
    private TextView finishTest;
    private QuestionsObject questionsObject;
    private ProgressBar progressBar;
    private TextView percentageText;
    private int currentQuestion = 0;
    private int totalQuestionsSize = 0;
    private String taskId;
    private ArrayList<String> answersList = new ArrayList<String>();
    private long totalTimeForQuestions = 0;
    private Handler handler = new Handler();
    private Runnable runnable;
    private Realm realm;
    private boolean answersSubmited = false;
    private PreviousAnswers previousAnswers;

    public QuizQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_quiz_questions, container, false);
        dashboardInterface.setCurveVisibility(true);
        realm = Realm.getDefaultInstance();
        findViews(fragmentView);
        taskId = getArguments().getString(AppConstants.TASK_ID);
        getQuestions();
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashboardInterface = (DashboardActivityInterface) context;
    }

    private void getQuestions() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        Call<QuestionsObject> stratergy = apiService.getTestQuestions(taskId);
        stratergy.enqueue(new Callback<QuestionsObject>() {
            @Override
            public void onResponse(Call<QuestionsObject> call, Response<QuestionsObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    questionsObject = response.body();
                    setValues();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<QuestionsObject> call, Throwable t) {
                if (dashboardInterface != null) {
                    dashboardInterface.showProgress(false);
                    getActivity().onBackPressed();
                }
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues() {
        totalTimeForQuestions = questionsObject.getTestTime();
        if (questionsObject.getQuestions().size() > 0) {
            totalQuestionsSize = questionsObject.getQuestions().size();
            progressBar.setMax(totalQuestionsSize);
            previousAnswers = realm.where(PreviousAnswers.class).equalTo("id", taskId).findFirst();
            if (previousAnswers != null && previousAnswers.getAnswers() != null && previousAnswers.getAnswers().size() > 0) {
                for (RealmString answer :
                        previousAnswers.getAnswers()) {
                    answersList.add(answer.getString());
                }
                totalTimeForQuestions = previousAnswers.getTimer();
                currentQuestion = answersList.size() - 1;
                if (currentQuestion < totalQuestionsSize - 1) {
                    currentQuestion++;
                    moveToNextQuestion();
                } else {
                    Toast.makeText(getActivity(), "Quiz has been completed Previously", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            } else {
                moveToNextQuestion();
            }
        } else {
            Toast.makeText(getActivity(), "No Questions", Toast.LENGTH_SHORT).show();
        }
        final NumberFormat twoDigitFormater = new DecimalFormat("00");
        runnable = new Runnable() {
            @Override
            public void run() {
                totalTimeForQuestions--;
                if (totalTimeForQuestions < 0) {
                    totalTimer.setTextColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                }
                long roundedNumber = Math.abs(totalTimeForQuestions);
                if (roundedNumber < 60) {
                    totalTimer.setText("00:00:" + twoDigitFormater.format(roundedNumber));
                } else if (roundedNumber < 3600) {
                    totalTimer.setText("00:" + twoDigitFormater.format(roundedNumber / 60) + ":" + twoDigitFormater
                            .format(roundedNumber % 60));
                } else {
                    long minutes = roundedNumber % 3600;
                    totalTimer.setText(twoDigitFormater.format(roundedNumber / 3600) + ":" + twoDigitFormater.format(
                            minutes / 60) + ":" + twoDigitFormater.format
                            (minutes % 60));
                }
                handler.postDelayed(runnable, 1000);
            }
        };
        handler.post(runnable);
    }

    private void stopTimer() {
        if (runnable != null && handler != null)
            handler.removeCallbacks(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
        if (!answersSubmited) {
            RealmList<RealmString> realmAnswers = new RealmList<RealmString>();
            for (String answer :
                    answersList) {
                RealmString realmString = new RealmString();
                realmString.setString(answer);
                realmAnswers.add(realmString);
            }
            PreviousAnswers answers = new PreviousAnswers();
            answers.setAnswers(realmAnswers);
            answers.setId(taskId);
            answers.setTimer(totalTimeForQuestions);
            realm.beginTransaction();
            realm.copyToRealmOrUpdate(answers);
            realm.commitTransaction();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (runnable != null && handler != null)
            handler.post(runnable);
    }

    protected void moveToNextQuestion() {
        progressBar.setProgress(currentQuestion);
        percentageText.setText(Integer.toString((int) (((float) (currentQuestion) / totalQuestionsSize) * 100)) + "%");
        QuizQuestionItemFragment quizQuestionItemFragment = new QuizQuestionItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.QUIZ_QUESTIONS, questionsObject.getQuestions().get(currentQuestion));
        bundle.putInt(AppConstants.POSITION, currentQuestion);
        bundle.putInt(AppConstants.TOTAL_QUESTIONS, totalQuestionsSize);
        quizQuestionItemFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_child_container, quizQuestionItemFragment, AppConstants.QUIZ_QUESTIONS).commit();
    }

    private void findViews(View fragmentView) {
        totalTimer = fragmentView.findViewById(R.id.totalTimer);
        finishTest = fragmentView.findViewById(R.id.finishTest);
        progressBar = fragmentView.findViewById(R.id.progressBar);
        percentageText = fragmentView.findViewById(R.id.percentageText);
        finishTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == finishTest) {
            showFinishDialogue();
        }
    }

    private void showFinishDialogue() {
        final Dialog finishDialogue;
        finishDialogue = new Dialog(getActivity(), android.R.style.Theme_Black_NoTitleBar);
        finishDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(150, 0, 0, 0)));
        finishDialogue.setContentView(R.layout.dialogue_exam_finish);
        TextView yes = finishDialogue.findViewById(R.id.yes);
        TextView cancel = finishDialogue.findViewById(R.id.cancel);
        View mainView = finishDialogue.findViewById(R.id.mainView);
        finishDialogue.show();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishDialogue.dismiss();
                submitAnswers();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishDialogue.dismiss();
            }
        });
        mainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishDialogue.dismiss();
            }
        });
    }

    @Override
    public void skipQuestion() {
        if (currentQuestion < questionsObject.getQuestions().size() - 1) {
            currentQuestion++;
            moveToNextQuestion();
        } else {
            submitAnswers();
        }
    }

    private void submitAnswers() {
        dashboardInterface.showProgress(true);
        if (answersList.size() != totalQuestionsSize) {
            ArrayList<QuestionsObject.Question> questionArrayList = questionsObject.getQuestions();
            for (int i = answersList.size(); i < totalQuestionsSize; i++) {
                answersList.add(questionArrayList.get(i).getId() + "," + 0 + "," + 0);
            }
        }
        TalentSprintApi apiService = ApiClient.getCacheClient(false).create(TalentSprintApi.class);
        long totalTime = 0;
        if (totalTimeForQuestions > 0) {
            totalTime = questionsObject.getTestTime() - totalTimeForQuestions;
        } else {
            totalTime = questionsObject.getTestTime() + totalTimeForQuestions;
        }
        Call<TestResultsObject> stratergy = apiService.getTestResults(taskId, totalTime, answersList);
        stratergy.enqueue(new Callback<TestResultsObject>() {
            @Override
            public void onResponse(Call<TestResultsObject> call, Response<TestResultsObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    TestResultsObject testResultsObject = response.body();
                    QuizResultFragment quizResultFragment = new QuizResultFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConstants.TEST_RESULT, testResultsObject);
                    bundle.putString(AppConstants.TASK_ID, taskId);
                    quizResultFragment.setArguments(bundle);
                    answersSubmited = true;
                    realm.beginTransaction();
                    if (previousAnswers != null)
                        previousAnswers.deleteFromRealm();
                    realm.commitTransaction();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.fragment_container, quizResultFragment, AppConstants.QUIZ_RESULT)
                            .addToBackStack(null).commit();

                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<TestResultsObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void submitQuestion(String answer) {
        answersList.add(answer);
        if (currentQuestion < totalQuestionsSize - 1) {
            currentQuestion++;
            moveToNextQuestion();
        } else {
            submitAnswers();
        }
    }
}
