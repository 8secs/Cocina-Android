package com.homecooking.ykecomo.rest.model;

import com.google.gson.annotations.SerializedName;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Auth;
import com.homecooking.ykecomo.model.ChefReview;
import com.homecooking.ykecomo.model.Group;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Message;
import com.homecooking.ykecomo.model.MessageThread;
import com.homecooking.ykecomo.model.Order;
import com.homecooking.ykecomo.model.OrderItem;
import com.homecooking.ykecomo.model.Page;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.model.WishList;
import com.homecooking.ykecomo.model.WishListItem;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.Hashtable;

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

    @SerializedName("product")
    public Product product;

    @SerializedName("groups")
    public ArrayList<Group> groups;

    @SerializedName("chefReviews")
    public ArrayList<ChefReview> chefReviews;

    @SerializedName("wishLists")
    public ArrayList<WishList> wishLists;

    @SerializedName("wishListItems")
    public ArrayList<WishListItem> wishListItems;

    @SerializedName("messageThreads")
    public ArrayList<MessageThread> messageThreads;

    @SerializedName("messages")
    public ArrayList<Message> messages;

    @SerializedName("orders")
    public ArrayList<Order> orders;

    @SerializedName("orderItems")
    protected ArrayList<OrderItem> orderItems;


    
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

    public Product getProduct() { return product; }

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

    public ArrayList<WishList> getWishLists() { return wishLists; }

    public ArrayList<WishListItem> getWishListItems() { return wishListItems; }

    public Member getMember() { return member; }

    public String getMessage() { return message; }

    public ArrayList<MessageThread> getMessageThreads(){ return messageThreads; }

    public ArrayList<Message> getMessages() { return messages; }

    public ArrayList<Order> getOrders() { return orders; }

    public ArrayList<OrderItem> getOrderItems() { return orderItems; }
    
    public Auth getAuth () { return auth; }
    
    public void setAuth(Auth auth){ this.auth = auth; }



    public void setMessage(String message) { this.message = message; }

    public void setCreateMember(ArrayList<Hashtable<String, String>> createMember) { this.createMember = createMember; }

    public void setImage(Image image) { this.image = image; }

    public void setGroups(ArrayList<Group> groups) { this.groups = groups; }

    public void setChefReviews(ArrayList<ChefReview> chefReviews){ this.chefReviews = chefReviews; }

    public void setWishLists(ArrayList<WishList> wishLists) { this.wishLists = wishLists; }

    public void setWishListItems(ArrayList<WishListItem> items) { this.wishListItems = items; }

    public void setMessageThreads(ArrayList<MessageThread> threads) { this.messageThreads = threads; }

    public void setMessages(ArrayList<Message> messages) { this.messages = messages; }

    public void setOrders(ArrayList<Order> orders) { this.orders = orders; }

    public void setOrderItems(ArrayList<OrderItem> orderItems) { this.orderItems = orderItems; }
}
