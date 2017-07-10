package com.talentsprint.android.esa.models;

/**
 * Created by Anudeep Reddy on 7/10/2017.
 */

public class ProfileObject {
    private String profilePic;
    private String planName;
    private String name;
    private String activeSince;

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActiveSince() {
        return activeSince;
    }

    public void setActiveSince(String activeSince) {
        this.activeSince = activeSince;
    }
}
