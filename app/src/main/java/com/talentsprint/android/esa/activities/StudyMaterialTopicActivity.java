package com.talentsprint.android.esa.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.fragments.StudyMaterialTopicsFragment;
import com.talentsprint.android.esa.utils.AppConstants;

public class StudyMaterialTopicActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_study_material_topic);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new StudyMaterialTopicsFragment(), AppConstants.DASHBOARD).commit();
    }

  /*  @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }*/

}
