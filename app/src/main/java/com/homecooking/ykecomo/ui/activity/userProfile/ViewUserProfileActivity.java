package com.homecooking.ykecomo.ui.activity.userProfile;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.manuelpeinado.fadingactionbar.extras.actionbarcompat.FadingActionBarHelper;


public class ViewUserProfileActivity extends BaseUserProfileActivity {

    protected TextView mLocationUser;
    protected GoogleMap mMap;

    public TextView getLocationUser(){ return mLocationUser; }
    public GoogleMap getMap() { return mMap; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupUI();
        try {
            initilizeMap();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initilizeMap() {
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            mMap.setMyLocationEnabled(true);

            if (mMap == null) {
                Toast.makeText(getApplicationContext(),
                        getResources().getString(R.string.no_map), Toast.LENGTH_SHORT)
                        .show();
            }else{
                addMarkerToMap();
            }
        }
    }
    
    private void addMarkerToMap(){
        getLocationUser().setText(getResources().getString(R.string.tu_posicion));
        double latitude = App.getUserUpdateLocation().getLatitude();
        double longitude = App.getUserUpdateLocation().getLongitude();
        MarkerOptions marker = new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(App.getUserAddress());
        getMap().addMarker(marker);

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                new LatLng(latitude, longitude)).zoom(12).build();
        getMap().animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }


    private void setupUI(){
        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.mipmap.ab_background)
                .headerLayout(R.layout.header_fading_scroll)
                .contentLayout(R.layout.activity_view_user_profile)
                .lightActionBar(false);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        mHeaderView = (ImageView) findViewById(R.id.image_header);
        mUsernameTxt = (TextView) findViewById(R.id.user_name);
        mDescriptionTxt = (TextView) findViewById(R.id.user_description);
        mEmailTxt = (TextView) findViewById(R.id.user_email);
        mFacebookTxt = (TextView) findViewById(R.id.user_fb);
        mUserAddressTxt = (TextView) findViewById(R.id.user_address);
        mEmailVerified = (TextView) findViewById(R.id.user_email_verified);

        mLocationUser = (TextView) findViewById(R.id.location_user);

        updateUI();

    }

    private void updateUI(){
        String url = "";
        if (!App.isIsFbMember()) {
            url = Constants.BASE_URL.concat(App.getPref().getString(Constants.MEMBER_IMAGE, ""));
            mFacebookTxt.setText(getResources().getString(R.string.no_tenemos_facebook));

            if (App.getPref().getString(Constants.MEMBER_VERIFIED, "0").equals("1"))
                mEmailVerified.setText(getResources().getString(R.string.email_verified));
            else
                getEmailVerified().setText(getResources().getString(R.string.email_no_verified));

            getUserAddressTxt().setText(App.getPref().getString(Constants.MEMBER_ADDRESS, ""));

        } else {
            if(App.getMember().getAvatarFilename() != null) url = Constants.BASE_URL.concat(App.getPref().getString(Constants.MEMBER_IMAGE, ""));
            else url = App.getUserpicFbURL();
            getFacebookTxt().setText(getResources().getString(R.string.registro_en_facebook));
            getEmailVerified().setText(getResources().getString(R.string.email_verified));

            if(!App.getPref().getBoolean(Constants.MEMBER_HAS_ADDRESS, false))
                getUserAddressTxt().setText(App.getPref().getString(Constants.MEMBER_CITY, "") + ", " + App.getPref().getString(Constants.MEMBER_COUNTRY, ""));
            else{
                getUserAddressTxt().setText(App.getMemberAddressStr(App.getMember()));
            }
        }

        setImage(url, getHeaderView());
        getUsernameTxt().setText(App.getPref().getString(Constants.MEMBER_NAME, "") + " " + App.getPref().getString(Constants.MEMBER_SURNAME, ""));

        String description = App.getPref().getString(Constants.MEMBER_DESCRIPTION, "");
        if (description.length() > 0)
            getDescriptionTxt().setText(Html.fromHtml(description));
        else
            getDescriptionTxt().setText(getResources().getString(R.string.no_description_profile));

        getEmailTxt().setText(App.getPref().getString(Constants.MEMBER_EMAIL, ""));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            Log.e("onActivityResult", "ResultOK");
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_perfil) {
            Intent i = new Intent(this, UserProfileSettings.class);
            startActivityForResult(i, Constants.EDIT_PROFILE_REQUEST_CODE);
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //if(App.getMember() != null) createObservable(Constants.VIEW_PROFILE_OBSERVABLE_TYPE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //if(App.getMember() != null) mUserSubscription.unsubscribe();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //if(App.getMember() != null) mUserSubscription.unsubscribe();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initilizeMap();
        updateUI();
    }
}
