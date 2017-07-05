package com.talentsprint.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talentsprint.R;
import com.talentsprint.interfaces.DashboardActivityInterface;
import com.talentsprint.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizQuestionsFragment extends Fragment implements View.OnClickListener {

    private DashboardActivityInterface dashboardInterface;
    private TextView questionsCount;
    private TextView questionTimer;
    private TextView questionNumber;
    private TextView question;
    private RecyclerView optionsRecycler;
    private TextView totalTimer;
    private TextView finishTest;
    private TextView submit;
    private TextView skip;

    public QuizQuestionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_quiz_questions, container, false);
        dashboardInterface.setCurveVisibility(true);
        findViews(fragmentView);
        OptionsAdapter optionsAdapter = new OptionsAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        optionsRecycler.setLayoutManager(mLayoutManager);
        optionsRecycler.setAdapter(optionsAdapter);
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashboardInterface = (DashboardActivityInterface) context;
    }

    private void findViews(View fragmentView) {
        questionsCount = fragmentView.findViewById(R.id.questionsCount);
        questionTimer = fragmentView.findViewById(R.id.questionTimer);
        questionNumber = fragmentView.findViewById(R.id.questionNumber);
        question = fragmentView.findViewById(R.id.question);
        optionsRecycler = fragmentView.findViewById(R.id.optionsRecycler);
        totalTimer = fragmentView.findViewById(R.id.totalTimer);
        finishTest = fragmentView.findViewById(R.id.finishTest);
        submit = fragmentView.findViewById(R.id.submit);
        skip = fragmentView.findViewById(R.id.skip);
        finishTest.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == finishTest) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new TestResultFragment(), AppConstants.QUIZ_RESULT)
                    .addToBackStack(null).commit();
        }
    }

    public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {

        private List<String> instructionsList;

        public OptionsAdapter(List<String> instructionsList) {
            this.instructionsList = instructionsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_question_option, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(View view) {
                super(view);
            }
        }
    }
}
