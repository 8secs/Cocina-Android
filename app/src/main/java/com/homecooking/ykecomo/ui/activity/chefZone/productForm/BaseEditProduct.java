package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
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

    protected Bundle mExtras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mExtras = getIntent().getExtras();
    }

    protected void setupUI(){
        mTitleLabel = (TextView) findViewById(R.id.titlelb);
        mTitle = (EditText) findViewById(R.id.etTitle);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mNext = (Button) findViewById(R.id.next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });
    }

    protected void gotoNext(){ }

    public void onValidationSucceeded() { gotoNext(); }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }
}
