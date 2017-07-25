package com.talentsprint.android.esa;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.services.NotificationOpenedHandler;
import com.talentsprint.android.esa.services.NotificationReceivedHandler;

import io.realm.Realm;
import io.realm.RealmConfiguration;

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
                .setNotificationReceivedHandler(new NotificationReceivedHandler())
                .setNotificationOpenedHandler(new NotificationOpenedHandler(this))
                .init();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("esaRealm.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }
}
