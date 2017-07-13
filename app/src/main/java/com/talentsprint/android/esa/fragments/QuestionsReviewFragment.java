package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.interfaces.QuizInterface;
import com.talentsprint.android.esa.models.TestReviewObject;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionsReviewFragment extends Fragment implements QuizInterface, View.OnClickListener {

    private TestReviewObject testReviewObject;
    private int currentQuestion = 0;
    private int totalQuestionsSize = 0;
    private ImageView moveRight;
    private ImageView moveLeft;
    private TextView results;
    private DashboardActivityInterface dashboardInterface;

    public QuestionsReviewFragment() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_questions_review, container, false);
        dashboardInterface.setCurveVisibility(true);
        testReviewObject = (TestReviewObject) getArguments().getSerializable(AppConstants.QUIZ_RESULT);
        totalQuestionsSize = testReviewObject.getQuestions().size();
        findViews(fragmentView);
        moveToNextQuestion();
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        moveRight = fragmentView.findViewById(R.id.moveRight);
        moveLeft = fragmentView.findViewById(R.id.moveLeft);
        results = fragmentView.findViewById(R.id.results);
        moveRight.setOnClickListener(this);
        moveLeft.setOnClickListener(this);
        results.setOnClickListener(this);
    }

    protected void moveToNextQuestion() {
        QuestionReviewItemFragment questionItemFragment = new QuestionReviewItemFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.QUIZ_QUESTIONS, testReviewObject.getQuestions().get(currentQuestion));
        bundle.putInt(AppConstants.POSITION, currentQuestion);
        bundle.putInt(AppConstants.TOTAL_QUESTIONS, totalQuestionsSize);
        questionItemFragment.setArguments(bundle);
        getChildFragmentManager().beginTransaction()
                .replace(R.id.fragment_child_container, questionItemFragment, AppConstants.QUIZ_QUESTIONS).commit();
    }

    @Override
    public void skipQuestion() {
    }

    @Override
    public void submitQuestion(String answer) {
    }

    @Override
    public void onClick(View view) {
        if (view == results) {
            getActivity().onBackPressed();
        } else if (view == moveLeft) {
            if (currentQuestion > 0) {
                currentQuestion--;
                moveToNextQuestion();
            }
        } else if (view == moveRight) {
            if (currentQuestion < totalQuestionsSize - 1) {
                currentQuestion++;
                moveToNextQuestion();
            }
        }
    }
}
