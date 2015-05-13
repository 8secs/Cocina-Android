package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class Image {

    public int id;

    @SerializedName("name")
    public String name;

    @SerializedName("title")
    public String title;

    @SerializedName("filename")
    public String filename;

    @SerializedName("content")
    public String content;

    @SerializedName("parent")
    public String parent;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public String getParent() {
        return parent;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }
}
