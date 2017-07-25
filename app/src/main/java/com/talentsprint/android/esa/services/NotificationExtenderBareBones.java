package com.talentsprint.android.esa.services;

import com.onesignal.NotificationExtenderService;
import com.onesignal.OSNotificationReceivedResult;
import com.talentsprint.android.esa.utils.AppConstants;
import com.talentsprint.android.esa.utils.PreferenceManager;

/**
 * Created by Anudeep Reddy on 7/25/2017.
 */

public class NotificationExtenderBareBones extends NotificationExtenderService {
    @Override
    protected boolean onNotificationProcessing(OSNotificationReceivedResult notification) {
        if (PreferenceManager.getBoolean(this, AppConstants.NOTIFICATION, true))
            return false;
        else
            return true;
    }
}
