package com.talentsprint.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.talentsprint.R;
import com.talentsprint.interfaces.DashboardActivityInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyExamsFragment extends Fragment implements View.OnClickListener {

    private ImageView add;
    private RelativeLayout noExams;
    private Button setExam, save, cancel;
    private RecyclerView examsRecycler;
    private DashboardActivityInterface dashboardInterface;

    public MyExamsFragment() {
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
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_my_exams, container, false);
        findViews(fragmentView);
        dashboardInterface.setCurveVisibility(true);
        AddExamsAdapter alertsAdapter = new AddExamsAdapter(new ArrayList<String>());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        examsRecycler.setLayoutManager(mLayoutManager);
        examsRecycler.setAdapter(alertsAdapter);
        return fragmentView;
    }

    private void findViews(View fragmentView) {
        add = fragmentView.findViewById(R.id.add);
        noExams = fragmentView.findViewById(R.id.noExams);
        setExam = fragmentView.findViewById(R.id.setExam);
        examsRecycler = fragmentView.findViewById(R.id.examsRecycler);
        save = fragmentView.findViewById(R.id.save);
        cancel = fragmentView.findViewById(R.id.cancel);
        examsRecycler.setVisibility(View.GONE);
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
        setExam.setOnClickListener(this);
        save.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == setExam) {
            examsRecycler.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            noExams.setVisibility(View.GONE);
        }
    }

    public class AddExamsAdapter extends RecyclerView.Adapter<AddExamsAdapter.MyViewHolder> {

        private List<String> alertssList;

        public AddExamsAdapter(List<String> alertssList) {
            this.alertssList = alertssList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_add_exam, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.examCount.setText(Integer.toString(position + 1));
        }

        @Override
        public int getItemCount() {
            return 4;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView examCount;
            public TextView examText;
            public TextView examName;
            public View divider;
            public ImageView calender;
            public ImageView delete;

            public MyViewHolder(View view) {
                super(view);
                examCount = view.findViewById(R.id.examCount);
                examText = view.findViewById(R.id.examText);
                examName = view.findViewById(R.id.examName);
                divider = view.findViewById(R.id.divider);
                calender = view.findViewById(R.id.calender);
                delete = view.findViewById(R.id.delete);
            }
        }
    }

}
