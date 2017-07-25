package com.talentsprint.android.esa.services;

import android.content.Context;

import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;
import com.talentsprint.android.esa.models.NotificationsObject;
import com.talentsprint.android.esa.utils.AppUtils;

import org.json.JSONObject;

/**
 * Created by Anudeep Reddy on 7/25/2017.
 */

public class NotificationOpenedHandler implements OneSignal.NotificationOpenedHandler {

    Context context;

    public NotificationOpenedHandler(Context context) {
        this.context = context;
    }

    @Override
    public void notificationOpened(OSNotificationOpenResult result) {
        JSONObject data = result.notification.payload.additionalData;
        NotificationsObject object = new NotificationsObject();
        object.setAddedDateLong(System.currentTimeMillis());
        object.setAddedDate(AppUtils.getDateInMMMDDYYYY(System.currentTimeMillis()));
        object.setExpiryDate(data.optString("expiryDate"));
        object.setCategory(data.optString("category"));
        object.setLink(data.optString("link"));
        object.setTitle(data.optString("title"));
        object.setType(data.optString("type"));
        AppUtils.navigateFromNotifications(context, object, false);
    }
}
