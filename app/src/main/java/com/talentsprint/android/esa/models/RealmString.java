package com.talentsprint.android.esa.models;

import io.realm.RealmObject;

/**
 * Created by Anudeep Reddy on 7/31/2017.
 */

public class RealmString extends RealmObject{
    private String string;

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}
