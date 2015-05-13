package com.homecooking.ykecomo.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by andres on 24/2/15.
 */
@Parcel
public class Address {
    
    public int id;
    
    @SerializedName("city")
    public String city;

    @SerializedName("country")
    public String country;

    @SerializedName("latitude")
    public String latitude;

    @SerializedName("longitud")
    public String longitud;

    @SerializedName("address")
    protected String address;

    @SerializedName("postalCode")
    protected String postalCode;

    @SerializedName("state")
    protected String state;

    public int getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
