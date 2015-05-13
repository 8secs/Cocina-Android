package com.homecooking.ykecomo.ui.activity.userProfile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.member.CreateMemberAction;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.operators.member.CheckMemberEmailFunc;
import com.homecooking.ykecomo.operators.member.CreateMemberFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import java.util.ArrayList;
import java.util.Hashtable;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignupActivity extends AppCompatActivity implements Validator.ValidationListener {

    @Required(order = 1)
    private EditText mNameTxt;
    @Required(order = 2)
    private EditText mLastNameTxt;
    @Required(order = 3)
    @Email(order = 4)
    private EditText mEmailTxt;
    @Required(order = 5)
    @Password(order = 6)
    @TextRule(order = 7, minLength = 6, message = "Enter at least 6 characters.")
    private EditText mPwdTxt;
    @ConfirmPassword(order = 8)
    private EditText mConfirmPwdTxt;

    private Validator mValidator;

    private Observable<ApiResponse> mUserObservable;
    private Subscription mUserSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_signup);

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        mNameTxt = (EditText) findViewById(R.id.etName);
        mLastNameTxt = (EditText) findViewById(R.id.etSurname);
        mEmailTxt = (EditText) findViewById(R.id.etEmail);
        mPwdTxt = (EditText) findViewById(R.id.etPassword);
        mConfirmPwdTxt = (EditText) findViewById(R.id.etPasswordConfirm);

        Button mSaveUser = (Button) findViewById(R.id.btnCreateAccount);
        mSaveUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });
    }

    private void signIn(){

        mUserObservable = App.getRestClient()
                .getPageService()
                .getMemberByEmail(mEmailTxt.getText().toString());

        mUserSubscription = AndroidObservable.bindActivity(this, mUserObservable)
                .map(new CheckMemberEmailFunc(setParams(), getResources().getString(R.string.email_on_bbdd)))
                .flatMap(new CreateMemberFunc())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new CreateMemberAction(this));

    }

    private ArrayList<Hashtable<String, String>> setParams(){
        ArrayList<Hashtable<String, String>> params = new ArrayList<Hashtable<String, String>>();
        Hashtable<String, String> first = new Hashtable<String, String>();
        first.put(Constants.FIRST_NAME, mNameTxt.getText().toString());
        Hashtable<String, String> sur = new Hashtable<String, String>();
        first.put(Constants.SURNAME, mLastNameTxt.getText().toString());
        Hashtable<String, String> e = new Hashtable<String, String>();
        first.put(Constants.EMAIL, mEmailTxt.getText().toString());
        Hashtable<String, String> fuid = new Hashtable<String, String>();
        first.put(Constants.PASSWORD, mPwdTxt.getText().toString());
        params.add(first);
        params.add(sur);
        params.add(e);
        params.add(fuid);
        return params;
    }

    public void updateUI(Member member){
        App.setMember(member);
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    public void onValidationSucceeded() { signIn(); }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
