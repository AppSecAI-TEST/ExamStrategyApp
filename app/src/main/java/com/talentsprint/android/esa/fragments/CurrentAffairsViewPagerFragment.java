package com.talentsprint.android.esa.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.talentsprint.android.esa.models.CurrentAffairsObject;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.apps.talentsprint.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentAffairsViewPagerFragment extends Fragment {

    private ViewPager contentViewPager;
    private int affairsListSize;
    private TextView counterTxt;
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
        affairsListSize = currentAffairsList.size();
        contentViewPager = fragmentView.findViewById(R.id.contentViewPager);
        counterTxt = fragmentView.findViewById(R.id.counterTxt);
        CurrentAffairsFragmentAdapter adapter = new CurrentAffairsFragmentAdapter(getChildFragmentManager(), currentAffairsList);
        contentViewPager.setAdapter(adapter);
        counterTxt.setText("1/" + affairsListSize);
        contentViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                counterTxt.setText((position + 1) + "/" + affairsListSize);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        contentViewPager.setCurrentItem(getArguments().getInt(AppConstants.POSITION, 0));
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
            bundle.putString("title",currentAffairs.get(position).getTitle());
            bundle.putString("imageURL",currentAffairs.get(position).getImageUrl());
            bundle.putString("description",currentAffairs.get(position).getDescription());
            item.setArguments(bundle);
            return item;
        }

        @Override
        public int getCount() {
            return currentAffairs.size();
        }
    }
}
