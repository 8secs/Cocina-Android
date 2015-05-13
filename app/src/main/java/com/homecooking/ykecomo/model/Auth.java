package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by andres on 4/03/15.
 */
@Parcel
public class Auth {

    @SerializedName("result")
    public Boolean result;

    @SerializedName("message")
    public String message;

    @SerializedName("code")
    public int code;

    @SerializedName("token")
    public String token;

    @SerializedName("expire")
    public long expire;

    @SerializedName("userID")
    public int userID;

    public Boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public String getToken() {
        return token;
    }

    public long getExpire() {
        return expire;
    }

    public int getUserID() {
        return userID;
    }
}
