package com.talentsprint.android.esa.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Anudeep Reddy on 7/10/2017.
 */

public class StratergyObject implements Serializable {

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

    public class FilterOptions implements Serializable {
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

    public class MonthTasks {
        public String dateMonth;
        public String date;
        public ArrayList<Task> tasks;

        public String getDateMonth() {
            return dateMonth;
        }

        public void setDateMonth(String dateMonth) {
            this.dateMonth = dateMonth;
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

    public class Stratergy {
        private ArrayList<MonthTasks> monthTasks;
        private String month;
        private long monthLong;

        public long getMonthLong() {
            return monthLong;
        }

        public void setMonthLong(long monthLong) {
            this.monthLong = monthLong;
        }

        public ArrayList<MonthTasks> getMonthTasks() {
            return monthTasks;
        }

        public void setMonthTasks(ArrayList<MonthTasks> monthTasks) {
            this.monthTasks = monthTasks;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }

    public class Task {
        public String status;
        public String taskId;
        public String title;
        public long duration;
        public String type;
        public String articleId;
        public String date;
        public String day;
        public String dayName;
        public long dateLong;
        public boolean isShowDate;
        public String contentType;
        public String subject;
        public boolean isPremium;
        public String contentUrl;
        public ArticleInfo articleInfo;

        public ArticleInfo getArticleInfo() {
            return articleInfo;
        }

        public String getContentUrl() {
            return contentUrl;
        }

        public void setContentUrl(String contentUrl) {
            this.contentUrl = contentUrl;
        }

        public void setArticleInfo(ArticleInfo articleInfo) {
            this.articleInfo = articleInfo;
        }

        public boolean isPremium() {
            return isPremium;
        }

        public void setPremium(boolean premium) {
            isPremium = premium;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public long getDateLong() {
            return dateLong;
        }

        public void setDateLong(long dateLong) {
            this.dateLong = dateLong;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getDayName() {
            return dayName;
        }

        public void setDayName(String dayName) {
            this.dayName = dayName;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public boolean isShowDate() {
            return isShowDate;
        }

        public void setShowDate(boolean showDate) {
            isShowDate = showDate;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

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

        public long getDuration() {
            return duration;
        }

        public void setDuration(long duration) {
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
