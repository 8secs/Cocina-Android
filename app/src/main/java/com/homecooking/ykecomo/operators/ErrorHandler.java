package com.homecooking.ykecomo.operators;

import android.widget.Toast;

import com.homecooking.ykecomo.app.App;

import rx.functions.Action1;

/**
 * Created by andres on 14/03/15.
 */
public class ErrorHandler implements Action1<Throwable> {

    @Override
    public void call(Throwable throwable) {
        Toast.makeText(App.getContext(), "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }
}
