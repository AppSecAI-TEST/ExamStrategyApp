package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import com.talentsprint.android.esa.models.QuestionsObject;
import com.talentsprint.android.esa.utils.AppConstants;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizQuestionItemFragment extends Fragment implements View.OnClickListener {
    private TextView questionsCount;
    private TextView questionTimer;
    private TextView questionNumber;
    private WebView questionView;
    private TextView submit;
    private TextView skip;
    private RecyclerView optionsRecycler;
    private int currentQuestion = 0;
    private int totalQuestionsSize = 0;
    private QuestionsObject.Question question;
    private long timer = 0;
    private QuizInterface quizInterface;
    private Handler handler = new Handler();
    private Runnable runnable;
    private int optionsSelectedPosition = -1;

    public QuizQuestionItemFragment() {
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
        final View fragmentView = inflater.inflate(R.layout.fragment_question_item, container, false);
        findViews(fragmentView);
        currentQuestion = getArguments().getInt(AppConstants.POSITION) + 1;
        totalQuestionsSize = getArguments().getInt(AppConstants.TOTAL_QUESTIONS);
        question = (QuestionsObject.Question) getArguments().getSerializable(AppConstants.QUIZ_QUESTIONS);
        questionsCount.setText(currentQuestion + "/" + totalQuestionsSize);
        questionNumber.setText(Integer.toString(currentQuestion));
        questionView.loadData(question.getQuestion(), "text/html", "UTF-8");
        OptionsAdapter optionsAdapter = new OptionsAdapter(question.getOptions());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        optionsRecycler.setLayoutManager(mLayoutManager);
        optionsRecycler.setAdapter(optionsAdapter);
        final NumberFormat twoDigitFormater = new DecimalFormat("00");
        runnable = new Runnable() {
            @Override
            public void run() {
                timer++;
                if (timer < 60) {
                    questionTimer.setText("00:" + twoDigitFormater.format(timer));
                } else {
                    questionTimer.setText(twoDigitFormater.format(timer / 60) + ":" + twoDigitFormater.format(timer % 60));
                }
                handler.postDelayed(runnable, 1000);
            }
        };
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        questionsCount = fragmentView.findViewById(R.id.questionsCount);
        questionTimer = fragmentView.findViewById(R.id.questionTimer);
        questionNumber = fragmentView.findViewById(R.id.questionNumber);
        questionView = fragmentView.findViewById(R.id.question);
        optionsRecycler = fragmentView.findViewById(R.id.optionsRecycler);
        submit = fragmentView.findViewById(R.id.submit);
        skip = fragmentView.findViewById(R.id.skip);
        submit.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == submit) {
            if (optionsSelectedPosition > -1) {
                stopTimer();
                quizInterface.submitQuestion(question.getId() + "," + timer + "," + (optionsSelectedPosition + 1));
            } else {
                Toast.makeText(getActivity(), "Select an option to submit", Toast.LENGTH_SHORT).show();
            }
        } else if (view == skip) {
            stopTimer();
            quizInterface.submitQuestion(question.getId() + "," + timer + "," + 0);
        }
    }

    private void stopTimer() {
        if (runnable != null && handler != null)
            handler.removeCallbacks(runnable);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (runnable != null && handler != null)
            handler.post(runnable);
    }

    public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {

        private List<String> optionsList;

        public OptionsAdapter(List<String> optionsList) {
            this.optionsList = optionsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_question_option, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.optionText.setText(optionsList.get(position));
            if (optionsSelectedPosition == position) {
                holder.selectButton.setImageResource(R.drawable.radio_enable);
            } else {
                holder.selectButton.setImageResource(R.drawable.radio_disable);
            }
        }

        @Override
        public int getItemCount() {
            return optionsList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public ImageView selectButton;
            public TextView optionText;

            public MyViewHolder(View view) {
                super(view);
                selectButton = view.findViewById(R.id.selectButton);
                optionText = view.findViewById(R.id.optionText);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        optionsSelectedPosition = getAdapterPosition();
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
