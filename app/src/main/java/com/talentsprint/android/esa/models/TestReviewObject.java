package com.talentsprint.android.esa.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/13/2017.
 */

public class TestReviewObject implements Serializable {

    public ArrayList<Question> questions;

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public class Question implements Serializable {
        public int userOption;
        public String question;
        public String explanation;
        public int correctOption;
        public String timeTaken;
        public long id;
        public ArrayList<String> options;

        public int getUserOption() {
            return userOption;
        }

        public void setUserOption(int userOption) {
            this.userOption = userOption;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public String getExplanation() {
            return explanation;
        }

        public void setExplanation(String explanation) {
            this.explanation = explanation;
        }

        public int getCorrectOption() {
            return correctOption;
        }

        public void setCorrectOption(int correctOption) {
            this.correctOption = correctOption;
        }

        public String getTimeTaken() {
            return timeTaken;
        }

        public void setTimeTaken(String timeTaken) {
            this.timeTaken = timeTaken;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public ArrayList<String> getOptions() {
            return options;
        }

        public void setOptions(ArrayList<String> options) {
            this.options = options;
        }
    }
}
