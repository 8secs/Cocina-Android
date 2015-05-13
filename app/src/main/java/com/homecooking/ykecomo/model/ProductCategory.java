package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by andres on 20/2/15.
 */
@Parcel
public class ProductCategory {

    public int id;

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

    @SerializedName("pageImage")
    public String pageImage;

    public String getImageFilename() {
        return imageFilename;
    }

    public void setImageFilename(String imageFilename) {
        this.imageFilename = imageFilename;
    }

    public String imageFilename;

    public int getID() { return id; }

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

    public void setId(int id) {
        this.id = id;
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

    public String getPageImage() {
        return pageImage;
    }

    public void setPageImage(String pageImage) {
        this.pageImage = pageImage;
    }
}
