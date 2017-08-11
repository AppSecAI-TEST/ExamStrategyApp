package com.talentsprint.android.esa.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/5/2017.
 */

public class HomeObject implements Serializable {
    private String nextExam;
    private String nextExamDate;
    private String status;
    private ArrayList<CurrentAffairsListObject.CurrentAffairArticles> currentAffairs;
    private ArrayList<TaskObject> tasklist;

    public String getNextExam() {
        return nextExam;
    }

    public void setNextExam(String nextExam) {
        this.nextExam = nextExam;
    }

    public String getNextExamDate() {
        return nextExamDate;
    }

    public void setNextExamDate(String nextExamDate) {
        this.nextExamDate = nextExamDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ArrayList<TaskObject> getTasklist() {
        return tasklist;
    }

    public void setTasklist(ArrayList<TaskObject> tasklist) {
        this.tasklist = tasklist;
    }

    public ArrayList<CurrentAffairsListObject.CurrentAffairArticles> getCurrentAffairs() {
        return currentAffairs;
    }

    public void setCurrentAffairs(ArrayList<CurrentAffairsListObject.CurrentAffairArticles> currentAffairs) {
        this.currentAffairs = currentAffairs;
    }
}
