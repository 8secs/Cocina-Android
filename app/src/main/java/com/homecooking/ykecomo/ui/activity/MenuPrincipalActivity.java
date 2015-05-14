package com.homecooking.ykecomo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.product.GetProductCategoryAction;
import com.homecooking.ykecomo.actions.product.GetProductCategoryOnNext;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.operators.ErrorHandler;
import com.homecooking.ykecomo.operators.member.GetAvatarMemberAction;
import com.homecooking.ykecomo.operators.member.GetAvatarMemberOnNext;
import com.homecooking.ykecomo.operators.product.GetImageObservableProductCategoryFunc;
import com.homecooking.ykecomo.operators.product.GetImageProductGategoryFunc;
import com.homecooking.ykecomo.operators.product.GetProductCategoriesFunc;
import com.homecooking.ykecomo.operators.product.SetImageProductCategoryFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import java.util.ArrayList;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MenuPrincipalActivity  extends BaseMenuActivity {

    protected static int PRODUCT_CATEGORIES_VIEW = 0;
    protected static int CHEF_USER_VIEW = 1;

    //PRODUCT CATEGORIES
    /*private ShopAdapter mAdapter;*/
    private ArrayList<ProductCategory> mMenuList = new ArrayList<ProductCategory>();

    public void setMenuList(ArrayList<ProductCategory> mMenuList) {
        this.mMenuList = mMenuList;
    }
    public ArrayList<ProductCategory> getMenuList() { return mMenuList; }

    //CHEFS
    /*private ChefAdapter mChefAdapter;*/
    private ArrayList<Member> mMenuChefList = new ArrayList<Member>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDrawerItems = new IDrawerItem[7];
        mDrawerItems[0] = new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withIdentifier(1);
        mDrawerItems[1] = new PrimaryDrawerItem().withName(R.string.drawer_item_free_play).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(2);
        mDrawerItems[2] = new PrimaryDrawerItem().withName(R.string.drawer_item_custom).withIcon(FontAwesome.Icon.faw_eye).withIdentifier(3);
        mDrawerItems[3] = new SectionDrawerItem().withName(R.string.drawer_item_section_header).withIdentifier(4);
        mDrawerItems[4] = new SecondaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(5);
        mDrawerItems[5] = new SecondaryDrawerItem().withName(R.string.drawer_item_help).withIcon(FontAwesome.Icon.faw_question).setEnabled(true).withIdentifier(6);
        mDrawerItems[6] = new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(FontAwesome.Icon.faw_bullhorn).withIdentifier(7);

        mSelectedMode = Constants.USER_ENVIRONMENT_MODE;
        setupUI(savedInstanceState);
        App.setPrefUserEnvironment(Constants.USER_ENVIRONMENT_MODE);
    }

    @Override
    protected void setupUI(@Nullable Bundle savedInstanceState) {

        super.setupUI(savedInstanceState);

        mActivity = this;
        createHeaderMenu(savedInstanceState);
        setupDrawer(savedInstanceState, onDrawerItemClickListener, mDrawerItems);

        if (savedInstanceState == null) {
            drawerResult.setSelectionByIdentifier(PROFILE_SETTING, false);
            headerResult.setActiveProfile(mProfile);
        }
    }

    private void createProductCategories(){
        mSelctedView = PRODUCT_CATEGORIES_VIEW;
        if(mMenuList.size() > 0) mMenuList.removeAll(mMenuList);
        App.getRestClient()
                .getPageService()
                .getCategories()
                .flatMap(new GetProductCategoriesFunc(this))
                .flatMap(new GetImageObservableProductCategoryFunc())
                .flatMap(new GetImageProductGategoryFunc())
                .map(new SetImageProductCategoryFunc(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new GetProductCategoryOnNext(), new ErrorHandler(), new GetProductCategoryAction(this));
    }

    private void createChefs(){
        mSelctedView = CHEF_USER_VIEW;
        if(mMenuChefList.size() > 0) mMenuChefList.removeAll(mMenuChefList);
        /**
         * TODO: Filtrar chefs por localizacion del usuario.
         *      Queda pendiente pillar la direccion del chef y compararla con la ubicacion del usuario.
         */
        App.getRestClient()
                .getPageService()
                .getChefGroup(4)
                .map(new Func1<ApiResponse, ArrayList<Member>>() {
                    @Override
                    public ArrayList<Member> call(ApiResponse apiResponse) {
                        ArrayList<Member> members = apiResponse.getMembers();
                        for (Member member : members) {
                            if (member.getProducts() != null && member.getDefaultShippingAddress() != null) {
                                mMenuChefList.add(member);
                            }
                        }
                        return mMenuChefList;
                    }
                })
                .flatMap(new Func1<ArrayList<Member>, Observable<Member>>() {
                    @Override
                    public Observable<Member> call(ArrayList<Member> list) {
                        return Observable.from(mMenuChefList);
                    }
                })
                .flatMap(new Func1<Member, Observable<ApiResponse>>() {
                    @Override
                    public Observable<ApiResponse> call(Member member) {
                        Observable<ApiResponse> observable = App.getRestClient()
                                .getPageService()
                                .getMemberAddress(Integer.toString(member.getId()));
                        return observable;
                    }
                })
                .map(new Func1<ApiResponse, Member>() {
                    @Override
                    public Member call(ApiResponse o) {
                        Address a = o.getAddresses().get(0);
                        Member member = setAddressToChef(a);
                        return member;
                    }
                })
                .flatMap(new Func1<Member, Observable<ApiResponse>>() {
                    @Override
                    public Observable<ApiResponse> call(Member member) {
                        if (member.getAvatar() != null) {
                            Observable observable = App.getRestClient()
                                    .getPageService()
                                    .getImage(Integer.parseInt(member.getAvatar()));
                            return observable;
                        } else {
                            Image image = new Image();
                            image.setFilename(Constants.DEFAULT_AVATAR_CHEF);
                            ApiResponse response = new ApiResponse();
                            response.setImage(image);
                            return Observable.just(response);
                        }

                    }
                })
                .map(new Func1<ApiResponse, Member>() {
                    @Override
                    public Member call(ApiResponse apiResponse) {
                        Image image = new Image();
                        if (apiResponse.getImages() != null) {
                            image = apiResponse.getImages().get(0);
                        } else if (apiResponse.getImage() != null) {
                            image = apiResponse.getImage();
                        }
                        Member member = setAvatarToChef(image);
                        return member;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new GetAvatarMemberOnNext(), new ErrorHandler(), new GetAvatarMemberAction(this));

    }

    private Member setAddressToChef(Address address){
        for(Member member : mMenuChefList){
            if(member.getDefaultShippingAddress() != null){
                if(Integer.valueOf(member.getDefaultShippingAddress()) == address.getId()){
                    member.setAddress(address);
                    return member;
                }
            }
        }
        return null;
    }

    private Member setAvatarToChef(Image image){
        for (Member member : mMenuChefList) {
            if(member.getAvatar() != null){
                if (Integer.valueOf(member.getAvatar()) == image.getId()) {
                    member.setAvatarFilename(image.getFilename());
                    return member;
                }
            }else{
                member.setAvatarFilename(Constants.DEFAULT_AVATAR_CHEF);
                return member;
            }
        }
        return null;
    }

    public void onProductCategoryComplete(){
        showProgress(false);
        mFragment.setMenuList(mMenuList);
        mFragment.onProductCategoriesComplete();
    }

    public void onMemberComplete(){
        showProgress(false);
        mFragment.setMenuChefList(mMenuChefList);
        mFragment.onMemberComplete();
    }

    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
            if (drawerItem instanceof Nameable) {
                String selected = MenuPrincipalActivity.this.getString(((Nameable) drawerItem).getNameRes());
                switch (selected.toLowerCase()){
                    case "home" :
                        showProgress(true);
                        createProductCategories();
                        break;
                    case "chefs" :
                        showProgress(true);
                        createChefs();
                        break;
                    case "help" :
                        Intent intent = new Intent(MenuPrincipalActivity.this, InitHelpActivity.class);
                        Bundle extras = new Bundle();
                        extras.putString("from", "menuPrincipal");
                        intent.putExtras(extras);
                        startActivity(intent);
                        break;
                }
            }

        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        if(mSelctedView == -1){
            createProductCategories();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_menu_principal, menu);
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
