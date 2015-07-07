package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by: andres
 * User: andres
 * Date: 7/07/15
 * Time: 10 : 23
 */
@Parcel
public class Message {

    protected int id;

    @SerializedName("content")
    protected String content;

    @SerializedName("status")
    protected String status;

    @SerializedName("author")
    protected String author;

    @SerializedName("thread")
    protected String thread;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }
}
