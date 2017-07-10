package com.talentsprint.android.esa.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.fragments.CurrentAffairsListFragment;
import com.talentsprint.android.esa.fragments.CurrentAffairsViewPagerFragment;
import com.talentsprint.android.esa.utils.AppConstants;

public class CurrentAffairsTopicsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_current_affairs_topics);
        if (getIntent().getSerializableExtra(AppConstants.CURRENT_AFFAIRS) != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(AppConstants.CURRENT_AFFAIRS, getIntent().getSerializableExtra(AppConstants.CURRENT_AFFAIRS));
            CurrentAffairsViewPagerFragment currentAffairsViewPagerFragment = new CurrentAffairsViewPagerFragment();
            currentAffairsViewPagerFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, currentAffairsViewPagerFragment, AppConstants.CURRENT_AFFAIRS).commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new CurrentAffairsListFragment(), AppConstants.CURRENT_AFFAIRS).commit();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

}
