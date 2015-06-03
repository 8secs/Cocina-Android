package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Required;

/**
 * Created by: andres
 * User: andres
 * Date: 26/05/15
 * Time: 17 : 35
 */
public class BaseEditProduct extends AppCompatActivity implements Validator.ValidationListener {

    @Required(order = 1)
    protected EditText mTitle;
    protected TextView mTitleLabel;
    protected Button mNext;
    protected Validator mValidator;
    protected ActionBar mActionbar;

    protected Bundle mExtras;
    protected String mStr;

    protected int mSelectedColumn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExtras = getIntent().getExtras();
        mActionbar = getSupportActionBar();
        mActionbar.setDisplayHomeAsUpEnabled(true);
    }
    protected void setupUI(){

        mTitleLabel = (TextView) findViewById(R.id.titlelb);
        mTitle = (EditText) findViewById(R.id.etTitle);
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mNext = (Button) findViewById(R.id.next);

        if(mExtras != null){
            if(mExtras.getInt(Constants.PRODUCT_ITEMS) == Constants.EDIT_PRODUCT_ITEM){
                mNext.setVisibility(View.GONE);
                mActionbar.setHomeAsUpIndicator(getResources().getDrawable(R.drawable.done_pref_btn));
                mActionbar.setTitle(getResources().getString(R.string.DONE));
            }else{
                mNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mValidator.validate();
                    }
                });
            }
        }
    }

    protected void update(){ }

    public void onValidationSucceeded() { update(); }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { return true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                update();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
