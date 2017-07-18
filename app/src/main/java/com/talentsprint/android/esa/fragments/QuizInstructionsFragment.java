package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.TestPropertiesObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class QuizInstructionsFragment extends Fragment implements View.OnClickListener {

    private TextView time;
    private TextView questionsCount;
    private ImageView tickImage;
    private TextView subjectName;
    private TextView topicName;
    private TextView examsText;
    private RecyclerView instructionsRecycler;
    private TextView startExam;
    private DashboardActivityInterface dashboardInterface;
    private String taskId;
    private TestPropertiesObject testPropertiesObject;

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
        taskId = getArguments().getString(AppConstants.TASK_ID);
        getInstructions();
        return fragmentView;
    }

    private void getInstructions() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = dashboardInterface.getApiService();
        Call<TestPropertiesObject> stratergy = apiService.getTestProperties(taskId);
        stratergy.enqueue(new Callback<TestPropertiesObject>() {
            @Override
            public void onResponse(Call<TestPropertiesObject> call, Response<TestPropertiesObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    testPropertiesObject = response.body();
                    setValues();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<TestPropertiesObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues() {
        try {
            InstructionsAdapter instructionsAdapter = new InstructionsAdapter(testPropertiesObject.getInstructions());
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            instructionsRecycler.setLayoutManager(mLayoutManager);
            instructionsRecycler.setAdapter(instructionsAdapter);
            questionsCount.setText(testPropertiesObject.getTestQuestions());
            examsText.setText(testPropertiesObject.getTestName());
            TestPropertiesObject.TestProperties testproperties = testPropertiesObject.getTestproperties();
            subjectName.setText(testproperties.getSubject());
            if (testproperties.getSubTopic() != null) {
                topicName.setText(testproperties.getTopic() + " | " + testproperties.getSubTopic());
            } else {
                topicName.setText(testproperties.getTopic());
            }
            if (testPropertiesObject.getTestTime() != null && testPropertiesObject.getTestTime().length() > 0) {
                Double timeGiven = Double.parseDouble(testPropertiesObject.getTestTime());
                String displayText = "";
                double durationHours = timeGiven / 3600;
                if (durationHours == 1) {
                    time.setText("1 hr");
                } else if (timeGiven > 3600) {
                    time.setText(((int) durationHours) + " hr " + (int) ((timeGiven % 3600) / 60) + " min");
                } else if (timeGiven > 3600) {
                    time.setText((int) ((timeGiven % 3600) / 60) + " min");
                } else {
                    time.setText("");
                }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void findViews(View fragmentView) {
        time = fragmentView.findViewById(R.id.time);
        questionsCount = fragmentView.findViewById(R.id.questionsCount);
        tickImage = fragmentView.findViewById(R.id.tickImage);
        subjectName = fragmentView.findViewById(R.id.subjectName);
        topicName = fragmentView.findViewById(R.id.topicName);
        instructionsRecycler = fragmentView.findViewById(R.id.instructionsRecycler);
        startExam = fragmentView.findViewById(R.id.startExam);
        examsText = fragmentView.findViewById(R.id.examsText);
        startExam.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == startExam) {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.TASK_ID, taskId);
            QuizQuestionsFragment quizQuestionsFragment = new QuizQuestionsFragment();
            quizQuestionsFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, quizQuestionsFragment, AppConstants.QUIZ_QUESTIONS)
                    .addToBackStack(null).commit();
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
            holder.text.setText(instructionsList.get(position));
        }

        @Override
        public int getItemCount() {
            if (instructionsList != null)
                return instructionsList.size();
            else
                return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView text;

            public MyViewHolder(View view) {
                super(view);
                text = view.findViewById(R.id.text);
            }
        }
    }
}
