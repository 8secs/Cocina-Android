package com.homecooking.ykecomo.operators.location;

import android.location.Location;

import rx.functions.Func1;

/**
 * Created by andres on 26/02/15.
 */
public class LocationToObjectFunc implements Func1<Location, Location> {
    @Override
    public Location call(Location location) {
        if (location != null){
            //Log.e("LocationToStringFunc", location.getLatitude() + " " + location.getLongitude() + " (" + location.getAccuracy() + ")");
            return location;
            //return location.getLatitude() + " " + location.getLongitude() + " (" + location.getAccuracy() + ")";
        }

        return null;
    }
}
