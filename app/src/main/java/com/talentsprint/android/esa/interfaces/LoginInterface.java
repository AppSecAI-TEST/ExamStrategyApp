package com.talentsprint.android.esa.interfaces;

import android.view.View;

/**
 * Created by Anudeep Reddy on 7/14/2017.
 */

public interface LoginInterface {
    public void showProgress(boolean isShow);

    public void loginGoogle(View googleLogin);

    public void loginFb(View fbLogin);
}
