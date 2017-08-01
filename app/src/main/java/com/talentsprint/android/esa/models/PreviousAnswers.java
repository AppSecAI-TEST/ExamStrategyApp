package com.talentsprint.android.esa.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Anudeep Reddy on 7/31/2017.
 */

public class PreviousAnswers extends RealmObject {
    @PrimaryKey
    private String id;
    private RealmList<RealmString> answers;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RealmList<RealmString> getAnswers() {
        return answers;
    }

    public void setAnswers(RealmList<RealmString> answers) {
        this.answers = answers;
    }

}
