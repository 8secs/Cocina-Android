package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class StarRating {

    protected int id;

    @SerializedName("starRatingCategory")
    protected String starRatingCategory;

    @SerializedName("rating")
    protected int rating;

    @SerializedName("maxRating")
    protected int maxRating;

    @SerializedName("chefReview")
    protected int chefReview;

    @SerializedName("productReview")
    protected int productReview;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStarRatingCategory() {
        return starRatingCategory;
    }

    public void setStarRatingCategory(String starRatingCategory) {
        this.starRatingCategory = starRatingCategory;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getMaxRating() {
        return maxRating;
    }

    public void setMaxRating(int maxRating) {
        this.maxRating = maxRating;
    }

    public int getChefReview() {
        return chefReview;
    }

    public void setChefReview(int chefReview) {
        this.chefReview = chefReview;
    }

    public int getProductReview() {
        return productReview;
    }

    public void setProductReview(int productReview) {
        this.productReview = productReview;
    }
}
