package com.talentsprint.android.esa.models;

import java.io.Serializable;

/**
 * Created by Anudeep Reddy on 7/13/2017.
 */

public class TestResultsObject implements Serializable {
    private TestProperties testproperties;
    private String score;
    private String performance;
    private int total;
    private int correct;
    private int wrong;
    private String timeTaken;
    private String extraTime;
    private String speed;

    public TestProperties getTestproperties() {
        return testproperties;
    }

    public void setTestproperties(TestProperties testproperties) {
        this.testproperties = testproperties;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public String getExtraTime() {
        return extraTime;
    }

    public void setExtraTime(String extraTime) {
        this.extraTime = extraTime;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public class TestProperties implements Serializable {
        public String subject;
        public String topic;
        public String subTopic;

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

        public String getSubTopic() {
            return subTopic;
        }

        public void setSubTopic(String subTopic) {
            this.subTopic = subTopic;
        }
    }
}
