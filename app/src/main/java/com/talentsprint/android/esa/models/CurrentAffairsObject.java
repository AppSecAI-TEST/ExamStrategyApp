package com.talentsprint.android.esa.models;

import com.talentsprint.android.esa.fragments.StrategyContentDisplayFragment;

import java.io.Serializable;

/**
 * Created by Anudeep Reddy on 7/5/2017.
 */

public class CurrentAffairsObject implements Serializable {
    private String shortDescription;
    private String contentUrl;
    private String description;
    private String id;
    private String title;
    private String imageUrl;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
