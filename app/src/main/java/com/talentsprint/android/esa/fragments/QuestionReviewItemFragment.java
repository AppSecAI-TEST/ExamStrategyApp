package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.QuizInterface;
import com.talentsprint.android.esa.models.TestReviewObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.views.OpenSansStrongTextView;
import com.talentsprint.android.esa.views.OpenSansTextView;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionReviewItemFragment extends Fragment {

    private OpenSansTextView questionsCount;
    private OpenSansTextView questionNumber;
    private WebView questionWebView;
    private RecyclerView optionsRecycler;
    private OpenSansStrongTextView correctAnswer;
    private WebView reasonWebView;
    private int currentQuestion = 0;
    private int totalQuestionsSize = 0;
    private int optionsSelectedPosition;
    private int correctPosition;
    private QuizInterface quizInterface;
    private TestReviewObject.Question question;

    public QuestionReviewItemFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        quizInterface = (QuizInterface) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_question_review_item, container, false);
        findViews(fragmentView);
        currentQuestion = getArguments().getInt(AppConstants.POSITION) + 1;
        totalQuestionsSize = getArguments().getInt(AppConstants.TOTAL_QUESTIONS);
        question = (TestReviewObject.Question) getArguments().getSerializable(AppConstants.QUIZ_QUESTIONS);
        optionsSelectedPosition = question.getUserOption() - 1;
        correctPosition = question.getCorrectOption() - 1;
        try {
            setValues();
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return fragmentView;
    }

    private void setValues() throws Exception {
        questionsCount.setText(currentQuestion + "/" + totalQuestionsSize);
        questionNumber.setText(Integer.toString(currentQuestion));
        questionWebView.loadData(question.getQuestion(), "text/html", "UTF-8");
        reasonWebView.loadData(question.getExplanation(), "text/html", "UTF-8");
        correctAnswer.setText("Correct Answer : " + question.getOptions().get(correctPosition));
        OptionsAdapter optionsAdapter = new OptionsAdapter(question.getOptions());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        optionsRecycler.setLayoutManager(mLayoutManager);
        optionsRecycler.setAdapter(optionsAdapter);
    }

    private void findViews(View fragmentView) {
        questionsCount = fragmentView.findViewById(R.id.questionsCount);
        questionNumber = fragmentView.findViewById(R.id.questionNumber);
        questionWebView = fragmentView.findViewById(R.id.question);
        optionsRecycler = fragmentView.findViewById(R.id.optionsRecycler);
        correctAnswer = fragmentView.findViewById(R.id.correctAnswer);
        reasonWebView = fragmentView.findViewById(R.id.reasonWebView);
    }

    public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {

        private List<String> optionsList;

        public OptionsAdapter(List<String> optionsList) {
            this.optionsList = optionsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_question_review_option, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.optionText.setText(optionsList.get(position));
            if (optionsSelectedPosition == position && position == correctPosition) {
                holder.mainView.setBackgroundResource(R.drawable.color_accent_runded_full_rect_stroke);
                holder.tick.setVisibility(View.VISIBLE);
            } else if (position == optionsSelectedPosition) {
                holder.mainView.setBackgroundResource(R.drawable.color_red_rounded_full_rect_stroke);
                holder.tick.setVisibility(View.INVISIBLE);
            } else if (position == correctPosition) {
                holder.mainView.setBackgroundResource(R.drawable.color_accent_runded_full_rect_stroke);
                holder.tick.setVisibility(View.VISIBLE);
            } else {
                holder.mainView.setBackgroundResource(0);
                holder.tick.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return optionsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView tick;
            public TextView optionText;
            public View mainView;

            public MyViewHolder(View view) {
                super(view);
                tick = view.findViewById(R.id.tick);
                optionText = view.findViewById(R.id.optionText);
                mainView = view;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
