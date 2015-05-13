package com.homecooking.ykecomo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.SharedPreferencesTokenCachingStrategy;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.google.android.gms.location.LocationRequest;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.location.DisplayTextOnViewAction;
import com.homecooking.ykecomo.actions.location.SetLocationAction;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.operators.ErrorHandler;
import com.homecooking.ykecomo.operators.location.AddressToStringFunc;
import com.homecooking.ykecomo.operators.member.SetAvatarAddressMapMember;
import com.homecooking.ykecomo.ui.activity.chefZone.ChefMenuPrincipalActivity;
import com.homecooking.ykecomo.ui.activity.userProfile.LoginActivity;
import com.homecooking.ykecomo.ui.activity.userProfile.ViewUserProfileActivity;
import com.homecooking.ykecomo.ui.view.DividerDecoration;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by andres on 12/05/15.
 */
public class BaseMenuActivity extends AppCompatActivity implements
        SwipeRefreshLayout.OnRefreshListener {

    protected static final int PROFILE_SETTING = 1;

    protected static int USER_MODE = 0;
    protected static int CHEF_MODE = 1;

    protected int mSelctedView = -1;
    protected int mSelectedMode = -1;
    protected Activity mActivity;

    protected Toolbar mToolbar;
    protected AccountHeader.Result headerResult = null;
    protected Drawer.Result drawerResult = null;
    protected RecyclerView mList;
    protected SwipeRefreshLayout mRefreshLayout;
    protected ImageView mMenuIcon;
    protected CircleProgressBar mProgress;
    protected Button mAccessUser;

    protected IDrawerItem[] mDrawerItems;
    protected IProfile mProfile;

    //MEMBER
    protected boolean isUser = false;
    protected UiLifecycleHelper uiHelper;
    protected Session.StatusCallback sessionStatusCallback;
    protected static final String TOKEN_CACHE_NAME_KEY = "TokenCacheName";

    /**
     * * USER LOCATION
     */
    protected ReactiveLocationProvider locationProvider;
    protected Observable<Location> lastKnownLocationObservable;
    protected Observable<Location> locationUpdatesObservable;

    protected Subscription lastKnownLocationSubscription;
    protected Subscription updatableLocationSubscription;
    protected Subscription addressSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUserLocationObservable();
        /**
         * SAVED INSTANCE STATE
         */
        if (savedInstanceState != null) {
            if(savedInstanceState.getInt(Constants.TYPE_USER, 0) == 2){
                SharedPreferencesTokenCachingStrategy restoredCache = new SharedPreferencesTokenCachingStrategy(
                        this,
                        savedInstanceState.getString(TOKEN_CACHE_NAME_KEY));
                App.setCurrentFbSession(Session.restoreSession(
                        this,
                        restoredCache,
                        sessionStatusCallback,
                        savedInstanceState));
                fetchFbUserInfo();
            }else if( savedInstanceState.getInt(Constants.TYPE_USER, 0) == 1){
                getLoginUser();
            }
        }else{
            if(App.getMember() != null) {
                getLoginUser();
            }
        }
    }

    protected void setupUI(@Nullable Bundle savedInstanceState){
        sessionStatusCallback = new Session.StatusCallback() {
            @Override
            public void call(Session session, SessionState state, Exception exception) {
                onSessionFbStateChange(session, state, exception);
            }
        };

        mProfile = new ProfileDrawerItem()
                .withName("Tu nombre")
                .withEmail("email@ykecomes.com")
                .withIcon(getResources().getDrawable(R.drawable.profile2))
                .withIdentifier(PROFILE_SETTING);

        setContentView(R.layout.activity_menu_principal);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        uiHelper = new UiLifecycleHelper(this, sessionStatusCallback);
        uiHelper.onCreate(savedInstanceState);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.recycler_swipe);
        mProgress = (CircleProgressBar) findViewById(R.id.progressBar);

        setupList();
        mRefreshLayout.setOnRefreshListener(this);

    }

    protected void createHeaderMenu(Bundle savedInstanceState){
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(mProfile)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {

                        if (profile instanceof IDrawerItem && ((IDrawerItem) profile).getIdentifier() == PROFILE_SETTING) {
                            if (headerResult.getProfiles() != null) {
                                if (isUser) {
                                    Intent i = new Intent(BaseMenuActivity.this, ViewUserProfileActivity.class);
                                    startActivityForResult(i, Constants.EDIT_PROFILE_REQUEST_CODE);
                                }
                            }
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    protected void setupDrawer(Bundle savedInstanceState, Drawer.OnDrawerItemClickListener itemClickListener, IDrawerItem... drawerItems){

        drawerResult = new Drawer()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withTranslucentStatusBar(true)
                .withFooter(R.layout.footer_menu)
                .addDrawerItems(mDrawerItems)
                .withOnDrawerItemClickListener(itemClickListener)
                .withSavedInstance(savedInstanceState)
                .build();

        mAccessUser = (Button) findViewById(R.id.access_btn);

        mAccessUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUser) {
                    Intent i = new Intent(BaseMenuActivity.this, LoginActivity.class);
                    startActivityForResult(i, Constants.LOGIN_REQUEST_CODE);
                    drawerResult.closeDrawer();
                } else {
                    if(mSelectedMode == USER_MODE){
                        Intent i = new Intent(BaseMenuActivity.this, ChefMenuPrincipalActivity.class);
                        startActivity(i);
                    }else{
                        Intent i = new Intent(BaseMenuActivity.this, MenuPrincipalActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

    }

    protected void setupList(){
        mList = (RecyclerView) mRefreshLayout.findViewById(R.id.section_list);
        mList.setLayoutManager(getLayoutManager());
        mList.addItemDecoration(getItemDecoration());

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerDecoration(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        if(isUser){
            if(App.getCurrentFbSession() != null){
                if(App.getCurrentFbSession().isOpened()){
                    Session.saveSession(App.getCurrentFbSession(), outState);
                    uiHelper.onSaveInstanceState(outState);
                    outState.putInt(Constants.TYPE_USER, Constants.FB_USER_TYPE);
                }
            }else{
                outState.putInt(Constants.TYPE_USER, Constants.LOGIN_USER_TYPE);
            }
        }

        outState = drawerResult.saveInstanceState(outState);
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            setActivityResult(requestCode);
        }
        if(resultCode == RESULT_CANCELED){
            /**
             * TODO: QUEDA PENDIENTE VER SI HAY QUE METER ALGO AQUI
             */
        }
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    protected void setActivityResult(int requestCode){
        switch (requestCode){
            case Constants.LOGIN_REQUEST_CODE:
                App.setMemberPrefs();
                if(App.getMember().getFacebookUID() == null){
                    App.setIsFbMember(false);
                    getAvatarLoginUser();
                }else{
                    App.setIsFbMember(true);
                    fetchFbUserInfo();
                }
                break;
            case Constants.EDIT_PROFILE_REQUEST_CODE:
                if(!App.isIsFbMember()) getAvatarLoginUser();
                break;
        }
    }

    protected void onSessionFbStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            App.setCurrentFbSession(session);
            fetchFbUserInfo();
        } else if (state.isClosed()) {
            /**
             * TODO: QUEDA PENDIENTE VER SI HAY QUE METER ALGO AQUI
             */
        }
    }

    protected void fetchFbUserInfo() {
        if (App.getCurrentFbSession() != null && App.getCurrentFbSession().isOpened()) {
            Request request = Request.newMeRequest(App.getCurrentFbSession(), new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser me, Response response) {
                    if (response.getRequest().getSession() == App.getCurrentFbSession()) {
                        if (me.getLocation() != null) {
                            App.setFbLocation(me.getLocation().getName());
                        }
                        getAvatarLoginUser();
                    }
                }
            });
            Bundle extras = request.getParameters();
            extras.putString("fields", "location");
            request.executeAsync();
        }else{
            Session.openActiveSession(this, false, sessionStatusCallback);
        }
    }

    protected void getLoginUser(){
        if(App.getMember() != null){
            if(App.getMember().getId() > 0){
                App.getRestClient()
                        .getPageService()
                        .getMember(Integer.toString(App.getMember().getId()))
                        .map(new SetAvatarAddressMapMember())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .cache()
                        .subscribe(new Action1<Member>() {
                            @Override
                            public void call(Member member) {
                                App.setMember(member);
                                App.setMemberPrefs();
                                getAvatarLoginUser();
                            }
                        });
            }
        }else{
            App.setMemberFromPrefs();
            getLoginUser();
        }
    }

    protected void getAvatarLoginUser(){
        isUser = true;
        if(mSelectedMode == USER_MODE) mAccessUser.setText(getResources().getString(R.string.cambiar_modo_chef_btn));
        else mAccessUser.setText(getResources().getString(R.string.modo_user_btn));
        String imageURL = "";
        if(App.getMember().getAvatarFilename() != null) {
            if(App.getMember().getAvatarFilename() != null)
                imageURL = Constants.BASE_URL.concat(App.getMember().getAvatarFilename());
        } else {
            if(App.isIsFbMember()) imageURL = App.getUserpicFbURL();
        }
        if(imageURL.length() > 0){
            mProfile.setIcon(imageURL);
            mProfile.setEmail(App.getMember().getEmail());
            mProfile.setName(App.getMember().getFirstName());
            headerResult.updateProfileByIdentifier(mProfile);
        }
    }

    protected void setupUserLocationObservable(){
        locationProvider = new ReactiveLocationProvider(this.getApplicationContext());
        lastKnownLocationObservable = locationProvider.getLastKnownLocation();
        locationUpdatesObservable = locationProvider.getUpdatedLocation(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setNumUpdates(5)
                        .setInterval(100)
        );
    }

    protected void createUserLocationSubscription(){
        lastKnownLocationSubscription = lastKnownLocationObservable
                .subscribe(new SetLocationAction(Constants.LAST_KNOWN_LOCATION), new ErrorHandler());

        updatableLocationSubscription = locationUpdatesObservable
                .subscribe(new SetLocationAction(Constants.LOCATIONS_UPDATES), new ErrorHandler());

        addressSubscription = AndroidObservable.bindActivity(this, locationUpdatesObservable
                .flatMap(new Func1<Location, Observable<List<Address>>>() {
                    @Override
                    public Observable<List<Address>> call(Location location) {
                        return locationProvider.getGeocodeObservable(location.getLatitude(), location.getLongitude(), 1);
                    }
                })
                .map(new Func1<List<Address>, Address>() {
                    @Override
                    public Address call(List<Address> addresses) {
                        return addresses != null && !addresses.isEmpty() ? addresses.get(0) : null;
                    }
                })
                .map(new AddressToStringFunc())
                .subscribeOn(Schedulers.io()))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisplayTextOnViewAction(), new ErrorHandler());
    }

    protected void unSubscribeLocation(){
        updatableLocationSubscription.unsubscribe();
        addressSubscription.unsubscribe();
        lastKnownLocationSubscription.unsubscribe();
    }

    protected void showProgress(boolean bool){
        if(bool){
            mProgress.setVisibility(View.VISIBLE);
        }else{
            mProgress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onRefresh() {
        /**
         * TODO: VERIFICAR CON QUE LISTA TRABAJAMOS PARA ACTUALIZAR
         */
    }

    @Override
    protected void onStart(){
        super.onStart();
        createUserLocationSubscription();
    }

    @Override
    protected void onResume(){
        super.onResume();
        uiHelper.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        uiHelper.onPause();

        AppEventsLogger.deactivateApp(this);
        unSubscribeLocation();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        uiHelper.onDestroy();
        AppEventsLogger.deactivateApp(this);
        unSubscribeLocation();
    }

    @Override
    public void onBackPressed() {
        if (drawerResult != null && drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
