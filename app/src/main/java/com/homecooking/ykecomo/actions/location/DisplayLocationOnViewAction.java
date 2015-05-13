package com.homecooking.ykecomo.actions.location;

import android.location.Location;

import rx.functions.Action1;


public class DisplayLocationOnViewAction implements Action1<Location> {

    private Location target;

    public DisplayLocationOnViewAction(Location location){
        this.target = location;
    }

    @Override
    public void call(Location l){
        //App.setLastKnownLocation(l);
    }

}
