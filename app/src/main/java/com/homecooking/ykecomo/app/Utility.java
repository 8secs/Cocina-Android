package com.homecooking.ykecomo.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.facebook.Session;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;

import java.util.Locale;
import java.util.Map;

/**
 * Created by: andres
 * User: andres
 * Date: 3/06/15
 * Time: 20 : 22
 */
public class Utility {

    static public boolean isNetworkAvailable(Context c){
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static void setMemberPrefs(){
        SharedPreferences preferences = App.getPref();
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(Constants.IS_MEMBER, true);
        editor.putInt(Constants.MEMBER_ID, App.getMember().getId());
        editor.putString(Constants.MEMBER_NAME, App.getMember().getFirstName());
        editor.putString(Constants.MEMBER_SURNAME, App.getMember().getSurname());
        editor.putString(Constants.MEMBER_ADDRESS, App.getMember().getAddressStr());
        editor.putString(Constants.MEMBER_EMAIL, App.getMember().getEmail());
        editor.putString(Constants.MEMBER_DESCRIPTION, App.getMember().getDescription());
        if(App.getMember().getAvatarFilename() != null) editor.putString(Constants.MEMBER_IMAGE, App.getMember().getAvatarFilename());

        if(App.getMember().getFacebookUID() == null){
            App.setIsFbMember(false);
            editor.putBoolean(Constants.IS_FB_MEMBER, false);
            editor.putInt(Constants.TYPE_USER, Constants.LOGIN_USER_TYPE);
            editor.putString(Constants.MEMBER_VERIFIED, App.getMember().getVerified());
        }else{
            App.setIsFbMember(true);
            App.setCurrentFbSession(Session.getActiveSession());
            editor.putBoolean(Constants.IS_FB_MEMBER, true);
            editor.putInt(Constants.TYPE_USER, Constants.FB_USER_TYPE);
            editor.putString(Constants.MEMBER_FB_ID, App.getMember().getFacebookUID());
            editor.putString(Constants.MEMBER_VERIFIED, "1");
        }

        if(App.getMember().getAddress() != null){
            editor.putBoolean(Constants.MEMBER_HAS_ADDRESS, true);
            editor.putString(Constants.MEMBER_DIRECCION, App.getMember().getAddress().getAddress());
            editor.putString(Constants.MEMBER_CITY, App.getMember().getAddress().getCity());
            editor.putString(Constants.MEMBER_STATE, App.getMember().getAddress().getState());
            editor.putString(Constants.MEMBER_COUNTRY, App.getMember().getAddress().getCountry());
            editor.putString(Constants.MEMBER_POSTAL_CODE, App.getMember().getAddress().getPostalCode());
        }else{
            editor.putBoolean(Constants.MEMBER_HAS_ADDRESS, false);
            if(App.getMember().getAddressStr() != null){

                String[] strs = App.getMember().getAddressStr().split(",");
                if(strs.length > 1){
                    editor.putString(Constants.MEMBER_CITY, strs[0].trim());
                    String code = Utility.getCountryCode(strs[1].trim(), "en");
                    editor.putString(Constants.MEMBER_COUNTRY, code);
                }
            }
        }
        editor.commit();
    }

    public static void setMemberFromPrefs(){
        if(App.getPref().getBoolean(Constants.IS_MEMBER, true)){
            Member member = new Member();
            member.setId(App.getPref().getInt(Constants.MEMBER_ID, 0));
            member.setFirstName(App.getPref().getString(Constants.MEMBER_NAME, ""));
            member.setSurname(App.getPref().getString(Constants.MEMBER_SURNAME, ""));
            member.setEmail(App.getPref().getString(Constants.MEMBER_EMAIL, ""));
            member.setDescription(App.getPref().getString(Constants.MEMBER_DESCRIPTION, ""));
            member.setVerified(App.getPref().getString(Constants.MEMBER_VERIFIED, ""));
            if(App.getPref().getInt(Constants.TYPE_USER, Constants.LOGIN_USER_TYPE) == Constants.LOGIN_USER_TYPE){
                App.setIsFbMember(App.getPref().getBoolean(Constants.IS_FB_MEMBER, false));
                member.setAvatarFilename(App.getPref().getString(Constants.MEMBER_IMAGE, ""));
            }else if(App.getPref().getInt(Constants.TYPE_USER, Constants.FB_USER_TYPE) == Constants.FB_USER_TYPE){
                App.setIsFbMember(App.getPref().getBoolean(Constants.IS_FB_MEMBER, true));
                member.setAvatarFilename(Utility.getUserpicFbURL());
                member.setAddressStr(App.getPref().getString(Constants.MEMBER_ADDRESS, ""));
                member.setFacebookUID(App.getPref().getString(Constants.MEMBER_FB_ID, ""));
            }

            if(App.getPref().getBoolean(Constants.MEMBER_HAS_ADDRESS, false)){
                Address address = new Address();
                address.setAddress(App.getPref().getString(Constants.MEMBER_DIRECCION, ""));
                address.setCity(App.getPref().getString(Constants.MEMBER_CITY, ""));
                address.setCountry(App.getPref().getString(Constants.MEMBER_COUNTRY, ""));
                address.setState(App.getPref().getString(Constants.MEMBER_STATE, ""));
                address.setPostalCode(App.getPref().getString(Constants.MEMBER_POSTAL_CODE, ""));
                member.setAddress(address);
            }

            App.setMember(member);
        }
    }

    public static void setPrefUserEnvironment(int environment){
        SharedPreferences preferences = App.getPref();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Constants.USER_ENVIRONMENT, environment);
        editor.commit();
    }

