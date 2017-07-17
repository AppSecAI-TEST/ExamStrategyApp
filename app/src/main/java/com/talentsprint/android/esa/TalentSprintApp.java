package com.talentsprint.android.esa;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OneSignal;

/**
 * Created by Anudeep Reddy on 7/5/2017.
 */

public class TalentSprintApp extends Application {
    public static TalentSprintApp appContext;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
