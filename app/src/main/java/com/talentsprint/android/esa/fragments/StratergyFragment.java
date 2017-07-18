package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.afollestad.sectionedrecyclerview.SectionedViewHolder;
import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.dialogues.CalenderDialogue;
import com.talentsprint.android.esa.dialogues.FilterDialogue;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.interfaces.FiltersInterface;
import com.talentsprint.android.esa.models.StratergyObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.AppUtils;
import com.talentsprint.android.esa.utils.TalentSprintApi;
import com.talentsprint.android.esa.views.LinearLayoutManagerWithSmoothScroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StratergyFragment extends Fragment implements View.OnClickListener, CalenderInterface, FiltersInterface {
    private DashboardActivityInterface dashboardInterface;
    private ImageView filter;
    private ImageView calender;
    private RecyclerView stratergyRecycler;
    private StratergyObject stratergyObject;
    private String currentDate;
    private int dateRowNumber = 0;
    private HashMap<String, Integer> dateIndexingMap = new HashMap<String, Integer>();
    private HashMap<String, Boolean> selectedContentFilters = new HashMap<String, Boolean>();
    private HashMap<String, Boolean> selectedSubjectFilters = new HashMap<String, Boolean>();

    public StratergyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardInterface.setCurveVisibility(true);
        View fragmentView = inflater.inflate(R.layout.fragment_stratergy, container, false);
        findViews(fragmentView);
        currentDate = AppUtils.getCurrentDateString();
        getStratergy();
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        dashboardInterface = (DashboardActivityInterface) context;
    }

    private void findViews(View fragmentView) {
        filter = fragmentView.findViewById(R.id.filter);
        calender = fragmentView.findViewById(R.id.calender);
        stratergyRecycler = fragmentView.findViewById(R.id.stratergyRecycler);
        calender.setOnClickListener(this);
        filter.setOnClickListener(this);

    }

    private void getStratergy() {
        dashboardInterface.showProgress(true);
        TalentSprintApi apiService = dashboardInterface.getApiService();
        Call<StratergyObject> stratergy = apiService.getStratergy();
        stratergy.enqueue(new Callback<StratergyObject>() {
            @Override
            public void onResponse(Call<StratergyObject> call, Response<StratergyObject> response) {
                dashboardInterface.showProgress(false);
                if (response.isSuccessful()) {
                    stratergyObject = response.body();
                    prepareStratergy();
                } else {
                    Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<StratergyObject> call, Throwable t) {
                if (dashboardInterface != null)
                    dashboardInterface.showProgress(false);
                if (getActivity() != null)
                    Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareStratergy() {
        ArrayList<StratergyObject.Stratergy> stratergyArrayList = stratergyObject.getStrategy();
        HashMap<String, ArrayList<StratergyObject.Task>> taskHashMap = new HashMap<String, ArrayList<StratergyObject.Task>>();
        dateRowNumber = 0;
        dateIndexingMap = new HashMap<String, Integer>();
        if (stratergyArrayList != null) {
            for (int i = 0; i < stratergyArrayList.size(); i++) {
                ArrayList<StratergyObject.Task> monthTasks = new ArrayList<StratergyObject.Task>();
                StratergyObject.Stratergy monthStratergy = stratergyArrayList.get(i);
                if (monthStratergy.getMonthTasks() != null) {
                    for (int k = 0; k < monthStratergy.getMonthTasks().size(); k++) {
                        StratergyObject.MonthTasks dayStratergy = monthStratergy.getMonthTasks().get(k);
                        for (int j = 0; j < dayStratergy.getTasks().size(); j++) {
                            StratergyObject.Task singleTask = dayStratergy.getTasks().get(j);
                            singleTask.setDate(dayStratergy.getDate());
                            if (j == 0) {
                                dateIndexingMap.put(dayStratergy.getDate(), dateRowNumber + 1);
                                singleTask.setShowDate(true);
                                String[] dateSplit = dayStratergy.getDateMonth().split(" ");
                                singleTask.setDay(dateSplit[0]);
                                if (dateSplit.length > 1) {
                                    singleTask.setDayName(dateSplit[1]);
                                }
                            }
                            monthTasks.add(singleTask);
                            dateRowNumber++;
                        }
                    }
                    if (taskHashMap.containsKey(monthStratergy.getMonth())) {
                        taskHashMap.get(monthStratergy.getMonth()).addAll(monthTasks);
                    } else {
                        if (monthTasks.size() > 0) {
                            dateRowNumber++;
                            taskHashMap.put(monthStratergy.getMonth(), monthTasks);
                        }
                    }
                }
            }
            List<String> monthsList = new ArrayList<String>(taskHashMap.keySet());
            Collections.reverse(monthsList);
            StratergyAdapter stratergyAdapter = new StratergyAdapter(taskHashMap, (ArrayList<String>) monthsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWithSmoothScroller(getActivity());
            stratergyRecycler.setLayoutManager(mLayoutManager);
            stratergyRecycler.setAdapter(stratergyAdapter);
            if (dateIndexingMap.containsKey(currentDate))
                stratergyRecycler.scrollToPosition(dateIndexingMap.get(currentDate));
        } else {
            Toast.makeText(getActivity(), "Strategy not found", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
    }

    private void filterStratergy() {
        ArrayList<StratergyObject.Stratergy> stratergyArrayList = stratergyObject.getStrategy();
        HashMap<String, ArrayList<StratergyObject.Task>> taskHashMap = new HashMap<String, ArrayList<StratergyObject.Task>>();
        dateRowNumber = 0;
        dateIndexingMap = new HashMap<String, Integer>();
        if (stratergyArrayList != null) {
            for (int i = 0; i < stratergyArrayList.size(); i++) {
                ArrayList<StratergyObject.Task> monthTasks = new ArrayList<StratergyObject.Task>();
                StratergyObject.Stratergy monthStratergy = stratergyArrayList.get(i);
                if (monthStratergy.getMonthTasks() != null) {
                    for (int k = 0; k < monthStratergy.getMonthTasks().size(); k++) {
                        StratergyObject.MonthTasks dayStratergy = monthStratergy.getMonthTasks().get(k);
                        int dateIndex = -1;
                        for (int j = 0; j < dayStratergy.getTasks().size(); j++) {
                            StratergyObject.Task singleTask = dayStratergy.getTasks().get(j);
                            boolean isAdd = checkConditionToAddTask(singleTask);
                            if (isAdd) {
                                singleTask.setDate(dayStratergy.getDate());
                                if (dateIndex == -1) {
                                    dateIndexingMap.put(dayStratergy.getDate(), dateRowNumber + 1);
                                    singleTask.setShowDate(true);
                                    String[] dateSplit = dayStratergy.getDateMonth().split(" ");
                                    singleTask.setDay(dateSplit[0]);
                                    if (dateSplit.length > 1) {
                                        singleTask.setDayName(dateSplit[1]);
                                    }
                                    dateIndex++;
                                }
                                monthTasks.add(singleTask);
                                dateRowNumber++;
                            }
                        }
                    }
                    if (taskHashMap.containsKey(monthStratergy.getMonth())) {
                        taskHashMap.get(monthStratergy.getMonth()).addAll(monthTasks);
                    } else {
                        if (monthTasks.size() > 0) {
                            dateRowNumber++;
                            taskHashMap.put(monthStratergy.getMonth(), monthTasks);
                        }
                    }
                }
            }
            List<String> monthsList = new ArrayList<String>(taskHashMap.keySet());
            Collections.reverse(monthsList);
            StratergyAdapter stratergyAdapter = new StratergyAdapter(taskHashMap, (ArrayList<String>) monthsList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManagerWithSmoothScroller(getActivity());
            stratergyRecycler.setLayoutManager(mLayoutManager);
            stratergyRecycler.setAdapter(stratergyAdapter);
            if (dateIndexingMap.containsKey(currentDate))
                stratergyRecycler.scrollToPosition(dateIndexingMap.get(currentDate));
        } else {
            Toast.makeText(getActivity(), "Strategy not found", Toast.LENGTH_SHORT).show();
            getActivity().onBackPressed();
        }
    }

    private boolean checkConditionToAddTask(StratergyObject.Task singleTask) {
        boolean isAdd;
        if (selectedSubjectFilters.size() > 0 && selectedContentFilters.size() > 0) {
            if (singleTask.getSubject() != null && singleTask.getContentType() != null && selectedSubjectFilters.containsKey
                    (singleTask.getSubject().toLowerCase()) && selectedContentFilters
                    .containsKey(singleTask.getContentType().toLowerCase())) {
                isAdd = true;
            } else {
                isAdd = false;
            }
        } else if (selectedSubjectFilters.size() > 0 || selectedContentFilters.size() > 0) {
            if ((singleTask.getSubject() != null && selectedSubjectFilters.containsKey(singleTask.getSubject().toLowerCase()))
                    || (singleTask.getContentType() != null && selectedContentFilters
                    .containsKey(singleTask.getContentType().toLowerCase()))) {
                isAdd = true;
            } else {
                isAdd = false;
            }
        } else {
            isAdd = true;
        }
        return isAdd;
    }

    @Override
    public void onClick(View view) {
        if (view == calender) {
            calender.setClickable(false);
            dashboardInterface.showProgress(true);
            Bundle bundle = new Bundle();
            bundle.putFloat(AppConstants.X_VALUE, calender.getX());
            int[] postions = new int[2];
            calender.getLocationInWindow(postions);
            bundle.putFloat(AppConstants.Y_VALUE, postions[1]);
            CalenderDialogue dialogue = new CalenderDialogue();
            dialogue.setArguments(bundle);
            dialogue.show(getChildFragmentManager(), null);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    calender.setClickable(true);
                    dashboardInterface.showProgress(false);
                }
            }, 3000);
        } else if (view == filter) {
            if (stratergyObject != null && stratergyObject.getFilterOptions() != null) {
                filter.setClickable(false);
                Bundle bundle = new Bundle();

                bundle.putFloat(AppConstants.X_VALUE, filter.getX());
                int[] postions = new int[2];
                filter.getLocationInWindow(postions);
                bundle.putFloat(AppConstants.Y_VALUE, postions[1]);
                bundle.putSerializable(AppConstants.FILTERS, stratergyObject.getFilterOptions());
                bundle.putSerializable(AppConstants.CONTENT_FILTERS, selectedContentFilters);
                bundle.putSerializable(AppConstants.SUBJECT_FILTERS, selectedSubjectFilters);
                FilterDialogue dialogue = new FilterDialogue();
                dialogue.setArguments(bundle);
                dialogue.show(getChildFragmentManager(), null);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        filter.setClickable(true);
                    }
                }, 2000);
            } else {
                Toast.makeText(getActivity(), "No Option to filter", Toast.LENGTH_SHORT).show();
            }
        }
    }

    int getStatusImage(String status) {
        if (status != null) {
            switch (status) {
                case "Overdue":
                    return R.drawable.error_dialogue;
                case "Completed":
                    return R.drawable.tick_circle;
                default:
                    return R.drawable.empty;
            }
        } else {
            return R.drawable.empty;
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
        String selectedDate = AppUtils.getDateInYYYMMDD(date);
        if (dateIndexingMap.containsKey(selectedDate)) {
            stratergyRecycler.smoothScrollToPosition(dateIndexingMap.get(selectedDate));
        } else {
            Toast.makeText(getActivity(), "No strategy found for the selected date", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void filtersSet(HashMap<String, Boolean> selectedContentFilters, HashMap<String, Boolean> selectedSubjectFilters) {
        this.selectedContentFilters = selectedContentFilters;
        this.selectedSubjectFilters = selectedSubjectFilters;
        filterStratergy();
    }

    public class StratergyAdapter extends SectionedRecyclerViewAdapter<StratergyAdapter.MyViewHolder> {

        private HashMap<String, ArrayList<StratergyObject.Task>> tasksHashMap;
        private ArrayList<String> monthsList;

        public StratergyAdapter(HashMap<String, ArrayList<StratergyObject.Task>> tasksHashMap, ArrayList<String> monthsList) {
            this.tasksHashMap = tasksHashMap;
            this.monthsList = monthsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutRes;
            switch (viewType) {
                case VIEW_TYPE_HEADER:
                    layoutRes = R.layout.list_item_exam_stratergy_header;
                    break;
                default:
                    layoutRes = R.layout.list_item_exam_stratergy;
                    break;
            }
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(layoutRes, parent, false);
            return new MyViewHolder(v);
        }

        @Override
        public int getSectionCount() {
            return monthsList.size();
        }

        @Override
        public int getItemCount(int section) {
            return tasksHashMap.get(monthsList.get(section)).size();
        }

        @Override
        public void onBindHeaderViewHolder(MyViewHolder holder, int section, boolean expanded) {
            holder.dateMonth.setText(monthsList.get(section));
        }

        @Override
        public void onBindFooterViewHolder(MyViewHolder holder, int section) {
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int section, int relativePosition, int absolutePosition) {
            StratergyObject.Task taskObject = tasksHashMap.get(monthsList.get(section)).get(relativePosition);
            if (taskObject.isShowDate()) {
                holder.dateDay.setVisibility(View.VISIBLE);
                holder.dateWeekday.setVisibility(View.VISIBLE);
                holder.circle.setVisibility(View.VISIBLE);
            } else {
                holder.dateDay.setVisibility(View.INVISIBLE);
                holder.dateWeekday.setVisibility(View.INVISIBLE);
                holder.circle.setVisibility(View.INVISIBLE);
            }
            if (relativePosition == 0) {
                holder.lineTop.setVisibility(View.INVISIBLE);
            } else {
                holder.lineTop.setVisibility(View.VISIBLE);
            }
            holder.title.setText(taskObject.getTitle());
            holder.time.setText(taskObject.getDuration() / 60 + " min");
            holder.dateDay.setText(taskObject.getDay());
            holder.dateWeekday.setText(taskObject.getDayName());
            holder.indicator.setImageResource(getStatusImage(taskObject.getStatus()));
        }

        public class MyViewHolder extends SectionedViewHolder {

            public TextView dateDay;
            public TextView dateWeekday;
            public View lineTop;
            public View lineBottom;
            public View circle;
            public TextView title;
            //public TextView subTitle;
            public TextView time;
            public ImageView indicator;
            public TextView dateMonth;

            public MyViewHolder(View view) {
                super(view);
                dateMonth = view.findViewById(R.id.dateMonth);
                dateDay = view.findViewById(R.id.dateDay);
                dateWeekday = view.findViewById(R.id.dateWeekday);
                lineTop = view.findViewById(R.id.lineTop);
                lineBottom = view.findViewById(R.id.lineBottom);
                circle = view.findViewById(R.id.circle);
                title = view.findViewById(R.id.title);
                // subTitle = view.findViewById(R.id.subTitle);
                indicator = view.findViewById(R.id.indicator);
                time = view.findViewById(R.id.time);
            }

        }
    }
}

