package com.talentsprint.android.esa.models;

import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/6/2017.
 */

public class TestPropertiesObject {
    private String testName;
    private String testTime;
    private String testQuestions;
    private ArrayList<String> instructions;
    private TestProperties testproperties;

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(String testQuestions) {
        this.testQuestions = testQuestions;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> instructions) {
        this.instructions = instructions;
    }

    public TestProperties getTestproperties() {
        return testproperties;
    }

    public void setTestproperties(TestProperties testproperties) {
        this.testproperties = testproperties;
    }

    public class TestProperties {
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
