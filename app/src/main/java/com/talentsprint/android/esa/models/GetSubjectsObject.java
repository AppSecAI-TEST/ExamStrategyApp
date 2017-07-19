package com.talentsprint.android.esa.models;

import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/19/2017.
 */

public class GetSubjectsObject {

    private String examName;
    private ArrayList<String> subjects;

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public ArrayList<String> getSubjects() {
        return subjects;
    }

    public void setSubjects(ArrayList<String> subjects) {
        this.subjects = subjects;
    }
}
