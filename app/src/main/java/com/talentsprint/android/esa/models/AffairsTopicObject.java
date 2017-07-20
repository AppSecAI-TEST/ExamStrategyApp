package com.talentsprint.android.esa.models;

import java.io.Serializable;

/**
 * Created by Anudeep Reddy on 7/10/2017.
 */

public class AffairsTopicObject implements Serializable {
    private String topicName;
    private int topicImage;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public int getTopicImage() {
        return topicImage;
    }

    public void setTopicImage(int topicImage) {
        this.topicImage = topicImage;
    }
}
