package com.homecooking.ykecomo.actions.location;

import android.location.Location;

import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;

import rx.functions.Action1;

/**
 * Created by andres on 15/03/15.
 */
public class SetLocationAction implements Action1<Location> {

    private String mType;

    public SetLocationAction(String type) {
        this.mType = type;
    }

    @Override
    public void call(Location location) {
        //Log.e("SelectLocation", this.mType);
        if(this.mType.equals(Constants.LAST_KNOWN_LOCATION))
            App.setUserLastLocation(location);
        else if(this.mType.equals(Constants.LOCATIONS_UPDATES))
            App.setUserUpdateLocation(location);
    }
}
