package com.talentsprint.android.esa.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/12/2017.
 */

public class QuestionsObject implements Serializable {
    private int noOfQuestion;
    private long testTime;
    private ArrayList<Question> questions;

    public int getNoOfQuestion() {
        return noOfQuestion;
    }

    public void setNoOfQuestion(int noOfQuestion) {
        this.noOfQuestion = noOfQuestion;
    }

    public long getTestTime() {
        return testTime;
    }

    public void setTestTime(long testTime) {
        this.testTime = testTime;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public class Question implements Serializable {
        public int id;
        public String question;
        public ArrayList<String> options;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public ArrayList<String> getOptions() {
            return options;
        }

        public void setOptions(ArrayList<String> options) {
            this.options = options;
        }
    }
}
