package com.talentsprint.android.esa.models;

/**
 * Created by Anudeep Reddy on 7/6/2017.
 * "id":"e001",
 * "name": "Bank PO",
 * "date": "12/12/2017"
 */

public class ExamObject {
    private String id;
    private String name;
    private String date;
    private boolean isPreviouslyAdded;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isPreviouslyAdded() {
        return isPreviouslyAdded;
    }

    public void setPreviouslyAdded(boolean previouslyAdded) {
        isPreviouslyAdded = previouslyAdded;
    }
}
