package com.talentsprint.android.esa.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

import com.onesignal.OSPermissionSubscriptionState;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        OneSignal.idsAvailable(new OneSignal.IdsAvailableHandler() {
            @Override
            public void idsAvailable(String userId, String registrationId) {
                OSPermissionSubscriptionState status = OneSignal.getPermissionSubscriptionState();
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
                }, 3000);
            }
        });
    }
}
