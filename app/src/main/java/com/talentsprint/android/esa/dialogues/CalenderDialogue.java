package com.talentsprint.android.esa.dialogues;

import android.app.Dialog;
import android.content.Context;
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

import com.talentsprint.android.esa.fragments.CalenderFragment;
import com.talentsprint.android.esa.interfaces.CalenderInterface;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.apps.talentsprint.R;

import java.util.Calendar;

/**
 * Created by Anudeep Reddy on 7/7/2017.
 */

public class CalenderDialogue extends DialogFragment implements CalenderInterface {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private View movableContent;
    private float x_value, y_value;
    private View main_content;
    private View pointerView;
    private long selectedDateLong, futureDateLong = 0;
    private CalenderInterface calenderInterface;

    public CalenderDialogue() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            calenderInterface = (CalenderInterface) getParentFragment();
            if (calenderInterface == null)
                calenderInterface = (CalenderInterface) getActivity();
        } catch (Exception e) {
            calenderInterface = (CalenderInterface) getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialogue_calender, container);
        x_value = getArguments().getFloat(AppConstants.X_VALUE);
        y_value = getArguments().getFloat(AppConstants.Y_VALUE);
        futureDateLong = getArguments().getLong(AppConstants.DATE_FUTURE, 0);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = view.findViewById(R.id.container);
        main_content = view.findViewById(R.id.main_content);
        movableContent = view.findViewById(R.id.movableContent);
        pointerView = view.findViewById(R.id.pointerView);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        selectedDateLong = getArguments().getLong(AppConstants.DATE_LONG, 0);
        if (selectedDateLong > 0) {
            Calendar calSelected = Calendar.getInstance();
            Calendar calPresent = Calendar.getInstance();
            calSelected.setTimeInMillis(selectedDateLong);
            int diffYear = calSelected.get(Calendar.YEAR) - calPresent.get(Calendar.YEAR);
            int diffMonth = diffYear * 12 + calSelected.get(Calendar.MONTH) - calPresent.get(Calendar.MONTH);
            mViewPager.setCurrentItem(AppConstants.CALENDER_TODAYS_PAGE_NUMBER + diffMonth);
        } else {
            mViewPager.setCurrentItem(AppConstants.CALENDER_TODAYS_PAGE_NUMBER);
        }
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 0, 0, 0)));
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
        movableContent.setY(y_value);
        pointerView.setX(x_value);
        main_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        movableContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
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

    @Override
    public void selectedDate(long date) {
        calenderInterface.selectedDate(date);
        Bundle bundle = new Bundle();
        bundle.putLong("selectedDate",date);

        dismiss();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return CalenderFragment.newInstance(position, x_value, y_value, selectedDateLong, futureDateLong);
        }

        @Override
        public int getCount() {
            return 600;
        }

    }
}
