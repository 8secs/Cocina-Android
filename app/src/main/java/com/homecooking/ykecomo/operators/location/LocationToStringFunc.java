package com.homecooking.ykecomo.operators.location;

import android.location.Location;

import rx.functions.Func1;


public class LocationToStringFunc implements Func1<Location, String> {
    @Override
    public String call(Location location) {
        if (location != null){
            //Log.e("LocationToStringFunc", location.getLatitude() + " " + location.getLongitude() + " (" + location.getAccuracy() + ")");
            return location.getLatitude() + " " + location.getLongitude() + " (" + location.getAccuracy() + ")";
        }
            
        return "no location available";
    }
}
