package com.talentsprint.android.esa.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/19/2017.
 */

public class TopicsObject implements Serializable {

    private String exam;
    private String subject;
    private ArrayList<String> topics;

    public String getExam() {
        return exam;
    }

    public void setExam(String exam) {
        this.exam = exam;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public ArrayList<String> getTopics() {
        return topics;
    }

    public void setTopics(ArrayList<String> topics) {
        this.topics = topics;
    }
}