    public static int getUserEnvironmentFromPref(){
        return App.getPref().getInt(Constants.USER_ENVIRONMENT, 0);
    }

    public static String getMemberAddressStr(Member member){
        return member.getAddress().getAddress() + ". "
                + member.getAddress().getCity() + ", "
                + member.getAddress().getState() + " - "
                + member.getAddress().getCountry();
    }

    public static String getUserpicFbURL(){
        return Constants.GRAPH_FB_URL + App.getPref().getString(Constants.MEMBER_FB_ID, "") + Constants.PICTURE_FB_URL_PARAMS;
    }

    public static Map<String,?> getAllPrefs(){
        Map<String,?> keys = App.getPref().getAll();

        for(Map.Entry<String,?> entry : keys.entrySet()){
            Log.e("map values", entry.getKey() + ": " +
                    entry.getValue().toString());
        }
        return keys;
    }

    public static String getCountryCode(String countryName, String language){
        String[] locales = Locale.getISOCountries();
        String code = Constants.NON_COUNTRY_CODE;
        for (String countryCode : locales) {
            Locale obj = new Locale(language, countryCode);
            if(obj.getDisplayCountry(obj).equals(countryName)) code = obj.getCountry();
        }
        return code;
    }

    public static String getCountryName(String code, String language){
        String[] locales = Locale.getISOCountries();
        String name = Constants.NON_COUNTRY_NAME;
        for (String countryCode : locales) {
            Locale obj = new Locale(language, countryCode);
            if(obj.getCountry().equals(code)) name = obj.getDisplayCountry(obj);
        }
        return name;
    }

    public static void updateProductChef(Product product){
        for(int i = 0; i < App.getProductsChef().size(); i++){
            if(App.getProductsChef().get(i).getID() == product.getID()){
                App.getProductsChef().set(i, product);
            }
        }
    }


    public static Bitmap decodeBitmapFromByteArray(byte[] data, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        //bitmap = App.rotateBitmap(bitmap);
        return bitmap;
    }


    public static Bitmap decodeBitmapFromFile(String file, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        Bitmap bitmap = BitmapFactory.decodeFile(file, options);
        //bitmap = App.rotateBitmap(bitmap);
        return bitmap;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
