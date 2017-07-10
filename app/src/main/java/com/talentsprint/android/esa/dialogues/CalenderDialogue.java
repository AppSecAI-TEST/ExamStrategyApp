package com.talentsprint.android.esa.dialogues;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.fragments.CalenderFragment;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.utils.AppConstants;

/**
 * Created by Anudeep Reddy on 7/7/2017.
 */

public class CalenderDialogue extends DialogFragment implements CalenderInterface {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private float x_value, y_value;
    private View main_content;
    private View pointerView;

    public CalenderDialogue() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogue_calender, container);
        x_value = getArguments().getFloat(AppConstants.X_VALUE);
        y_value = getArguments().getFloat(AppConstants.Y_VALUE);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = view.findViewById(R.id.container);
        main_content = view.findViewById(R.id.main_content);
        pointerView = view.findViewById(R.id.pointerView);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setCurrentItem(AppConstants.CALENDER_TODAYS_PAGE_NUMBER);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        main_content.setY(y_value);
        pointerView.setX(x_value);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    @Override
    public void moveNext() {
        if (mViewPager.getCurrentItem() + 1 < AppConstants.CALENDER_TOTAL_PAGES)
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
    }

    @Override
    public void movePrevious() {
        if (mViewPager.getCurrentItem() - 1 >= 0)
            mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CalenderFragment.newInstance(position, x_value, y_value);
        }

        @Override
        public int getCount() {
            return 600;
        }

    }
}
