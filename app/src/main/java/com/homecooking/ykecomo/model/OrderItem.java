package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by: andres
 * User: andres
 * Date: 9/07/15
 * Time: 09 : 07
 */
@Parcel
public class OrderItem {

    protected int id;

    @SerializedName("discount")
    protected String discount;

    @SerializedName("quantity")
    protected String quantity;

    @SerializedName("unitPrice")
    protected String unitPrice;

    @SerializedName("calculatedTotal")
    protected String calculatedTotal;

    @SerializedName("product")
    protected int product;

    @SerializedName("order")
    protected int order;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getCalculatedTotal() {
        return calculatedTotal;
    }

    public void setCalculatedTotal(String calculatedTotal) {
        this.calculatedTotal = calculatedTotal;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }




}
