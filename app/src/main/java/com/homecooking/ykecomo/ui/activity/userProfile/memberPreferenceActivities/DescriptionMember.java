package com.homecooking.ykecomo.ui.activity.userProfile.memberPreferenceActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;

public class DescriptionMember extends AppCompatActivity {

    private EditText mDescTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_member);


        ActionBar actionbar = getSupportActionBar();
        //actionbar.setHomeButtonEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getResources().getString(R.string.DONE));

        mDescTxt = (EditText) findViewById(R.id.etDesc);
        mDescTxt.setText(Html.fromHtml(App.getPref().getString(Constants.MEMBER_DESCRIPTION, "")));
    }

    private void updateMember(){
        Intent returnIntent = new Intent();
        Bundle extras = new Bundle();
        extras.putString(Constants.MEMBER_DESCRIPTION, mDescTxt.getText().toString());
        extras.putInt(Constants.EDIT_PROFILE_ITEMS, Constants.EDIT_DESC_MEMBER_PROFILE);
        returnIntent.putExtras(extras);
        setResult(RESULT_OK, returnIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

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
