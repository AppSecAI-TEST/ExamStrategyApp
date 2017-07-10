package com.talentsprint.android.esa.models;

import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/10/2017.
 */

public class StratergyObject {

    private FilterOptions filterOptions;
    private ArrayList<Stratergy> strategy;

    public FilterOptions getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(FilterOptions filterOptions) {
        this.filterOptions = filterOptions;
    }

    public ArrayList<Stratergy> getStrategy() {
        return strategy;
    }

    public void setStrategy(ArrayList<Stratergy> strategy) {
        this.strategy = strategy;
    }

    public class FilterOptions {
        public ArrayList<String> subjects;
        public ArrayList<String> contentType;

        public ArrayList<String> getSubjects() {
            return subjects;
        }

        public void setSubjects(ArrayList<String> subjects) {
            this.subjects = subjects;
        }

        public ArrayList<String> getContentType() {
            return contentType;
        }

        public void setContentType(ArrayList<String> contentType) {
            this.contentType = contentType;
        }
    }

    public class Stratergy {
        public String month;
        public String date;
        public ArrayList<Task> tasks;

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<Task> getTasks() {
            return tasks;
        }

        public void setTasks(ArrayList<Task> tasks) {
            this.tasks = tasks;
        }
    }

    public class Task {
        public String status;
        public String taskId;
        public String title;
        public String contentUrl;
        public String duration;
        public String type;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

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

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
