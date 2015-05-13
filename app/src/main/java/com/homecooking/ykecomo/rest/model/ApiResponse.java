package com.homecooking.ykecomo.rest.model;

import com.google.gson.annotations.SerializedName;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Auth;
import com.homecooking.ykecomo.model.ChefReview;
import com.homecooking.ykecomo.model.Group;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Page;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.model.ProductCategory;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by andres on 19/2/15.
 */
@Parcel
public class ApiResponse {

    @SerializedName("pages")
    public ArrayList<Page> pages;

    @SerializedName("productCategories")
    public ArrayList<ProductCategory> productCategories;

    @SerializedName("products")
    public ArrayList<Product> products;

    @SerializedName("members")
    public ArrayList<Member> members;

    @SerializedName("images")
    public ArrayList<Image> images;

    @SerializedName("addresses")
    public ArrayList<Address> addresses;

    @SerializedName("address")
    public Address address;

    @SerializedName("member")
    public Member member;

    @SerializedName("image")
    public Image image;

    @SerializedName("groups")
    public ArrayList<Group> groups;

    @SerializedName("chefReviews")
    public ArrayList<ChefReview> chefReviews;
    
    protected Auth auth;

    protected String message;

    protected ArrayList<Hashtable<String, String>> createMember;

    public ArrayList<Page> getPages() {
        return pages;
    }

    public ArrayList<ProductCategory> getProductCategories() {
        return productCategories;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public ArrayList<Member> getMembers() {
        return members;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public ArrayList<Address> getAddresses() { return addresses; }

    public Address getAddress(){ return address; }

    public Image getImage() { return image; }

    public ArrayList<Hashtable<String, String>> getCreateMember() { return createMember; }

    public ArrayList<Group> getGroups() { return groups; }

    public ArrayList<ChefReview> getChefReviews() { return chefReviews; }

    public Member getMember() { return member; }

    public String getMessage() { return message; }
    
    public Auth getAuth () { return auth; }
    
    public void setAuth(Auth auth){ this.auth = auth; }

    public void setMessage(String message) { this.message = message; }

    public void setCreateMember(ArrayList<Hashtable<String, String>> createMember) { this.createMember = createMember; }

    public void setImage(Image image) { this.image = image; }

    public void setGroups(ArrayList<Group> groups) { this.groups = groups; }

    public void setChefReviews(ArrayList<ChefReview> chefReviews){ this.chefReviews = chefReviews; }
}
