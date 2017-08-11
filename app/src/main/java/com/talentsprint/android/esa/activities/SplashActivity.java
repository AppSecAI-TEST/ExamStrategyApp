package com.talentsprint.android.esa.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import com.talentsprint.apps.talentsprint.R;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.PreferenceManager;
import com.talentsprint.android.esa.utils.ServiceManager;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        if (PreferenceManager.getString(SplashActivity.this, AppConstants.ONE_SIGNAL_ID, "").equalsIgnoreCase("")) {
            if (!new ServiceManager(SplashActivity.this).isNetworkAvailable()) {
                Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
            }
            OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
                    @Override
                    public void idsAvailable(String userId, String registrationId) {
                        PreferenceManager.saveString(SplashActivity.this, AppConstants.ONE_SIGNAL_ID, registrationId);
                        navigateToHome();
                    }
            });
        } else {
            navigateToHome();
            }
    }

    private void navigateToHome() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Intent navigate = new Intent(SplashActivity.this, DashboardActivity.class);
                    startActivity(navigate);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1500);
    }
}
