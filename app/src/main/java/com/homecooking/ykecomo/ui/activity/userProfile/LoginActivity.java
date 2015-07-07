package com.homecooking.ykecomo.ui.activity.userProfile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.member.LoginFbMemberAction;
import com.homecooking.ykecomo.actions.member.LoginMemberAction;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.app.Utility;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Auth;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.operators.ErrorHandler;
import com.homecooking.ykecomo.operators.member.LoginFbMemberFunc;
import com.homecooking.ykecomo.operators.member.LoginMemberFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import java.util.ArrayList;
import java.util.Hashtable;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;

public class LoginActivity extends AppCompatActivity implements Validator.ValidationListener {

    Button btn_LoginIn = null;
    Button btn_SignUp = null;

    @Required(order = 1)
    @Email(order = 2)
    private EditText mUserNameEditText;
    @Required(order = 3)
    @TextRule(order = 4, minLength = 6, message = "Enter at least 5 characters.")
    private EditText mPasswordEditText;

    private Observable<Auth> mLoginObservable;
    private Subscription mLoginSubscription;

    public Auth getmAuth() {
        return mAuth;
    }

    public void setmAuth(Auth mAuth) {
        this.mAuth = mAuth;
    }

    private Auth mAuth;

    public void setMembers(ArrayList<Member> mMembers) {
        this.mMembers = mMembers;
    }

    public void setImages(ArrayList<Image> mImages) {
        this.mImages = mImages;
    }

    public void setAddresses(ArrayList<Address> mAddresses) {
        this.mAddresses = mAddresses;
    }

    private ArrayList<Member> mMembers;
    private ArrayList<Image> mImages;
    private ArrayList<Address> mAddresses;

    private Validator mValidator;

    private Observable<ApiResponse> mUserObservable;
    private Subscription mUserSubscription;

    private static final String PERMISSION = "public_profile, email, user_location";
    //private final String PENDING_ACTION_BUNDLE_KEY = "com.visitaelaljarafe.homecooking:PendingAction";

    private LoginButton loginButton;
    private PendingAction pendingAction = PendingAction.NONE;

    private enum PendingAction { NONE }

    public GraphUser user;
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            String name = savedInstanceState.getString(Constants.PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }

        setContentView(R.layout.activity_login);

        btn_LoginIn = (Button) findViewById(R.id.btn_login);
        btn_SignUp = (Button) findViewById(R.id.btn_signup);
        mUserNameEditText = (EditText) findViewById(R.id.username);
        mPasswordEditText = (EditText) findViewById(R.id.password);

        btn_LoginIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mValidator.validate();
            }// End onClick
        });

        btn_SignUp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent in = new Intent(LoginActivity.this, SignupActivity.class);
                startActivityForResult(in, 1);
            }
        });

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);

        loginButton = (LoginButton) findViewById(R.id.authButton);
        loginButton.setPublishPermissions(PERMISSION);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if(user != null) {
                    LoginActivity.this.user = user;
                    getFbUser();
                }
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

        outState.putString(Constants.PENDING_ACTION_BUNDLE_KEY, pendingAction.name());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                sendBackToActivity(App.getMember());
            }
        }
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "El usuario ha cancelado el proceso", Toast.LENGTH_SHORT).show();
        }
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
    }

    private void login(){
        if(Utility.isNetworkAvailable(this)){
            mLoginObservable = App.getRestClient()
                    .getPageService()
                    .loginUser(mUserNameEditText.getText().toString(),
                            mPasswordEditText.getText().toString());

            mLoginSubscription = AndroidObservable.bindActivity(this, mLoginObservable)
                    .flatMap(new LoginMemberFunc(this))
                    .subscribe(new LoginMemberAction(this), new ErrorHandler());
        }else{
            Log.e("isNetworkAvailable", "Sin conexion");
        }

    }

    private void getFbUser(){

        mUserObservable = App.getRestClient().getPageService().getFbMember(user.getId());

        mUserSubscription = AndroidObservable.bindActivity(this, mUserObservable)
                .flatMap(new LoginFbMemberFunc(setFbParams()))
                .subscribe(new LoginFbMemberAction(this));
    }
    
    public void setupUser(){
        if(mAuth.getResult() == true){
            Member member = mMembers.get(0);
            member.setAvatarFilename(mImages.get(0).getFilename());
            member.setAddress(mAddresses.get(0));
            member.setAddressStr(Utility.getMemberAddressStr(member));
            Session session = Session.getActiveSession();
            if(session.isOpened()) session.close();
            sendBackToActivity(member);
        }else{
            Toast.makeText(this, getResources().getString(R.string.email_pass_invalid), Toast.LENGTH_LONG).show();
        }
    }

    private ArrayList<Hashtable<String, String>> setFbParams(){
        Log.e("setFbParams", "lugar " + LoginActivity.this.user.getLocation().getName());

        String firstname = LoginActivity.this.user.getFirstName();
        String facebookUID = LoginActivity.this.user.getId();
        String email = LoginActivity.this.user.getProperty("email").toString();
        String surname = LoginActivity.this.user.getLastName();
        String link = LoginActivity.this.user.getLink();
        ArrayList<Hashtable<String, String>> params = new ArrayList<Hashtable<String, String>>();

        Hashtable<String, String> first = new Hashtable<String, String>();
        first.put(Constants.FIRST_NAME, firstname);
        Hashtable<String, String> sur = new Hashtable<String, String>();
        first.put(Constants.SURNAME, surname);
        Hashtable<String, String> e = new Hashtable<String, String>();
        first.put(Constants.EMAIL, email);
        Hashtable<String, String> fuid = new Hashtable<String, String>();
        first.put(Constants.FACEBOOK_UID, facebookUID);
        Hashtable<String, String> l = new Hashtable<String, String>();
        first.put(Constants.FACEBOOK_LINK, link);
        params.add(first);
        params.add(sur);
        params.add(e);
        params.add(fuid);
        params.add(l);
        return params;
    }

    public void onValidationSucceeded() { login(); }

    public void onValidationFailed(View failedView, Rule<?> failedRule) {
        String message = failedRule.getFailureMessage();

        if (failedView instanceof EditText) {
            failedView.requestFocus();
            ((EditText) failedView).setError(message);
        } else {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    // FACEBOOK

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            //Log.d("HelloFacebook", String.format("Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            //Log.d("HelloFacebook", "Success!");
        }
    };

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (pendingAction != PendingAction.NONE && (exception instanceof FacebookOperationCanceledException || exception instanceof FacebookAuthorizationException)) {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle(R.string.cancelled)
                    .setMessage(R.string.permission_not_granted)
                    .setPositiveButton(R.string.ok, null)
                    .show();
            pendingAction = PendingAction.NONE;
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

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    public void sendBackToActivity(Member member){

        App.setMember(member);
        Utility.setMemberPrefs();
        Intent returnIntent = new Intent();
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}
