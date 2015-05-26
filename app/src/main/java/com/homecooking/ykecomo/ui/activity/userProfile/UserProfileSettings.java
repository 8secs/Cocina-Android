package com.homecooking.ykecomo.ui.activity.userProfile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.preferences.ImagePreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

public class UserProfileSettings extends BaseUserProfileActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {

        ImagePreference imagePref;
        Preference namePref;
        Preference addressPref;
        Preference emailPref;
        Preference descPref;

        Map<String, ?> mKeys;
        UserProfileSettings mActivity;

        Address mAddress;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.preferences);

            imagePref = (ImagePreference)getPreferenceScreen().findPreference(getActivity().getResources().getString(R.string.MEMBER_AVATAR));
            namePref = getPreferenceScreen().findPreference(getActivity().getResources().getString(R.string.MEMBER_NAME_KEY));
            addressPref = getPreferenceScreen().findPreference(getActivity().getResources().getString(R.string.ADDRESS_KEY));
            emailPref = getPreferenceScreen().findPreference(getActivity().getResources().getString(R.string.EMAIL_KEY));
            descPref = getPreferenceScreen().findPreference(getActivity().getResources().getString(R.string.DESCRIPTION_KEY));

            updateUI();

            imagePref.setActivity(mActivity);
            String imageURL = "";
            if(mKeys.get(Constants.MEMBER_IMAGE) != null){
                imageURL = mKeys.get(Constants.MEMBER_IMAGE).toString();
                imageURL = Constants.BASE_URL.concat(imageURL);
            }else{
                imageURL = App.getUserpicFbURL();
            }
            imagePref.setUrl(imageURL);

            imagePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    mActivity.selectImage();
                    return false;
                }
            });


            namePref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent("com.visitaelaljarafe.homecooking.ui.activity.memberPreferenceActivities.EditFullnameMember");
                    i.putExtra(Constants.MEMBER_NAME, App.getPref().getString(Constants.MEMBER_NAME, ""));
                    i.putExtra(Constants.MEMBER_SURNAME, App.getPref().getString(Constants.MEMBER_SURNAME, ""));
                    startActivityForResult(i, Constants.EDIT_PROFILE_REQUEST_CODE);
                    return false;
                }
            });


            addressPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent("com.visitaelaljarafe.homecooking.ui.activity.memberPreferenceActivities.AddressMember");
                    startActivityForResult(i, Constants.EDIT_PROFILE_REQUEST_CODE);
                    return false;
                }
            });


            emailPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent("com.visitaelaljarafe.homecooking.ui.activity.memberPreferenceActivities.EmailMember");
                    i.putExtra(Constants.MEMBER_EMAIL, App.getPref().getString(Constants.MEMBER_EMAIL, ""));
                    startActivityForResult(i, Constants.EDIT_PROFILE_REQUEST_CODE);
                    return false;
                }
            });


            descPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent i = new Intent("com.visitaelaljarafe.homecooking.ui.activity.memberPreferenceActivities.DescriptionMember");
                    i.putExtra(Constants.MEMBER_DESCRIPTION, App.getPref().getString(Constants.MEMBER_DESCRIPTION, ""));
                    startActivityForResult(i, Constants.EDIT_PROFILE_REQUEST_CODE);
                    return false;
                }
            });
        }

        private void updateUI(){
            mKeys = App.getPref().getAll();
            namePref.setSummary(mKeys.get(Constants.MEMBER_NAME) + " " + mKeys.get(Constants.MEMBER_SURNAME));

            if(mKeys.get(Constants.MEMBER_ADDRESS) != null) addressPref.setSummary(mKeys.get(Constants.MEMBER_ADDRESS).toString());
            else {
                if(mKeys.get(Constants.MEMBER_CITY) != null && mKeys.get(Constants.MEMBER_COUNTRY) != null)
                    addressPref.setSummary(mKeys.get(Constants.MEMBER_CITY).toString() + ", " + mKeys.get(Constants.MEMBER_COUNTRY).toString());
                else addressPref.setSummary(getActivity().getResources().getString(R.string.tu_address));
            }
            emailPref.setSummary(mKeys.get(Constants.MEMBER_EMAIL).toString());
            if(mKeys.get(Constants.MEMBER_DESCRIPTION) != null) descPref.setSummary(Html.fromHtml(mKeys.get(Constants.MEMBER_DESCRIPTION).toString()));
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            boolean addressChanged = false;
            Map<String, Object> map = new Hashtable<String, Object>();

            if(resultCode == RESULT_OK){

                Bundle extras = data.getExtras();
                int type = extras.getInt(Constants.EDIT_PROFILE_ITEMS);

                switch (type){
                    case 1:
                        map.put(Constants.FIRST_NAME, extras.getString(Constants.MEMBER_NAME));
                        map.put(Constants.SURNAME, extras.getString(Constants.MEMBER_SURNAME));
                        break;
                    case 2:
                        map.put(Constants.EMAIL, extras.getString(Constants.MEMBER_EMAIL));
                        break;
                    case 3:
                        map.put(Constants.DESCRIPTION, extras.getString(Constants.MEMBER_DESCRIPTION));
                        break;
                    case 4:
                        addressChanged = true;
                        if(mAddress == null) mAddress = new Address();
                        mAddress.setAddress(extras.getString(Constants.MEMBER_DIRECCION));
                        mAddress.setPostalCode(extras.getString(Constants.MEMBER_POSTAL_CODE));
                        mAddress.setState(extras.getString(Constants.MEMBER_STATE));
                        mAddress.setCountry(extras.getString(Constants.MEMBER_COUNTRY));
                        mAddress.setCity(extras.getString(Constants.MEMBER_CITY));

                        break;

                }
            }
            if(addressChanged){
                createAddress(map);
            }else{
                updateMember(map);
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

        private ArrayList<Map<String, Object>> setAddressParams(){
            ArrayList<Map<String, Object>> params = new ArrayList<Map<String, Object>>();

            Map<String, Object> hasmap = new Hashtable<String, Object>();
            hasmap.put(Constants.CITY, mAddress.getCity());
            hasmap.put(Constants.COUNTRY, mAddress.getCountry());
            hasmap.put(Constants.MEMBERID, App.getMember().getId());
            hasmap.put(Constants.POSTAL_CODE, mAddress.getPostalCode());
            hasmap.put(Constants.STATE, mAddress.getState());
            hasmap.put(Constants.ADDRESS, mAddress.getAddress());

            params.add(hasmap);

            return params;
        }

        private ArrayList<Map<String, Object>> setUpadateParams(Map<String, Object> map){
            ArrayList<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
            params.add(map);
            return params;
        }

        private void updateMember(Map<String, Object> map){

            App.getRestClient()
                    .getPageService()
                    .updateMember(App.getMember().getId(), setUpadateParams(map))
                    .subscribe(new Action1<ApiResponse>() {
                        @Override
                        public void call(ApiResponse apiResponse) {
                            App.setMember(apiResponse.getMember());
                            if(apiResponse.getAddresses().size() > 0) App.getMember().setAddress(apiResponse.getAddresses().get(0));
                            App.getMember().setAddressStr(App.getMemberAddressStr(App.getMember()));
                            App.setMemberPrefs();
                            updateUI();
                        }
                    });
        }

        private void createAddress(Map<String, Object> map){
            App.getRestClient()
                    .getPageService()
                    .getMemberAddress(Integer.toString(App.getMember().getId()))
                    .flatMap(new Func1<ApiResponse, Observable<ApiResponse>>() {
                        @Override
                        public Observable<ApiResponse> call(ApiResponse apiResponse) {
                            if (apiResponse.getAddresses().size() == 0) {
                                Observable<ApiResponse> observable = App.getRestClient()
                                        .getPageService()
                                        .createAddress(setAddressParams());
                                return observable;
                            } else {
                                int id = apiResponse.getAddresses().get(0).getId();
                                Observable<ApiResponse> observable = App.getRestClient()
                                        .getPageService()
                                        .updateAddress(id, setAddressParams());
                                return observable;
                            }
                        }
                    })
                    .subscribe(new Action1<ApiResponse>() {
                        @Override
                        public void call(ApiResponse apiResponse) {
                            mAddress = apiResponse.getAddress();
                            Map<String, Object> map = new Hashtable<String, Object>();
                            map.put(Constants.DEFAULT_SHIPPING_ADDRESS, mAddress.getId());
                            updateMember(map);
                        }
                    });

        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

            Map<String,?> keys = sharedPreferences.getAll();

            /*for(Map.Entry<String,?> entry : keys.entrySet()){
                Log.e("map values", entry.getKey() + ": " +
                        entry.getValue().toString());
            } */
        }

        @Override
        public void onAttach(Activity activity){
            super.onAttach(activity);

            if(activity instanceof UserProfileSettings){
                mActivity = (UserProfileSettings)activity;
                mActivity.setPicasso(Picasso.with(mActivity));
            }
        }

        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen()
                    .getSharedPreferences()
                    .unregisterOnSharedPreferenceChangeListener(this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //getMenuInflater().inflate(R.menu.menu_user_profile_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            Intent returnIntent = new Intent();
            setResult(RESULT_OK, returnIntent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
