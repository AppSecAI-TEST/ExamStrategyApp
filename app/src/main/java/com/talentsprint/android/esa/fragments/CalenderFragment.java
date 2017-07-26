package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.models.CalenderObject;
import com.talentsprint.android.esa.utils.AppConstants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalenderFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private TextView monthText;
    private ImageView moveLeft;
    private ImageView moveRight;
    private TextView yearText;
    private TextView todayText;
    private RecyclerView monthRecycler;
    private int selectedPosition = -1;
    private int todaysPosition = -1;
    private boolean isShowTodaysText = false;
    private String[] monthsList = new String[]{"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY",
            "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER",};
    private CalenderInterface calenderInterface;
    private String currentMontString;
    private String currentYearString;

    public CalenderFragment() {
    }

    public static CalenderFragment newInstance(int sectionNumber, float x_value, float y_value, long selectedDateLong) {
        CalenderFragment fragment = new CalenderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putFloat(AppConstants.X_VALUE, x_value);
        args.putFloat(AppConstants.Y_VALUE, y_value);
        args.putLong(AppConstants.DATE_LONG, selectedDateLong);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        calenderInterface = (CalenderInterface) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_month_layout, container, false);
        findViews(rootView);
        if (getArguments().getInt(ARG_SECTION_NUMBER) == AppConstants.CALENDER_TODAYS_PAGE_NUMBER) {
            // Setting the visibility of today view
            if (getArguments().getLong(AppConstants.DATE_LONG, 0) == 0) {
                showTodayDate();
            } else {
                hideTodaysDate();
            }
        } else {
            hideTodaysDate();
        }
        new AsyncTask<Void, Void, ArrayList<CalenderObject>>() {

            @Override
            protected ArrayList<CalenderObject> doInBackground(Void... arrayLists) {
                return prepareDaysList();
            }

            @Override
            protected void onPostExecute(ArrayList<CalenderObject> daysList) {
                super.onPostExecute(daysList);
                monthText.setText(currentMontString);
                yearText.setText(currentYearString);
                SubjectsAdapter adapter = new SubjectsAdapter(daysList);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 7);
                monthRecycler.setLayoutManager(mLayoutManager);
                monthRecycler.setAdapter(adapter);
            }
        }.execute();
        return rootView;
    }

    private void hideTodaysDate() {
        todayText.setVisibility(View.INVISIBLE);
        isShowTodaysText = false;
    }

    private void showTodayDate() {
        todayText.setVisibility(View.VISIBLE);
        isShowTodaysText = true;
    }

    @NonNull
    private ArrayList<CalenderObject> prepareDaysList() {
        //Logic for displaying calender. Please recheck before changing - Anudeep
        Calendar cal = Calendar.getInstance();
        //Getting selected calender
        Calendar selectedCalender = Calendar.getInstance();
        selectedCalender.setTimeInMillis(cal.getTimeInMillis());
        long selectedDateLong = getArguments().getLong(AppConstants.DATE_LONG, 0);
        if (selectedDateLong > 0) {
            Calendar localCal = Calendar.getInstance();
            localCal.setTimeInMillis(selectedDateLong);
            selectedCalender.set(Calendar.DAY_OF_MONTH, localCal.get(Calendar.DAY_OF_MONTH));
            selectedCalender.set(Calendar.YEAR, localCal.get(Calendar.YEAR));
            selectedCalender.set(Calendar.MONTH, localCal.get(Calendar.MONTH));
            selectedDateLong = selectedCalender.getTimeInMillis();
        }
        //Getting present days date to compare and display in list - Anudeep
        long today = cal.getTimeInMillis();
        //Getting the actual present month and year - Anudeep
        int presentMonth = cal.get(Calendar.MONTH);
        int presentYear = cal.get(Calendar.YEAR);
        //Adding or subtracting the number of months depending on the page number - Anudeep
        cal.add(Calendar.MONTH, getArguments().getInt(ARG_SECTION_NUMBER) - AppConstants.CALENDER_TODAYS_PAGE_NUMBER);
        //Setting the day of month to 1 - Anudeep
        cal.set(Calendar.DAY_OF_MONTH, 1);
        currentMontString = monthsList[cal.get(Calendar.MONTH)];
        currentYearString = Integer.toString(cal.get(Calendar.YEAR));
        //Getting the day of the week - Anudeep
        int daysInweek1 = cal.get(Calendar.DAY_OF_WEEK);
        //Getting the maximum number of days of this particular month - Anudeep
        int maxDays = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //Calculating the number of previous month's days to be shown and subtracting them in the calender - Anudeep
        int previousMonthDays = 1 - daysInweek1;
        cal.add(Calendar.DATE, previousMonthDays);
        ArrayList<CalenderObject> daysList = new ArrayList<CalenderObject>();
        //Grid count is 7*6=42. So running the loop for 42 days and adding each day and pushing the object to the list - Anudeep
        for (int i = 0; i < 42; i++) {
            CalenderObject calenderObject = new CalenderObject();
            calenderObject.setDay(cal.get(Calendar.DAY_OF_MONTH));
            calenderObject.setTimeMillis(cal.getTimeInMillis());
            if (i < daysInweek1 - 1) {
                calenderObject.setThisMonth(false);
            } else if (i > maxDays + daysInweek1 - 2) {
                calenderObject.setThisMonth(false);
            } else {
                calenderObject.setThisMonth(true);
                if (selectedDateLong == 0 && today == cal.getTimeInMillis()) {
                    selectedPosition = i;
                    todaysPosition = i;
                } else {
                    if (selectedDateLong == cal.getTimeInMillis()) {
                        selectedPosition = i;
                    }
                }
            }
            daysList.add(calenderObject);
            cal.add(Calendar.DATE, 1);
        }
        return daysList;
    }

    private void findViews(View rootView) {
        monthRecycler = rootView.findViewById(R.id.monthRecycler);
        monthText = rootView.findViewById(R.id.monthText);
        moveLeft = rootView.findViewById(R.id.moveLeft);
        moveRight = rootView.findViewById(R.id.moveRight);
        yearText = rootView.findViewById(R.id.yearText);
        todayText = rootView.findViewById(R.id.todayText);
        moveLeft.setOnClickListener(this);
        moveRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == moveLeft) {
            calenderInterface.movePrevious();
        } else if (view == moveRight) {
            calenderInterface.moveNext();
        }
    }

    public class SubjectsAdapter extends RecyclerView.Adapter<SubjectsAdapter.MyViewHolder> {

        private List<CalenderObject> calendersList;

        public SubjectsAdapter(ArrayList<CalenderObject> calendersList) {
            this.calendersList = calendersList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_calender_date, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final CalenderObject calenderObject = calendersList.get(position);
            holder.dateView.setText(Integer.toString(calenderObject.getDay()));
            if (calenderObject.isThisMonth()) {
                holder.dateView.setTextColor(Color.parseColor("#555555"));
            } else {
                holder.dateView.setTextColor(Color.parseColor("#bababa"));
            }
            if (selectedPosition == position) {
                holder.selectedView.setVisibility(View.VISIBLE);
            } else {
                holder.selectedView.setVisibility(View.INVISIBLE);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (calenderObject.isThisMonth()) {
                        calenderInterface.selectedDate(calenderObject.getTimeMillis());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return calendersList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView dateView;
            private View selectedView;

            public MyViewHolder(View view) {
                super(view);
                dateView = view.findViewById(R.id.dateView);
                selectedView = view.findViewById(R.id.selectedView);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        }
    }
}
