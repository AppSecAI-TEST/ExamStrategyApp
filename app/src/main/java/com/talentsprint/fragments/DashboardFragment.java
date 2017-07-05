package com.talentsprint.fragments;

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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.talentsprint.R;
import com.talentsprint.interfaces.DashboardActivityInterface;
import com.talentsprint.utils.AppConstants;
import com.talentsprint.views.CirclePageIndicator;
import com.talentsprint.views.OpenSansTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout tasks;
    private RelativeLayout todaysTasksLyt;
    private ViewPager currentAffairsViewPager;
    private RecyclerView alertsRecyclerView;
    private OpenSansTextView nextExam;
    private OpenSansTextView nextExamDate;
    private CirclePageIndicator indicator;
    private Button setExam;
    private DashboardActivityInterface dashboardInterface;

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
        TestFragmentAdapter adapter = new TestFragmentAdapter(getFragmentManager());
        currentAffairsViewPager.setAdapter(adapter);
        indicator.setViewPager(currentAffairsViewPager);
        AlertsAdapter alertsAdapter = new AlertsAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        alertsRecyclerView.setLayoutManager(mLayoutManager);
        alertsRecyclerView.setAdapter(alertsAdapter);
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        tasks = fragmentView.findViewById(R.id.tasks);
        todaysTasksLyt = fragmentView.findViewById(R.id.todaysTasksLyt);
        currentAffairsViewPager = fragmentView.findViewById(R.id.currentAffairsViewPager);
        alertsRecyclerView = fragmentView.findViewById(R.id.alertsRecyclerView);
        nextExam = fragmentView.findViewById(R.id.nextExam);
        nextExamDate = fragmentView.findViewById(R.id.nextExamDate);
        indicator = fragmentView.findViewById(R.id.indicator);
        setExam = fragmentView.findViewById(R.id.setExam);
        setExam.setOnClickListener(this);
        setExam.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, new QuizInstructionsFragment(), AppConstants.QUIZ_INSTRUCTIONS)
                        .addToBackStack(null).commit();
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == setExam) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new MyExamsFragment(), AppConstants.MY_EXAMS).addToBackStack(null).commit();
        }
    }

    class TestFragmentAdapter extends FragmentPagerAdapter {

        public TestFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            CurrentAffairsViewPagerItem item = new CurrentAffairsViewPagerItem();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.CONTENT, "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
                    "Lorem Ihas been the printing and typesettinand types Lorem Ipsum is simply dummy text of the printing and " +
                    "typesetting industry. Lorem Ihas been the printing and typesettinand types ...");
            item.setArguments(bundle);
            return item;
        }

        @Override
        public int getCount() {
            return 5;
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
