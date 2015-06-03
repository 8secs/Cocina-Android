package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by: andres
 * User: andres
 * Date: 15/05/15
 * Time: 08 : 23
 */
public class WishListItem {

    protected int id;

    @SerializedName("buyableID")
    protected String buyableID;

    @SerializedName("buyableClassName")
    protected String buyableClassName;

    @SerializedName("wishList")
    protected int wishList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuyableID() {
        return buyableID;
    }

    public void setBuyableID(String buyableID) {
        this.buyableID = buyableID;
    }

    public String getBuyableClassName() {
        return buyableClassName;
    }

    public void setBuyableClassName(String buyableClassName) {
        this.buyableClassName = buyableClassName;
    }

    public int getWishList() {
        return wishList;
    }

    public void setWishList(int wishList) {
        this.wishList = wishList;
    }
}
