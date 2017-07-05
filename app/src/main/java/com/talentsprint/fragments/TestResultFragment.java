package com.talentsprint.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talentsprint.R;
import com.talentsprint.interfaces.DashboardActivityInterface;

/**
 * A simple {@link Fragment} subclass.
 */
public class TestResultFragment extends Fragment {

    private DashboardActivityInterface dashboardInterface;

    public TestResultFragment() {
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
        View fragmentView = inflater.inflate(R.layout.fragment_test_result, container, false);
        dashboardInterface.setCurveVisibility(false);
        return fragmentView;
    }

}
