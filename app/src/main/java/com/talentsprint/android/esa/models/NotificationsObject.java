package com.talentsprint.android.esa.models;

import io.realm.RealmObject;

/**
 * Created by Anudeep Reddy on 7/25/2017.
 */

public class NotificationsObject extends RealmObject {
    private String title;
    private String link;
    private String expiryDate;
    private String addedDate;
    private long expiryDateLong;
    private long addedDateLong;
    private String type;
    private String category;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public long getExpiryDateLong() {
        return expiryDateLong;
    }

    public void setExpiryDateLong(long expiryDateLong) {
        this.expiryDateLong = expiryDateLong;
    }

    public long getAddedDateLong() {
        return addedDateLong;
    }

    public void setAddedDateLong(long addedDateLong) {
        this.addedDateLong = addedDateLong;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(String addedDate) {
        this.addedDate = addedDate;
    }
}
