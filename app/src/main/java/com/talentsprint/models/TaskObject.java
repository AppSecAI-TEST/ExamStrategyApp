package com.talentsprint.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Anudeep Reddy on 7/5/2017.
 */

public class TaskObject {
    private String taskId;
    private String title;
    private String type;
    @SerializedName("content-url")
    private String contentUrl;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }
}
