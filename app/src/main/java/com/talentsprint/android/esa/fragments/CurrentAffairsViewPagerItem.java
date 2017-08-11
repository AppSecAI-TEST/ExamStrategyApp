package com.talentsprint.android.esa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talentsprint.android.esa.interfaces.CurrentAffairsInterface;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.apps.talentsprint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentAffairsViewPagerItem extends Fragment {

    CurrentAffairsInterface currentAffairsInterface;

    public CurrentAffairsViewPagerItem() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        currentAffairsInterface = (CurrentAffairsInterface) getParentFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.view_pager_item_current_affairs, container, false);
        TextView content = (TextView) fragmentView;
        content.setText(getArguments().getString(AppConstants.CONTENT, ""));
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAffairsInterface.currentAffairsSelected();
            }
        });
        return fragmentView;
    }

}
