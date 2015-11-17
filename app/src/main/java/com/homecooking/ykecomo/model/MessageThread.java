package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by: andres
 * User: andres
 * Date: 7/07/15
 * Time: 10 : 06
 */
@Parcel
public class MessageThread {

    public int id;

    @SerializedName("title")
    public String title;

    @SerializedName("isReadOnly")
    public String isReadOnly;

    @SerializedName("order")
    public String order;

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

    public String getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(String isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }
}
