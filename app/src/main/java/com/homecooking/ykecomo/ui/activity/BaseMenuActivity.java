package com.homecooking.ykecomo.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;
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
import com.homecooking.ykecomo.app.Utility;
import com.homecooking.ykecomo.model.Favorite;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.operators.ErrorHandler;
import com.homecooking.ykecomo.operators.location.AddressToStringFunc;
import com.homecooking.ykecomo.operators.member.SetAvatarAddressMapMember;
import com.homecooking.ykecomo.ui.activity.chefZone.ChefMenuPrincipalActivity;
import com.homecooking.ykecomo.ui.activity.userProfile.LoginActivity;
import com.homecooking.ykecomo.ui.activity.userProfile.ViewUserProfileActivity;
import com.homecooking.ykecomo.ui.fragment.MenuFragment;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.List;

import pl.charmas.android.reactivelocation.ReactiveLocationProvider;
import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class BaseMenuActivity extends AppCompatActivity implements MenuFragment.OnFragmentInteractionListener{

    protected static final int PROFILE_SETTING = 1;

    protected int mSelctedView = -1;
    protected int mSelectedMode = -1;
    protected Activity mActivity;

    protected MenuFragment mSelectedFragment;
    protected ArrayList<MenuFragment> mFragments = new ArrayList<>();

    //USER ENVIRONMENT
    private final String[] TITLES = { "Categories", "Chefs", "Favorites", "Messages", "ShoppingCart" };
    private final int[] ICONS = {R.mipmap.ic_launcher, R.mipmap.ic_chef, R.mipmap.ic_favorite_color, R.mipmap.ic_email, R.mipmap.ic_shopping_cart};
    protected MenuFragment mProductCategoriesFragment;
    protected MenuFragment mChefsFragement;
    protected MenuFragment mFavoriteFragment;
    protected MenuFragment mMessageFragment;
    protected MenuFragment mShoppingFragment;

    //CHEF ENVIROMENT
    private final String[] TITLES_CHEF = { "Products"};
    private final int[] ICONS_CHEF = {R.mipmap.ic_launcher};
    protected MenuFragment mProductsChefFragment;

    //UI
    protected AccountHeader.Result headerResult = null;
    protected Drawer.Result drawerResult = null;

    protected CircleProgressBar mProgress;
    protected Button mAccessUser;

    protected PagerSlidingTabStrip mTabs;
    protected ViewPager mPager;
    protected MenuPagerAdapter mPagerAdapter;

    protected IDrawerItem[] mDrawerItems;
    protected IProfile mProfile;

    //MEMBER
    protected boolean isUser = false;
    protected UiLifecycleHelper uiHelper;
    protected Session.StatusCallback sessionStatusCallback;
    protected static final String TOKEN_CACHE_NAME_KEY = "TokenCacheName";

    //LISTS DATA
    protected ArrayList<ProductCategory> mMenuList = new ArrayList<ProductCategory>();
    protected ArrayList<Member> mMenuChefList = new ArrayList<Member>();
    protected ArrayList<Favorite> mMenuWishList = new ArrayList<>();
    protected ArrayList<?> mMenuMessageList = new ArrayList<>();

    public void setMenuList(ArrayList<ProductCategory> mMenuList) {
        this.mMenuList = mMenuList;
    }
    public ArrayList<ProductCategory> getMenuList() { return mMenuList; }

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

    protected void setFragmentsViews(){

        if(mSelectedMode == Constants.USER_ENVIRONMENT_MODE){

            mProductCategoriesFragment = MenuFragment.newInstance(mSelectedMode, this);
            mChefsFragement = MenuFragment.newInstance(mSelectedMode, this);
            mFavoriteFragment = MenuFragment.newInstance(mSelectedMode, this);
            mMessageFragment = MenuFragment.newInstance(mSelectedMode, this);
            mShoppingFragment = MenuFragment.newInstance(mSelectedMode, this);

            mFragments.add(mProductCategoriesFragment);
            mFragments.add(mChefsFragement);
            mFragments.add(mFavoriteFragment);
            mFragments.add(mMessageFragment);
            mFragments.add(mShoppingFragment);
        }else{
            mProductsChefFragment = MenuFragment.newInstance(mSelectedMode, this);
            mFragments.add(mProductsChefFragment);
        }
        mSelectedFragment = (MenuFragment) mFragments.get(0);
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

        mTabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        mPager = (ViewPager) findViewById(R.id.pager);

        setFragmentsViews();

        mPagerAdapter = new MenuPagerAdapter(getSupportFragmentManager(), mFragments);
        mPager.setOffscreenPageLimit(mFragments.size());
        if(mSelectedMode == Constants.USER_ENVIRONMENT_MODE) mPagerAdapter.setIcons(ICONS);
        else mPagerAdapter.setIcons(ICONS_CHEF);

        final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
                .getDisplayMetrics());

        mPager.setPageMargin(pageMargin);
        mPager.setAdapter(mPagerAdapter);
        mTabs.setViewPager(mPager);
        mTabs.setOnPageChangeListener(pageListener);

        uiHelper = new UiLifecycleHelper(this, sessionStatusCallback);
        uiHelper.onCreate(savedInstanceState);
        mProgress = (CircleProgressBar) findViewById(R.id.progressBar);
    }

    protected void createHeaderMenu(Bundle savedInstanceState){
        headerResult = new AccountHeader()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header_green)
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
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(false)
                .withTranslucentStatusBar(true)
                .withFooter(R.layout.footer_menu)
                .addDrawerItems(mDrawerItems)
                .withOnDrawerItemClickListener(itemClickListener)
                .withSavedInstance(savedInstanceState)
                .withDrawerGravity(Gravity.END)
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
                    if (mSelectedMode == Constants.USER_ENVIRONMENT_MODE) {
                        Intent i = new Intent(BaseMenuActivity.this, ChefMenuPrincipalActivity.class);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(BaseMenuActivity.this, MenuPrincipalActivity.class);
                        startActivity(i);
                    }
                }
            }
        });

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
        Log.e("setActivityResult", Integer.toString(requestCode));
        switch (requestCode){
            case Constants.LOGIN_REQUEST_CODE:
                Utility.setMemberPrefs();
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
            case Constants.EDIT_PRODUCT_REQUEST_CODE:
                Log.e("setActivityResult", "code " + Constants.EDIT_PRODUCT_REQUEST_CODE);
                getProducts();
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
        if(!Utility.isNetworkAvailable(this)){
            Log.e("isNetworkConnection", "no hay conexión");
        }else{
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
                                    Utility.setMemberPrefs();
                                    getAvatarLoginUser();
                                }
                            });
                }
            }else{
                Utility.setMemberFromPrefs();
                getLoginUser();
            }
        }
    }

    protected void getAvatarLoginUser(){
        isUser = true;
        if(mSelectedMode == Constants.USER_ENVIRONMENT_MODE) mAccessUser.setText(getResources().getString(R.string.cambiar_modo_chef_btn));
        else mAccessUser.setText(getResources().getString(R.string.modo_user_btn));
        String imageURL = "";
        if(App.getMember().getAvatarFilename() != null) {
            if(App.getMember().getAvatarFilename() != null)
                imageURL = Constants.BASE_URL.concat(App.getMember().getAvatarFilename());
        } else {
            if(App.isIsFbMember()) imageURL = Utility.getUserpicFbURL();
        }
        if(imageURL.length() > 0){
            mProfile.setIcon(imageURL);
            mProfile.setEmail(App.getMember().getEmail());
            mProfile.setName(App.getMember().getFirstName());
            headerResult.updateProfileByIdentifier(mProfile);
        }

        if(mSelectedMode == Constants.CHEF_ENVIROMENT_MODE){
            if(App.getProductsChef() != null){
                getProducts();
            }
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

    @Override
    public void onListItemInteration(int position) { }

    @Override
    public void onButtonPressed() { }

    protected void unSubscribeLocation(){
        if(Utility.isNetworkAvailable(this)){
            updatableLocationSubscription.unsubscribe();
            addressSubscription.unsubscribe();
            lastKnownLocationSubscription.unsubscribe();
        }
    }

    protected void showProgress(boolean bool){
        if(bool){
            mProgress.setVisibility(View.VISIBLE);
        }else{
            mProgress.setVisibility(View.GONE);
        }
    }

    protected void getProducts(){ }

    protected void getProductCategories(){ }

    protected void getPublicChefs(){ }

    protected void getFavorites(){ }

    protected ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            mSelectedFragment = mFragments.get(position);
            if(mSelectedMode == Constants.USER_ENVIRONMENT_MODE){
                switch (position){
                    case 0:
                        if(mMenuList.size() == 0){
                            showProgress(true);
                            getProductCategories();
                        }
                        break;
                    case 1:
                        if(mMenuChefList.size() == 0) {
                            showProgress(true);
                            getPublicChefs();
                        }
                        break;
                    case 2:
                        if(mMenuWishList.size() == 0){
                            showProgress(true);
                            getFavorites();
                        }
                        break;
                }
            }else{
                if(App.getProductsChef().size() == 0){
                    showProgress(true);
                    getProducts();
                }
            }
        }
        @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
        @Override public void onPageScrollStateChanged(int state) { }
    };

    public class MenuPagerAdapter extends FragmentStatePagerAdapter implements PagerSlidingTabStrip.IconTabProvider {

        List<MenuFragment> fragments;

        private String[] titles;
        private int[] icons;

        public MenuPagerAdapter(FragmentManager fm, List<MenuFragment> fragments){
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getPageIconResId(int position){ return icons[position]; }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        public void addFragment(MenuFragment fragment) {
            this.fragments.add(fragment);
        }

        @Override
        public MenuFragment getItem(int position) {

            return this.fragments.get(position);
        }

        public void setIcons(int[] icons) {
            this.icons = icons;
        }

    }

    @Override
    protected void onStart(){
        super.onStart();
        if(!Utility.isNetworkAvailable(this)){
            Log.e("isNetworkConnection", "no hay conexión");
        }else{
            createUserLocationSubscription();
        }
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
