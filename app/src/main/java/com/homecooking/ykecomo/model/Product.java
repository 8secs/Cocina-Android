package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Product {

    protected int id;

    @SerializedName("internalItemID")
    protected String internalItemID;

    @SerializedName("model")
    protected String model;

    @SerializedName("basePrice")
    protected String basePrice;

    @SerializedName("allowPurchase")
    protected String allowPurchase;

    @SerializedName("portions")
    protected String portions;

    @SerializedName("city")
    protected String city;

    @SerializedName("miniDescription")
    protected String miniDescription;

    @SerializedName("title")
    protected String title;

    @SerializedName("content")
    protected String content;

    @SerializedName("image")
    protected String image;

    @SerializedName("chef")
    protected String chef;

    @SerializedName("parent")
    protected String parentID;

    protected String mImgUrl;

    protected String mChefName;

    protected Member mMember;

    public Product() { }

    public String getImgUrl() {
        return mImgUrl;
    }

    public void setImgUrl(String mImgUrl) {
        this.mImgUrl = mImgUrl;
    }

    public String getChefName() {
        return mChefName;
    }

    public void setChefName(String mChefName) {
        this.mChefName = mChefName;
    }
    
    public Member getMember() { return mMember; }
    
    public void setMember(Member member){ this.mMember = member; }

    public int getID() {
        return id;
    }

    public String getInternalItemID() {
        return internalItemID;
    }

    public String getModel() {
        return model;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public String getAllowPurchase() {
        return allowPurchase;
    }

    public String getPortions() {
        return portions;
    }

    public String getCity() {
        return city;
    }

    public String getMiniDescription() {
        return miniDescription;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getImage() {
        return image;
    }

    public String getChef() {
        return chef;
    }

    public String getParentID() {
        return parentID;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setInternalItemID(String internalItemID) {
        this.internalItemID = internalItemID;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public void setAllowPurchase(String allowPurchase) {
        this.allowPurchase = allowPurchase;
    }

    public void setPortions(String portions) {
        this.portions = portions;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setMiniDescription(String miniDescription) {
        this.miniDescription = miniDescription;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public void setmChefName(String mChefName) {
        this.mChefName = mChefName;
    }

    public void setmMember(Member mMember) {
        this.mMember = mMember;
    }
}
