package com.talentsprint.android.esa.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.talentsprint.apps.talentsprint.R;
import com.talentsprint.android.esa.fragments.StudyMaterialTopicsFragment;
import com.talentsprint.android.esa.interfaces.StudyMaterialActivityInterface;
import com.talentsprint.android.esa.models.TopicsObject;
import com.talentsprint.android.esa.utils.AppConstants;

public class StudyMaterialTopicActivity extends FragmentActivity implements StudyMaterialActivityInterface {

    private ImageView back;
    private TextView examName;
    private TextView subjectText;
    private TopicsObject topicsObject;
    private ProgressBar progressBar;
    private View progressBarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_study_material_topic);
        findViews();
        topicsObject = (TopicsObject) getIntent().getSerializableExtra(AppConstants.TOPICS);
        if (topicsObject != null) {
            if (topicsObject.getSubject() != null)
                subjectText.setText(topicsObject.getSubject().toUpperCase());
            else
                subjectText.setText("");
            if (topicsObject.getExam() != null)
                examName.setText(topicsObject.getExam().toUpperCase());
            else
                examName.setText("");
            openTopics();
        }
        subjectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAllFragments();
                openTopics();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void openTopics() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AppConstants.TOPICS, topicsObject);
        StudyMaterialTopicsFragment studyMaterialTopicsFragment = new StudyMaterialTopicsFragment();
        studyMaterialTopicsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, studyMaterialTopicsFragment, AppConstants.DASHBOARD).commit();
    }

    private void popAllFragments() {
        FragmentManager fm = getSupportFragmentManager();
        for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }
    }

    private void findViews() {
        back = findViewById(R.id.back);
        subjectText = findViewById(R.id.subjectText);
        examName = findViewById(R.id.examName);
        progressBar = findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);
        progressBar.setVisibility(View.GONE);
        progressBarView.setVisibility(View.GONE);
    }

    @Override
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
    public void doubleBack() {
        onBackPressed();
    }
}
