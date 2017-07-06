package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentAffairsViewPagerItem extends Fragment {

    public CurrentAffairsViewPagerItem() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.view_pager_item_current_affairs, container, false);
        TextView content = (TextView) fragmentView;
        content.setText(getArguments().getString(AppConstants.CONTENT, ""));
        return fragmentView;
    }

}
