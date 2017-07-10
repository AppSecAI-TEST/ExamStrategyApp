package com.talentsprint.android.esa.interfaces;

import com.talentsprint.android.esa.utils.TalentSprintApi;

/**
 * Created by Anudeep on 6/30/2017.
 */

public interface DashboardActivityInterface {
    public void setCurveVisibility(boolean isShow);

    public void showProgress(boolean isShow);

    public void examAdded();

    public TalentSprintApi getApiService();
}
