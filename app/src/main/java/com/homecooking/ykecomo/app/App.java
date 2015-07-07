package com.homecooking.ykecomo.app;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageView;

import com.facebook.Session;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.rest.RestClient;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class App extends Application {

    private static RestClient restClient;
    private static SharedPreferences mPref;
    private static Member mMember;
    private static Session mCurrentFbSession;
    private static boolean mIsFbMember;
    private static String mFbLocation;
    private static Context mContext;
    private static Location mUserLastLocation;
    private static Location mUserUpdateLocation;
    private static String mUserAddress;
    private static ArrayList<Product> mProductsChef;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
        mPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        restClient = new RestClient();

        DrawerImageLoader.init(new DrawerImageLoader.IDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                Picasso.with(imageView.getContext()).load(uri).placeholder(placeholder).into(imageView);
            }

            @Override
            public void cancel(ImageView imageView) {
                Picasso.with(imageView.getContext()).cancelRequest(imageView);
            }

            @Override
            public Drawable placeholder(Context ctx) {
                return null;
            }
        });

    }

    /**
     *
     * @return boolean
     */
    public static boolean isReady(){return checkIfIsFirstRun();}

    protected static boolean checkIfIsFirstRun(){
        if(mPref.contains(Constants.IS_FIRST_TIME_STR)) return true;
        else return false;
    }

    public static RestClient getRestClient() { return restClient; }

    public  static SharedPreferences getPref() { return mPref; }

    public static Member getMember() { return mMember; }

    public static void setMember(Member member) { mMember = member; }

    public static Location getUserLastLocation() { return mUserLastLocation; }

    public static Location getUserUpdateLocation() { return mUserUpdateLocation; }

    public static String getUserAddress(){ return mUserAddress; }

    public static Session getCurrentFbSession() {
        return mCurrentFbSession;
    }

    public static void setCurrentFbSession(Session mCurrentFbSession) { App.mCurrentFbSession = mCurrentFbSession; }

    public static Context getContext() { return mContext; }

    public static void setUserLastLocation(Location mUserLastLocation) { App.mUserLastLocation = mUserLastLocation; }

    public static void setUserUpdateLocation(Location mUserUpdateLocation) { App.mUserUpdateLocation = mUserUpdateLocation; }

    public static void setUserAddress(String mUserAddress) { App.mUserAddress = mUserAddress; }

    public static boolean isIsFbMember() { return mIsFbMember; }

    public static void setIsFbMember(boolean mIsFbMember) { App.mIsFbMember = mIsFbMember; }

    public static String getFbLocation() {
        return mFbLocation;
    }

    public static void setFbLocation(String mFbLocation) {
        App.mFbLocation = mFbLocation;
    }

    public static ArrayList<Product> getProductsChef(){ return mProductsChef; }

    public static void setProductsChef(ArrayList<Product> products) { App.mProductsChef = products; }

}
