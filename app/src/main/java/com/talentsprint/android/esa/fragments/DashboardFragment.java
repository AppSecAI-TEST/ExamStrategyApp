package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
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
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.models.CurrentAffairsObject;
import com.talentsprint.android.esa.models.HomeObject;
import com.talentsprint.android.esa.utils.ApiClient;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.TalentSprintApi;
import com.talentsprint.android.esa.views.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout tasks;
    private RelativeLayout todaysTasksLyt;
    private ViewPager currentAffairsViewPager;
    private RecyclerView alertsRecyclerView, tasksRecycler;
    private TextView nextExam;
    private TextView nextExamDate;
    private CirclePageIndicator indicator;
    private DashboardActivityInterface dashboardInterface;
    private ImageView calenderView;

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
        getDashBoard();
        return fragmentView;
    }

    private void getDashBoard() {
        dashboardInterface.showProgress(true);
        OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
        String oneSignalId = status.getSubscriptionStatus().getUserId();
        TalentSprintApi apiService =
                ApiClient.getClient().create(TalentSprintApi.class);
        Call<HomeObject> getHomeDetails = apiService.getHome(oneSignalId);
        getHomeDetails.enqueue(new Callback<HomeObject>() {
            @Override
            public void onResponse(Call<HomeObject> call, Response<HomeObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful())
                    setValues(response);
            }

            @Override
            public void onFailure(Call<HomeObject> call, Throwable t) {
                dashboardInterface.showProgress(false);
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setValues(Response<HomeObject> response) {
        HomeObject homeObject = response.body();
        CurrentAffairsFragmentAdapter adapter = new CurrentAffairsFragmentAdapter(getFragmentManager(), homeObject
                .getCurrentAffairs());
        currentAffairsViewPager.setAdapter(adapter);
        indicator.setViewPager(currentAffairsViewPager);
        AlertsAdapter alertsAdapter = new AlertsAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        alertsRecyclerView.setLayoutManager(mLayoutManager);
        alertsRecyclerView.setAdapter(alertsAdapter);
        String status = homeObject.getStatus();
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
                calenderView.setVisibility(View.VISIBLE);
            }
        }
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
        calenderView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setExam) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MyExamsFragment(), AppConstants.MY_EXAMS).addToBackStack(null).commit();
        } else if (view.getId() == R.id.assessYourself) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new QuizInstructionsFragment(), AppConstants.QUIZ_INSTRUCTIONS)
                    .addToBackStack(null).commit();
        }
    }

    class CurrentAffairsFragmentAdapter extends FragmentPagerAdapter {

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

        private List<String> alertssList;

        public AlertsAdapter(List<String> alertssList) {
            this.alertssList = alertssList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_your_alerts, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view;
            }
        }
    }

}