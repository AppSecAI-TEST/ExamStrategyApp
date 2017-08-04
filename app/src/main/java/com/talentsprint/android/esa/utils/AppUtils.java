package com.talentsprint.android.esa.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.talentsprint.android.esa.activities.CurrentAffairsActivity;
import com.talentsprint.android.esa.activities.DashboardActivity;
import com.talentsprint.android.esa.activities.LoginActivity;
import com.talentsprint.android.esa.models.NotificationsObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anudeep Reddy on 7/14/2017.
 */

public class AppUtils {

    public static String getCurrentDateString() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static long getLongFromYYYMMDD(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date d = format.parse(date);
        d.setHours(11);
        return d.getTime();
    }

    public static long getLongFromMMMMMYY(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("MMMMM yyyy");
        Date d = format.parse(date);
        return d.getTime();
    }

    public static long getLongFromDDMMYYY(String date) throws Exception {
        SimpleDateFormat format;
        if (date.contains("-"))
            format = new SimpleDateFormat("dd-MM-yyyy");
        else
            format = new SimpleDateFormat("dd/MM/yyyy");
        Date d = format.parse(date);
        return d.getTime();
    }

    public static String getDateInYYYMMDD(long timeInMillis) {
        Date date = new Date();
        date.setTime(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public static String getDateInMMMDDYYYY(long timeInMillis) {
        Date date = new Date();
        date.setTime(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormat.format(date);
    }

    public static String getDateInDDMMYYYY(long timeInMillis) {
        Date date = new Date();
        date.setTime(timeInMillis);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(date);
    }

    public static void closeKeyboard(View view, Context context) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-[+]]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static void navigateFromNotifications(Context context, NotificationsObject notificationsObject, boolean isFromHome) {
        if (notificationsObject.getCategory() != null && notificationsObject.getCategory().length() > 0) {
            if (notificationsObject.getCategory().equalsIgnoreCase(AppConstants.HOME)) {
                Intent navigate = new Intent(context, DashboardActivity.class);
                navigate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                navigate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (!isFromHome)
                    context.startActivity(navigate);
            } else if (notificationsObject.getCategory().equalsIgnoreCase(AppConstants.CURRENT_AFFAIRS1)) {
                Intent navigate = new Intent(context, CurrentAffairsActivity.class);
                navigate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(navigate);
            } else if (notificationsObject.getCategory().equalsIgnoreCase(AppConstants.LOGIN)) {
                if (!PreferenceManager.getBoolean(context, AppConstants.IS_LOGGEDIN, false)) {
                    Intent navigate = new Intent(context, LoginActivity.class);
                    navigate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(navigate);
                }
            } else if (notificationsObject.getCategory().equalsIgnoreCase(AppConstants.BROWSER)) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(notificationsObject.getLink()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application found to open link", Toast.LENGTH_SHORT).show();
                }

            }
        } else {
            Intent navigate = new Intent(context, DashboardActivity.class);
            navigate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            navigate.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (!isFromHome)
                context.startActivity(navigate);
        }
    }
}
