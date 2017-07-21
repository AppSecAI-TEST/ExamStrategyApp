package com.talentsprint.android.esa.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.dialogues.CalenderDialogue;
import com.talentsprint.android.esa.fragments.CurrentAffairsListFragment;
import com.talentsprint.android.esa.fragments.CurrentAffairsViewPagerFragment;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.models.CurrentAffairsListObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.AppUtils;
import com.talentsprint.android.esa.utils.DynamicWidthSpinner;
import com.talentsprint.android.esa.utils.TalentSprintApi;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CurrentAffairsTopicsActivity extends FragmentActivity implements View.OnClickListener, CalenderInterface {

    private ImageView back;
    private TextView currentAffairName;
    private DynamicWidthSpinner topicsNameSpinner;
    private View titleIndicator;
    private ImageView calender;
    private TextView dateText;
    private View dateDivider;
    private ProgressBar progressBar;
    private View progressBarView;
    private long affairsDate;
    private ArrayList<String> topicsList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_current_affairs_topics);
        affairsDate = System
                .currentTimeMillis();
        findViews();
        if (getIntent().getSerializableExtra(AppConstants.CURRENT_AFFAIRS) != null) {
            hideHeaders(true);
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstants.CURRENT_AFFAIRS, getIntent().getSerializableExtra(AppConstants.CURRENT_AFFAIRS));
            bundle.putInt(AppConstants.POSITION, getIntent().getIntExtra(AppConstants.POSITION, 0));
            CurrentAffairsViewPagerFragment currentAffairsViewPagerFragment = new CurrentAffairsViewPagerFragment();
            currentAffairsViewPagerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, currentAffairsViewPagerFragment, AppConstants.CURRENT_AFFAIRS).commit();
        } else {
            addTopics();
            TopicsAdapter topicsAdapter = new TopicsAdapter(CurrentAffairsTopicsActivity.this, R.layout
                    .drop_down_item_select_exam,
                    topicsList);
            topicsAdapter.setDropDownViewResource(R.layout.drop_down_item_select_exam);
            topicsNameSpinner.setAdapter(topicsAdapter);
            topicsNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    getTopics(topicsList.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            int selectedPostion = getIntent().getIntExtra(AppConstants.POSITION, 0);
            if (selectedPostion == 0) {
                getTopics(topicsList.get(0));
            } else {
                topicsNameSpinner.setSelection(selectedPostion);
            }
        }
    }

    private void hideHeaders(boolean isHide) {
        if (isHide) {
            topicsNameSpinner.setVisibility(View.GONE);
            titleIndicator.setVisibility(View.GONE);
            calender.setVisibility(View.GONE);
            dateText.setVisibility(View.GONE);
            dateDivider.setVisibility(View.GONE);
        } else {
            topicsNameSpinner.setVisibility(View.VISIBLE);
            titleIndicator.setVisibility(View.VISIBLE);
            calender.setVisibility(View.VISIBLE);
            dateText.setVisibility(View.VISIBLE);
            dateDivider.setVisibility(View.VISIBLE);
        }
    }

    private void addTopics() {
        /*National,International,Banking Economy and Finance,
        Art & Culture,Sports,Awards & Honors,Science & Technology,Environment - Topics given by Sravya and confirmed that they
        need to be hardcoded*/
        topicsList.add("Everything");
        topicsList.add("National");
        topicsList.add("International");
        topicsList.add("Banking Economy and Finance");
        topicsList.add("Art & Culture");
        topicsList.add("Sports");
        topicsList.add("Awards & Honors");
        topicsList.add("Science & Technology");
        topicsList.add("Environment");
    }

    private void getTopics(String topicName) {
        showProgress(true);
        TalentSprintApi apiService =
                ApiClient.getCacheClient().create(TalentSprintApi.class);
        Call<CurrentAffairsListObject> getExams = apiService.getCurrentAffairs(topicName, AppUtils.getDateInYYYMMDD(affairsDate));
        getExams.enqueue(new Callback<CurrentAffairsListObject>() {
            @Override
            public void onResponse(Call<CurrentAffairsListObject> call, Response<CurrentAffairsListObject> response) {
                showProgress(false);
                if (response.isSuccessful()) {
                    CurrentAffairsListObject currentAffairsListObject = response.body();
                    if (currentAffairsListObject.getCurrentAffairArticles() != null && currentAffairsListObject
                            .getCurrentAffairArticles().size() > 0)
                        dateText.setText(currentAffairsListObject.getCurrentAffairArticles().get(0).getDate());
                    CurrentAffairsListFragment currentAffairsListFragment = new CurrentAffairsListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(AppConstants.TOPICS, currentAffairsListObject);
                    currentAffairsListFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, currentAffairsListFragment, AppConstants.CURRENT_AFFAIRS).commit();
                } else {
                    Toast.makeText(CurrentAffairsTopicsActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<CurrentAffairsListObject> call, Throwable t) {
                try {
                    showProgress(false);
                    Toast.makeText(CurrentAffairsTopicsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void findViews() {
        back = findViewById(R.id.back);
        currentAffairName = findViewById(R.id.currentAffairName);
        topicsNameSpinner = findViewById(R.id.subjectNameSpinner);
        titleIndicator = findViewById(R.id.titleIndicator);
        calender = findViewById(R.id.calender);
        dateText = findViewById(R.id.dateText);
        dateDivider = findViewById(R.id.dateDivider);
        progressBar = findViewById(R.id.progressBar);
        progressBarView = findViewById(R.id.progressBarView);
        calender.setOnClickListener(this);
        back.setOnClickListener(this);
        progressBar.setVisibility(View.GONE);
        progressBarView.setVisibility(View.GONE);
        dateText.setText("");
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
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() == 0) {
            finish();
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == back) {
            onBackPressed();
        } else if (view == calender) {
            calender.setClickable(false);
            showProgress(true);
            Bundle bundle = new Bundle();
            bundle.putFloat(AppConstants.X_VALUE, calender.getX());
            int[] postions = new int[2];
            calender.getLocationInWindow(postions);
            bundle.putFloat(AppConstants.Y_VALUE, postions[1]);
            CalenderDialogue dialogue = new CalenderDialogue();
            dialogue.setArguments(bundle);
            dialogue.show(getSupportFragmentManager(), null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    calender.setClickable(true);
                    showProgress(false);
                }
            }, 3000);
        }
    }

    @Override
    public void moveNext() {
    }

    @Override
    public void movePrevious() {
    }

    @Override
    public void selectedDate(long date) {
        if (affairsDate != date) {
            affairsDate = date;
            getTopics(topicsList.get(topicsNameSpinner.getSelectedItemPosition()));
        }
    }

    public class TopicsAdapter extends ArrayAdapter<String> {

        public TopicsAdapter(Context context, int textViewResourceId, ArrayList<String> items) {
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
            if (getItem(position) != null)
                viewHolder.examName.setText(getItem(position).toUpperCase());
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
            if (getItem(position) != null)
                viewHolder.examName.setText(getItem(position).toUpperCase());
            return convertView;
        }

        public class ViewHolder {
            private TextView examName;
        }
    }
}
