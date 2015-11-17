package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by: andres
 * User: andres
 * Date: 8/07/15
 * Time: 20 : 09
 */
@Parcel
public class Order {
    protected int id;

    @SerializedName("total")
    protected String total;

    @SerializedName("reference")
    protected String reference;

    @SerializedName("placed")
    protected String placed;

    @SerializedName("member")
    protected String member;

    @SerializedName("paid")
    protected String paid;

    protected ArrayList<OrderItem> items;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getPlaced() {
        return placed;
    }

    public void setPlaced(String placed) {
        this.placed = placed;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public ArrayList<OrderItem> getItems(){ return items; }

    public void setItems(ArrayList<OrderItem> items) { this.items = items; }
}
