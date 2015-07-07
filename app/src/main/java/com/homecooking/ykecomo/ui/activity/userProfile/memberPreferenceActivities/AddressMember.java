package com.homecooking.ykecomo.ui.activity.userProfile.memberPreferenceActivities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.app.Utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class AddressMember extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private Spinner mSpinnerCountry;
    private EditText mAddressTxt;
    private EditText mCityTxt;
    private EditText mStateTxt;
    private EditText mCpTxt;

    private String mCountryCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_member);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getResources().getString(R.string.DONE));

        mSpinnerCountry = (Spinner) findViewById(R.id.spinnerCountry);
        mAddressTxt = (EditText) findViewById(R.id.etAddress);
        mCityTxt = (EditText) findViewById(R.id.etCity);
        mStateTxt = (EditText) findViewById(R.id.etState);
        mCpTxt = (EditText) findViewById(R.id.etCp);

        //Log.e("Address", App.getPref().getString(Constants.MEMBER_ADDRESS, ""));
        fillForm();
    }

    private void fillForm(){
        SharedPreferences preferences = App.getPref();


        if(preferences.getBoolean(Constants.MEMBER_HAS_ADDRESS, false)){
            fillSpinner(preferences.getString(Constants.MEMBER_COUNTRY, ""));
            mAddressTxt.setText(preferences.getString(Constants.MEMBER_DIRECCION, ""));
            mStateTxt.setText(preferences.getString(Constants.MEMBER_STATE, ""));
            mCpTxt.setText(preferences.getString(Constants.MEMBER_POSTAL_CODE, ""));
            mCityTxt.setText(preferences.getString(Constants.MEMBER_CITY, ""));
        }else{
            if(App.isIsFbMember()){
                fillSpinner(preferences.getString(Constants.MEMBER_COUNTRY, ""));
                mCityTxt.setText(preferences.getString(Constants.MEMBER_CITY, ""));
            }else{
                fillSpinner(Locale.getDefault().getCountry());
            }
        }
    }

    private void updateMember(){
        Intent returnIntent = new Intent();
        Bundle extras = new Bundle();
        if(mCountryCode != null && mCountryCode.length() > 0) extras.putString(Constants.MEMBER_COUNTRY, mCountryCode);
        extras.putString(Constants.MEMBER_DIRECCION, mAddressTxt.getText().toString());
        extras.putString(Constants.MEMBER_CITY, mCityTxt.getText().toString());
        extras.putString(Constants.MEMBER_STATE, mStateTxt.getText().toString());
        extras.putString(Constants.MEMBER_POSTAL_CODE, mCpTxt.getText().toString());
        extras.putInt(Constants.EDIT_PROFILE_ITEMS, Constants.EDIT_ADDRESS_MEMBER_PROFILE);
        returnIntent.putExtras(extras);
        setResult(RESULT_OK, returnIntent);
    }

    private void fillSpinner(String contryCode){
        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_row, countries);
        mSpinnerCountry.setAdapter(adapter);
        mSpinnerCountry.setOnItemSelectedListener(this);

        mSpinnerCountry.setSelection(countries.indexOf(Utility.getCountryName(contryCode, "es")));


    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String selected = parent.getItemAtPosition(pos).toString();
        mCountryCode = Utility.getCountryCode(selected, "es");
        //Log.e("mCountryCode", mCountryCode);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_address_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            updateMember();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
