package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by andres on 21/02/15.
 */
@Parcel
public class Member {

    public int id;

    @SerializedName("firstName")
    protected String firstName;

    @SerializedName("surname")
    protected String surname;

    @SerializedName("email")
    protected String email;

    @SerializedName("avatar")
    protected String avatar;

    @SerializedName("defaultShippingAddress")
    protected String defaultShippingAddress;

    @SerializedName("description")
    protected String description;

    @SerializedName("verified")
    protected String verified;

    @SerializedName("chefType")
    protected int chefType;

    @SerializedName("products")
    protected int[] products;

    protected String addressStr;

    protected ArrayList<Product> productsArray;

    protected ArrayList<ChefReview> chefReviews;

    public Address address;

    public String facebookUID;

    public String avatarFilename;

    public int getId() { return id; }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() { return avatar; }

    public Address getAddress() { return address; }

    public int getChefType() { return  chefType; }

    public int[] getProducts() { return products; }

    public ArrayList<Product> getProductsArray() { return productsArray; }

    public ArrayList<ChefReview> getChefReviews() { return chefReviews; }

    public void setAddress(Address address){
        this.address = address;
    }

    public String getAvatarFilename() { return avatarFilename; }

    public void setAvatarFilename(String filename){
        this.avatarFilename = filename;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFacebookUID() {
        return facebookUID;
    }

    public void setFacebookUID(String facebookUID) {
        this.facebookUID = facebookUID;
    }

    public String getDefaultShippingAddress() { return defaultShippingAddress; }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public String getAddressStr() { return addressStr; }

    public void setAddressStr(String address) { this.addressStr = address; }

    public void setChefType(int chefType) { this.chefType = chefType; }

    public void setProducts(int[] products) { this.products = products; }

    public void setProductsArray(ArrayList<Product> products) { this.productsArray = products; }

    public void setChefReviews(ArrayList<ChefReview> reviews) { this.chefReviews = reviews; }
}
