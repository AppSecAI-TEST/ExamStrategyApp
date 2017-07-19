package com.talentsprint.android.esa.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/19/2017.
 */

public class SubTopicsObject implements Serializable {

    private String exam;
    private String subject;
    private String topic;
    private ArrayList<String> subTopics;

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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public ArrayList<String> getSubTopics() {
        return subTopics;
    }

    public void setSubTopics(ArrayList<String> subTopics) {
        this.subTopics = subTopics;
    }
}
