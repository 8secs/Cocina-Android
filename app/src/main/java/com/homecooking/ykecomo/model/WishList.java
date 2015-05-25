package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by: andres
 * User: andres
 * Date: 15/05/15
 * Time: 08 : 23
 */
@Parcel
public class WishList {

    protected int id;

    @SerializedName("title")
    protected String title;

    @SerializedName("owner")
    protected int owner;

    @SerializedName("items")
    protected int[] items;

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

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public int[] getItems() {
        return items;
    }

    public void setItems(int[] items) {
        this.items = items;
    }
}
