package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.graphics.Color;
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

    public CalenderFragment() {
    }

    public static CalenderFragment newInstance(int sectionNumber, float x_value, float y_value) {
        CalenderFragment fragment = new CalenderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putFloat(AppConstants.X_VALUE, x_value);
        args.putFloat(AppConstants.Y_VALUE, y_value);
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
        ArrayList<CalenderObject> daysList = prepareDaysList();
        SubjectsAdapter adapter = new SubjectsAdapter(daysList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 7);
        monthRecycler.setLayoutManager(mLayoutManager);
        monthRecycler.setAdapter(adapter);
        Bundle arguments = getArguments();
        return rootView;
    }

    @NonNull
    private ArrayList<CalenderObject> prepareDaysList() {
        //Logic for displaying calender. Please recheck before changing - Anudeep
        Calendar cal = Calendar.getInstance();
        //Getting present days date to compare and display in list - Anudeep
        long today = cal.getTimeInMillis();
        //Getting the actual present month and year - Anudeep
        int presentMonth = cal.get(Calendar.MONTH);
        int presentYear = cal.get(Calendar.YEAR);
        //Adding or subtracting the number of months depending on the page number - Anudeep
        cal.add(Calendar.MONTH, getArguments().getInt(ARG_SECTION_NUMBER) - AppConstants.CALENDER_TODAYS_PAGE_NUMBER);
        //Checking if present month and year are equal to the added month and year - Anudeep
        if (presentYear == cal.get(Calendar.YEAR) && presentMonth == cal.get(Calendar.MONTH)) {
            // Setting the visibility of today view
            todayText.setVisibility(View.VISIBLE);
            isShowTodaysText = true;
        } else {
            todayText.setVisibility(View.INVISIBLE);
            isShowTodaysText = false;
        }
        //Setting the day of month to 1 - Anudeep
        cal.set(Calendar.DAY_OF_MONTH, 1);
        monthText.setText(monthsList[cal.get(Calendar.MONTH)]);
        yearText.setText(Integer.toString(cal.get(Calendar.YEAR)));
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
                if (today == cal.getTimeInMillis()) {
                    selectedPosition = i;
                    todaysPosition = i;
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
            return 42;
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
