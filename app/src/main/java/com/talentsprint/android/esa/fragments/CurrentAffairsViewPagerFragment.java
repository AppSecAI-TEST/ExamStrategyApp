package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.models.CurrentAffairsObject;
import com.talentsprint.android.esa.utils.AppConstants;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentAffairsViewPagerFragment extends Fragment {

    private ViewPager contentViewPager;
    private ArrayList<CurrentAffairsObject> currentAffairsList;

    public CurrentAffairsViewPagerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_current_affairs_view_pager, container, false);
        currentAffairsList = (ArrayList<CurrentAffairsObject>) getArguments().
                getSerializable(AppConstants.CURRENT_AFFAIRS);
        contentViewPager = fragmentView.findViewById(R.id.contentViewPager);
        CurrentAffairsFragmentAdapter adapter = new CurrentAffairsFragmentAdapter(getChildFragmentManager(), currentAffairsList);
        contentViewPager.setAdapter(adapter);
        return fragmentView;
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
            WebviewDisplayFragment item = new WebviewDisplayFragment();
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.URL, currentAffairs.get(position).getContentUrl());
            item.setArguments(bundle);
            return item;
        }

        @Override
        public int getCount() {
            return currentAffairs.size();
        }
    }
}
