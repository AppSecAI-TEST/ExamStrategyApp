package com.talentsprint.android.esa.fragments;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.activities.CurrentAffairsTopicsActivity;
import com.talentsprint.android.esa.activities.VideoPlayerActivity;
import com.talentsprint.android.esa.dialogues.CalenderDialogue;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.interfaces.CurrentAffairsInterface;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.CurrentAffairsObject;
import com.talentsprint.android.esa.models.HomeObject;
import com.talentsprint.android.esa.models.NotificationsObject;
import com.talentsprint.android.esa.models.TaskObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.AppUtils;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.android.esa.utils.TalentSprintApi;
import com.talentsprint.android.esa.views.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener, CurrentAffairsInterface, CalenderInterface {

    private RelativeLayout tasks;
    private RelativeLayout todaysTasksLyt;
    private ViewPager currentAffairsViewPager;
    private RecyclerView alertsRecyclerView, tasksRecycler;
    private TextView nextExam;
    private TextView nextExamDate;
    private CirclePageIndicator indicator;
    private DashboardActivityInterface dashboardInterface;
    private ImageView calenderView;
    private HomeObject homeObject;

    public DashboardFragment() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        findViews(fragmentView);
        dashboardInterface.setCurveVisibility(false);
        getDashBoard(0);
        getAlerts();
        return fragmentView;
    }

    private void getAlerts() {
        List<NotificationsObject> notificationsList = new ArrayList<NotificationsObject>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<NotificationsObject> notificationsObjects = realm.where(NotificationsObject.class).equalTo("type",
                AppConstants.ALERT).greaterThan
                ("expiryDateLong", System.currentTimeMillis()).findAll();
        notificationsList = realm.copyFromRealm(notificationsObjects);
        AlertsAdapter alertsAdapter = new AlertsAdapter(notificationsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        alertsRecyclerView.setLayoutManager(mLayoutManager);
        alertsRecyclerView.setAdapter(alertsAdapter);

    }

    private void getDashBoard(final int recurringValue) {
        dashboardInterface.showProgress(true);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String oneSignalId = status.getSubscriptionStatus().getUserId();
        TalentSprintApi apiService =
                dashboardInterface.getApiService();
        Call<HomeObject> getHomeDetails = apiService.getHome(oneSignalId);
        getHomeDetails.enqueue(new Callback<HomeObject>() {
            @Override
            public void onResponse(Call<HomeObject> call, Response<HomeObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    homeObject = response.body();
                    if (homeObject.getCurrentAffairs() == null && recurringValue == 0) {
                        dashboardInterface.showProgress(true);
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getDashBoard(1);
                            }
                        }, 1000);
                    } else if (isAdded()) {
                        setValues();
                    }
                }
            }

            @Override
            public void onFailure(Call<HomeObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues() {
        CurrentAffairsFragmentAdapter adapter = new CurrentAffairsFragmentAdapter(getChildFragmentManager(), homeObject
                .getCurrentAffairs());
        currentAffairsViewPager.setAdapter(adapter);
        indicator.setViewPager(currentAffairsViewPager);
        String status = homeObject.getStatus();
        //Conditions as per the API document provided by talent sprint
        if (status == null || status.equalsIgnoreCase(AppConstants.EXAM_NOT_SET)) {
            nextExamDate.setText("Not Set");
            View inflatedLayout = getActivity().getLayoutInflater().inflate(R.layout.include_set_exams_dashboard, null, false);
            todaysTasksLyt.addView(inflatedLayout);
            inflatedLayout.findViewById(R.id.setExam).setOnClickListener(this);
        } else {
            nextExamDate.setText(homeObject.getNextExam() + ", " + homeObject.getNextExamDate());
            if (status.equalsIgnoreCase(AppConstants.ASESMENT_NOT_TAKEN)) {
                View inflatedLayout = getActivity().getLayoutInflater().inflate(R.layout.include_assesment_dashboard, null,
                        false);
                todaysTasksLyt.addView(inflatedLayout);
                inflatedLayout.findViewById(R.id.assessYourself).setOnClickListener(this);
            } else if (status.equalsIgnoreCase(AppConstants.PREPARING_STRATERGY)) {
                View inflatedLayout = getActivity().getLayoutInflater().inflate(R.layout.include_intermediate_dashboard, null,
                        false);
                todaysTasksLyt.addView(inflatedLayout);
            } else if (status.equalsIgnoreCase(AppConstants.STRATERGY_IS_READY)) {
                //calenderView.setVisibility(View.VISIBLE);
                dashboardInterface.isStratergyReady(true);
                TasksAdapter tasksAdapter = new TasksAdapter(homeObject.getTasklist());
                RecyclerView.LayoutManager mtaskLayoutManager = new LinearLayoutManager(getActivity());
                tasksRecycler.setLayoutManager(mtaskLayoutManager);
                tasksRecycler.setAdapter(tasksAdapter);
            }
        }
        dashboardInterface.setExamDate(nextExamDate.getText().toString());
    }

    private void findViews(View fragmentView) {
        tasks = fragmentView.findViewById(R.id.tasks);
        todaysTasksLyt = fragmentView.findViewById(R.id.todaysTasksLyt);
        currentAffairsViewPager = fragmentView.findViewById(R.id.currentAffairsViewPager);
        alertsRecyclerView = fragmentView.findViewById(R.id.alertsRecyclerView);
        nextExam = fragmentView.findViewById(R.id.nextExam);
        nextExamDate = fragmentView.findViewById(R.id.nextExamDate);
        indicator = fragmentView.findViewById(R.id.indicator);
        calenderView = fragmentView.findViewById(R.id.calenderView);
        tasksRecycler = fragmentView.findViewById(R.id.tasksRecycler);
        nextExamDate.setText("");
        calenderView.setOnClickListener(this);
        nextExamDate.setOnClickListener(this);
        calenderView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setExam || view.getId() == R.id.nextExamDate) {
            openMyExams();
        } else if (view.getId() == R.id.assessYourself) {
            openQuiz("0");
        } else if (view == calenderView) {
            calenderView.setClickable(false);
            dashboardInterface.showProgress(true);
            Bundle bundle = new Bundle();
            bundle.putFloat(AppConstants.X_VALUE, calenderView.getX());
            int[] postions = new int[2];
            calenderView.getLocationInWindow(postions);
            bundle.putFloat(AppConstants.Y_VALUE, postions[1]);
            CalenderDialogue dialogue = new CalenderDialogue();
            dialogue.setArguments(bundle);
            dialogue.show(getChildFragmentManager(), null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    calenderView.setClickable(true);
                    dashboardInterface.showProgress(false);
                }
            }, 3000);
        }
    }

    private void openMyExams() {
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new MyExamsFragment(), AppConstants.MY_EXAMS).addToBackStack(null).commit();
    }

    @Override
    public void currentAffairsSelected() {
        Intent navigate = new Intent(getActivity(), CurrentAffairsTopicsActivity.class);
        navigate.putExtra(AppConstants.CURRENT_AFFAIRS, homeObject.getCurrentAffairs());
        navigate.putExtra(AppConstants.POSITION, currentAffairsViewPager.getCurrentItem());
        startActivity(navigate);
    }

    @Override
    public void moveNext() {
    }

    @Override
    public void movePrevious() {
    }

    @Override
    public void selectedDate(long date) {
    }

    private void openQuiz(String taskId2) {
        Bundle bundle = new Bundle();
        bundle.putString(AppConstants.TASK_ID, taskId2);
        QuizInstructionsFragment quizInstructionsFragment = new QuizInstructionsFragment();
        quizInstructionsFragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, quizInstructionsFragment, AppConstants.QUIZ_INSTRUCTIONS)
                .addToBackStack(null).commit();
    }

    private void openContent(TaskObject taskObject) {
        if (taskObject != null && taskObject.getContentUrl() != null) {
            if (taskObject.getContentUrl().contains(AppConstants.PDF)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(taskObject.getContentUrl()));
                try {
                    getActivity().startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), "No application found to open PDF", Toast.LENGTH_SHORT).show();
                }
            } else if (taskObject.getContentUrl().contains(AppConstants.MP4)) {
                Intent navigate = new Intent(getActivity(), VideoPlayerActivity.class);
                navigate.putExtra(AppConstants.URL, taskObject.getContentUrl());
                startActivity(navigate);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.URL, taskObject.getContentUrl());
                StrategyContentDisplayFragment quizInstructionsFragment = new StrategyContentDisplayFragment();
                quizInstructionsFragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, quizInstructionsFragment, AppConstants.CONTENT)
                        .addToBackStack(null).commit();
            }
        }
    }

    class CurrentAffairsFragmentAdapter extends FragmentStatePagerAdapter {

        ArrayList<CurrentAffairsObject> currentAffairs;

        public CurrentAffairsFragmentAdapter(FragmentManager fm, ArrayList<CurrentAffairsObject> currentAffairs) {
            super(fm);
            if (currentAffairs != null)
                this.currentAffairs = currentAffairs;
            else
                this.currentAffairs = new ArrayList<CurrentAffairsObject>();
        }

        @Override
        public Fragment getItem(int position) {
            CurrentAffairsViewPagerItem item = new CurrentAffairsViewPagerItem();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.CONTENT, currentAffairs.get(position).getShortDescription());
            item.setArguments(bundle);
            return item;
        }

        @Override
        public int getCount() {
            return currentAffairs.size();
        }
    }

    public class AlertsAdapter extends RecyclerView.Adapter<AlertsAdapter.MyViewHolder> {

        private List<NotificationsObject> alertssList;

        public AlertsAdapter(List<NotificationsObject> alertssList) {
            this.alertssList = alertssList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_your_alerts, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            holder.title.setText(alertssList.get(position).getTitle());
            holder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppUtils.navigateFromNotifications(getActivity(), alertssList.get(position), true);
                }
            });
        }

        @Override
        public int getItemCount() {
            if (alertssList != null)
                return alertssList.size();
            else
                return 0;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view;
            }
        }
    }

    public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> {

        private List<TaskObject> tasksList;

        public TasksAdapter(List<TaskObject> tasksList) {
            this.tasksList = tasksList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_tasks, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final TaskObject taskObject = tasksList.get(position);
            holder.subTopicName.setText(taskObject.getTitle());
            switch (taskObject.getType()) {
                case AppConstants.NON_VIDEO:
                    holder.topicName.setText("Non-Video");
                    holder.topicImage.setImageResource(R.drawable.word_of_the_day);
                    break;
                case AppConstants.VIDEO:
                    holder.topicName.setText("Video");
                    holder.topicImage.setImageResource(R.drawable.video);
                    break;
                case AppConstants.TEST:
                    holder.topicName.setText("Quiz");
                    holder.topicImage.setImageResource(R.drawable.quiz);
                    break;
                case AppConstants.WORD_OF_THE_DAY:
                    holder.topicName.setText("Word of the day");
                    holder.topicImage.setImageResource(R.drawable.word_of_the_day);
                    break;
            }
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!taskObject.isPremium() || PreferenceManager.getString(getActivity(), AppConstants.USER_TYPE, "")
                            .equalsIgnoreCase(AppConstants.PREMIUM)) {
                        switch (taskObject.getType()) {
                            case AppConstants.NON_VIDEO:
                                openContent(taskObject);
                                break;
                            case AppConstants.VIDEO:
                                openContent(taskObject);
                                break;
                            case AppConstants.TEST:
                                openQuiz(taskObject.getTaskId());
                                break;
                            case AppConstants.WORD_OF_THE_DAY:
                                openContent(taskObject);
                                break;
                        }
                    } else {
                        openContact(taskObject.getTitle());
                    }
                }

            });
        }

        private void openContact(String title) {
            GoToContactFragment goToContactFragment = new GoToContactFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstants.CONTENT, title);
            goToContactFragment.setArguments(bundle);
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, goToContactFragment, AppConstants.DASHBOARD)
                    .addToBackStack(null).commit();
        }

        @Override
        public int getItemCount() {
            return tasksList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView topicName, subTopicName;
            public ImageView topicImage;
            public View view;

            public MyViewHolder(View view) {
                super(view);
                topicName = view.findViewById(R.id.topicName);
                subTopicName = view.findViewById(R.id.subTopicName);
                topicImage = view.findViewById(R.id.topicImage);
                this.view = view;
            }
        }
    }
}
