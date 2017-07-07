package com.talentsprint.android.esa.models;

/**
 * Created by Anudeep Reddy on 7/7/2017.
 */

public class CalenderObject {
    private int day;
    private long timeMillis;
    private boolean isThisMonth;

    public boolean isThisMonth() {
        return isThisMonth;
    }

    public void setThisMonth(boolean thisMonth) {
        isThisMonth = thisMonth;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public long getTimeMillis() {
        return timeMillis;
    }

    public void setTimeMillis(long timeMillis) {
        this.timeMillis = timeMillis;
    }

}
