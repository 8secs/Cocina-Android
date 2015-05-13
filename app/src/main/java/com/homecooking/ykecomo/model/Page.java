package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Page {
    
    public int ID;
    
    @SerializedName("uRLSegment")
    public String url;
    
    @SerializedName("title")
    public String title;
    
    @SerializedName("menuTitle")
    public String menuTitle;
    
    @SerializedName("content")
    public String content;
    
    @SerializedName("sort")
    public String sort;
    
    @SerializedName("locale")
    public String locale;

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getSort() {
        return sort;
    }

    public String getLocale() {
        return locale;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMenuTitle(String menuTitle) {
        this.menuTitle = menuTitle;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
