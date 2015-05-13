package com.homecooking.ykecomo.actions.location;


import com.homecooking.ykecomo.app.App;

import rx.functions.Action1;

/**
 * Created by andres on 26/2/15.
 */
public class DisplayTextOnViewAction implements Action1<String> {

    @Override
    public void call(String s) {
        App.setUserAddress(s);
    }
}
