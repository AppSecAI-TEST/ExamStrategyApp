package com.talentsprint.android.esa.interfaces;

import com.talentsprint.android.esa.utils.TalentSprintApi;

/**
 * Created by Anudeep on 6/30/2017.
 */

public interface DashboardActivityInterface {
    public void setCurveVisibility(boolean isShow);

    public void showProgress(boolean isShow);

    public void examAdded();

    public void setStatus(String status);

    public TalentSprintApi getApiService();

    public void setExamDate(String examDate);

    public void isStratergyReady(boolean isStratergyReady);
}
