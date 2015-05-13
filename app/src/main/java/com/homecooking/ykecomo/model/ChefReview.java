package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ChefReview {

    protected int id;

    @SerializedName("title")
    protected String title;

    @SerializedName("comments")
    protected String comments;

    @SerializedName("approved")
    protected String approved;

    @SerializedName("chef")
    protected String chef;

    @SerializedName("member")
    protected String member;

    @SerializedName("starRatings")
    protected int[] starRatings;

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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public int[] getStarRatings() {
        return starRatings;
    }

    public void setStarRatings(int[] starRatings) {
        this.starRatings = starRatings;
    }
}
