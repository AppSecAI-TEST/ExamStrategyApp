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

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.dialogues.CalenderDialogue;
import com.talentsprint.android.esa.interfaces.DashboardActivityInterface;
import com.talentsprint.android.esa.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StratergyFragment extends Fragment implements View.OnClickListener {
    private DashboardActivityInterface dashboardInterface;
    private ImageView filter;
    private ImageView calender;
    private RecyclerView stratergyRecycler;

    public StratergyFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        dashboardInterface.setCurveVisibility(true);
        View fragmentView = inflater.inflate(R.layout.fragment_stratergy, container, false);
        findViews(fragmentView);
        StratergyAdapter stratergyAdapter = new StratergyAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        stratergyRecycler.setLayoutManager(mLayoutManager);
        stratergyRecycler.setAdapter(stratergyAdapter);
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
    }

    @Override
    public void onClick(View view) {
        if (view == calender) {
            Bundle bundle = new Bundle();
            bundle.putFloat(AppConstants.X_VALUE, calender.getX());
            int[] postions = new int[2];
            calender.getLocationInWindow(postions);
            bundle.putFloat(AppConstants.Y_VALUE, postions[1]);
            CalenderDialogue dialogue = new CalenderDialogue();
            dialogue.setArguments(bundle);
            dialogue.show(getFragmentManager(), null);
        }
    }

    public class StratergyAdapter extends RecyclerView.Adapter<StratergyAdapter.MyViewHolder> {

        private List<String> instructionsList;

        public StratergyAdapter(List<String> instructionsList) {
            this.instructionsList = instructionsList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_exam_stratergy, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            if (position % 2 == 0) {
                holder.dateDay.setVisibility(View.VISIBLE);
                holder.dateMonth.setVisibility(View.VISIBLE);
                holder.circle.setVisibility(View.VISIBLE);
            } else {
                holder.dateDay.setVisibility(View.INVISIBLE);
                holder.dateMonth.setVisibility(View.INVISIBLE);
                holder.circle.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return 15;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            public TextView dateDay;
            public TextView dateMonth;
            public View lineTop;
            public View lineBottom;
            public View circle;
            public TextView title;
            public TextView subTitle;
            public TextView time;

            public MyViewHolder(View view) {
                super(view);
                dateDay = view.findViewById(R.id.dateDay);
                dateMonth = view.findViewById(R.id.dateMonth);
                lineTop = view.findViewById(R.id.lineTop);
                lineBottom = view.findViewById(R.id.lineBottom);
                circle = view.findViewById(R.id.circle);
                title = view.findViewById(R.id.title);
                subTitle = view.findViewById(R.id.subTitle);
                time = view.findViewById(R.id.time);

            }
        }
    }
}
