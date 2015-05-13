package com.homecooking.ykecomo.ui.activity.memberPreferenceActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;

public class EmailMember extends AppCompatActivity {

    private EditText mEmailTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_member);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(getResources().getString(R.string.DONE));

        mEmailTxt = (EditText) findViewById(R.id.etEmail);
        mEmailTxt.setText(App.getPref().getString(Constants.MEMBER_EMAIL, ""));
    }

    private void updateMember(){
        Intent returnIntent = new Intent();
        Bundle extras = new Bundle();
        extras.putString(Constants.MEMBER_EMAIL, mEmailTxt.getText().toString());
        extras.putInt(Constants.EDIT_PROFILE_ITEMS, Constants.EDIT_EMAIL_MEMBER_PROFILE);
        returnIntent.putExtras(extras);
        setResult(RESULT_OK, returnIntent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_edit_fullname_member, menu);
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
