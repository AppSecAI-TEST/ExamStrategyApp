package com.talentsprint.android.esa.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.models.ExamObject;
import com.talentsprint.android.esa.models.GetExamsObject;
import com.talentsprint.android.esa.models.GetSubjectsObject;
import com.talentsprint.android.esa.models.TopicsObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StudyMaterialActivity extends Activity implements View.OnClickListener {

    private RecyclerView topicsRecyclerView;
    private ImageView back;
    private Spinner subjectNameSpinner;
    private ExamsAdapter spinnerAdapter;
    private ProgressBar progressBar;
    private View progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_study_material);
        findViews();
        getExams();
    }

    private void findViews() {
        topicsRecyclerView = findViewById(R.id.topicsRecyclerView);
        back = findViewById(R.id.back);
        subjectNameSpinner = findViewById(R.id.subjectNameSpinner);
        progressBar = findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);
        back.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
        progressBarView.setVisibility(View.GONE);
    }

    public void showProgress(boolean isShow) {
        if (isShow) {
            progressBar.setVisibility(View.VISIBLE);
            progressBarView.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            progressBarView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    private void getExams() {
        showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<GetExamsObject> getExams = apiService.getExams();
        getExams.enqueue(new Callback<GetExamsObject>() {
            @Override
            public void onResponse(Call<GetExamsObject> call, Response<GetExamsObject> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    setValues(response);
                } else {
                    Toast.makeText(StudyMaterialActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<GetExamsObject> call, Throwable t) {
                try {
                    showProgress(false);
                    Toast.makeText(StudyMaterialActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getStudyMaterial(String examName) {
        showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<GetSubjectsObject> getExams = apiService.getSubjects(examName);
        getExams.enqueue(new Callback<GetSubjectsObject>() {
            @Override
            public void onResponse(Call<GetSubjectsObject> call, Response<GetSubjectsObject> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    GetSubjectsObject subjectsObject = response.body();
                    SubjectsAdapter adapter = new SubjectsAdapter(subjectsObject.getSubjects(), subjectsObject.getExamName());
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(StudyMaterialActivity.this);
                    topicsRecyclerView.setLayoutManager(mLayoutManager);
                    topicsRecyclerView.setAdapter(adapter);
                } else {
                    Toast.makeText(StudyMaterialActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<GetSubjectsObject> call, Throwable t) {
                try {
                    showProgress(false);
                    Toast.makeText(StudyMaterialActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getTopics(String examName, String subectName) {
        showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<TopicsObject> getExams = apiService.getTopics(examName, subectName);
        getExams.enqueue(new Callback<TopicsObject>() {
            @Override
            public void onResponse(Call<TopicsObject> call, Response<TopicsObject> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    Intent navigate = new Intent(StudyMaterialActivity.this, StudyMaterialTopicActivity.class);
                    navigate.putExtra(AppConstants.TOPICS, response.body());
                    startActivity(navigate);
                } else {
                    Toast.makeText(StudyMaterialActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<TopicsObject> call, Throwable t) {
                try {
                    showProgress(false);
                    Toast.makeText(StudyMaterialActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setValues(Response<GetExamsObject> response) {
        final ArrayList<ExamObject> exams1ist = response.body().getExams();
        if (exams1ist != null && exams1ist.size() > 0) {
            getStudyMaterial(exams1ist.get(0).getName());
            ArrayList<ExamObject> exams = exams1ist;
            spinnerAdapter = new ExamsAdapter(StudyMaterialActivity.this, R.layout.drop_down_item_select_exam,
                    exams);
            spinnerAdapter.setDropDownViewResource(R.layout.drop_down_item_select_exam);
            subjectNameSpinner.setAdapter(spinnerAdapter);
            subjectNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    getStudyMaterial(exams1ist.get(position).getName());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            onBackPressed();
        }
    }

    public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

        private List<String> subjectsList;
        private String subjectNameString;

        public SubjectsAdapter(List<String> subjectsList, String subjectName) {
            this.subjectsList = subjectsList;
            this.subjectNameString = subjectName;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_subject, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.subjectName.setText(subjectsList.get(position));
            switch (subjectsList.get(position)) {
                case AppConstants.APTITUDE:
                    holder.subjectImage.setImageResource(R.drawable.aptitude);
                    break;
                case AppConstants.ENGLISH:
                    holder.subjectImage.setImageResource(R.drawable.english);
                    break;
                case AppConstants.RESONING:
                    holder.subjectImage.setImageResource(R.drawable.reasoning);
                    break;
                case AppConstants.GENERAL_AWARENESS:
                    holder.subjectImage.setImageResource(R.drawable.general_awareness);
                    break;
                default:
                    holder.subjectImage.setImageResource(R.drawable.general_awareness);
                    break;
            }
        }

        @Override
        public int getItemCount() {
            if (subjectsList != null)
                return subjectsList.size();
            else
                return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView subjectName;
            public ImageView subjectImage;

            public MyViewHolder(View view) {
                super(view);
                subjectName = view.findViewById(R.id.subjectName);
                subjectImage = view.findViewById(R.id.subjectImage);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getTopics(subjectNameString, subjectsList.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

    public class ExamsAdapter extends ArrayAdapter<ExamObject> {

        public ExamsAdapter(Context context, int textViewResourceId, ArrayList<ExamObject> items) {
            super(context, textViewResourceId, items);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.drop_down_item_select_exam_strong, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.examName = convertView.findViewById(R.id.examName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ExamObject item = getItem(position);
            if (item != null)
                viewHolder.examName.setText(item.getName().toUpperCase());
            else
                viewHolder.examName.setText("");
            return convertView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(this.getContext())
                        .inflate(R.layout.drop_down_item_select_exam_list_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.examName = convertView.findViewById(R.id.examName);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            ExamObject item = getItem(position);
            if (item != null) {
                viewHolder.examName.setText(item.getName().toUpperCase());
            }
            return convertView;
        }

        public class ViewHolder {
            private TextView examName;
        }
    }
}
