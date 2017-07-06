package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.views.OpenSansTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizInstructionsFragment extends Fragment implements View.OnClickListener {

    private OpenSansTextView time;
    private OpenSansTextView questionsCount;
    private ImageView tickImage;
    private RecyclerView typeRecycler;
    private RecyclerView instructionsRecycler;
    private Button startExam;
    private DashboardActivityInterface dashboardInterface;

    public QuizInstructionsFragment() {
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
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_quiz_instructions, container, false);
        findViews(fragmentView);
        dashboardInterface.setCurveVisibility(true);
        InstructionsAdapter instructionsAdapter = new InstructionsAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        instructionsRecycler.setLayoutManager(mLayoutManager);
        instructionsRecycler.setAdapter(instructionsAdapter);
        QuestionTypeAdapter questionssAdapter = new QuestionTypeAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        typeRecycler.setLayoutManager(mLayoutManager1);
        typeRecycler.setAdapter(questionssAdapter);
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        time = fragmentView.findViewById(R.id.time);
        questionsCount = fragmentView.findViewById(R.id.questionsCount);
        tickImage = fragmentView.findViewById(R.id.tickImage);
        typeRecycler = fragmentView.findViewById(R.id.typeRecycler);
        instructionsRecycler = fragmentView.findViewById(R.id.instructionsRecycler);
        startExam = fragmentView.findViewById(R.id.startExam);
        startExam.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == startExam) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new QuizQuestionsFragment(), AppConstants.QUIZ_QUESTIONS)
                    .addToBackStack(null).commit();
        }
    }

    public class QuestionTypeAdapter extends RecyclerView.Adapter<QuestionTypeAdapter.MyViewHolder> {

        private List<String> questionTypeList;

        public QuestionTypeAdapter(List<String> questionTypeList) {
            this.questionTypeList = questionTypeList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_question_type, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public MyViewHolder(View view) {
                super(view);
            }
        }
    }

    public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.MyViewHolder> {

        private List<String> instructionsList;

        public InstructionsAdapter(List<String> instructionsList) {
            this.instructionsList = instructionsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_question_instructiona, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 2;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public MyViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.instructionsText);
            }
        }
    }
}
