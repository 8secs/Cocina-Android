package com.homecooking.ykecomo.ui.preferences;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

/**
 * Created by andres on 30/03/15.
 */
public class UserFullnamePreference extends Preference {

    private Context mContext;

    public UserFullnamePreference(Context context){
        super(context);
        this.mContext = context;
    }

    public UserFullnamePreference(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        mContext = context;
    }

    public UserFullnamePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
}
