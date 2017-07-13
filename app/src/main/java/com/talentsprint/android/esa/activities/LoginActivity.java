package com.talentsprint.android.esa.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;

import com.talentsprint.android.esa.R;
import com.talentsprint.android.esa.fragments.LoginHomeFragment;
import com.talentsprint.android.esa.utils.AppConstants;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_login);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new LoginHomeFragment(), AppConstants.DASHBOARD).commit();
    }
}
