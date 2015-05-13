package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;


public class Group {

    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("code")
    public String code;

    @SerializedName("locked")
    public String locked;

    @SerializedName("linkedPage")
    public String linkedPage;

    @SerializedName("members")
    public int[] members;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLocked() {
        return locked;
    }

    public void setLocked(String locked) {
        this.locked = locked;
    }

    public String getLinkedPage() {
        return linkedPage;
    }

    public void setLinkedPage(String linkedPage) {
        this.linkedPage = linkedPage;
    }

    public int[] getMembers() {
        return members;
    }

    public void setMembers(int[] members) {
        this.members = members;
    }
}
