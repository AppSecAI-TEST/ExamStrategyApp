package com.talentsprint.android.esa.services;

import com.onesignal.OSNotification;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.models.NotificationsObject;
import com.talentsprint.android.esa.utils.AppUtils;

import org.json.JSONObject;

import io.realm.Realm;

/**
 * Created by Anudeep Reddy on 7/25/2017.
 */

public class NotificationReceivedHandler implements OneSignal.NotificationReceivedHandler {
    @Override
    public void notificationReceived(OSNotification notification) {
        JSONObject data = notification.payload.additionalData;
        if (data != null) {
            Realm realm = Realm.getDefaultInstance();
            NotificationsObject object = new NotificationsObject();
            object.setAddedDateLong(System.currentTimeMillis());
            object.setAddedDate(AppUtils.getDateInMMMDDYYYY(System.currentTimeMillis()));
            object.setExpiryDate(data.optString("expiryDate"));
            object.setCategory(data.optString("category"));
            object.setLink(data.optString("link"));
            object.setTitle(data.optString("title"));
            object.setType(data.optString("type"));
            if (data.optString("expiryDate") != null) {
                try {
                    object.setExpiryDateLong(AppUtils.getLongFromDDMMYYY(data.optString("expiryDate")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            realm.beginTransaction();
            realm.copyToRealm(object);
            realm.commitTransaction();
        }
    }
}
