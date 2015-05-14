package com.homecooking.ykecomo.ui.activity.chefZone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.BaseMenuActivity;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;
import com.homecooking.ykecomo.ui.adapter.ProductAdapter;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ChefMenuPrincipalActivity extends BaseMenuActivity implements AdapterView.OnItemClickListener {

    private ProductAdapter mAdapter;

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

        mSelectedMode = Constants.CHEF_ENVIROMENT_MODE;
        setupUI(savedInstanceState);
        App.setPrefUserEnvironment(Constants.CHEF_ENVIROMENT_MODE);
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

    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem drawerItem) {
            if (drawerItem instanceof Nameable) {
                String selected = ChefMenuPrincipalActivity.this.getString(((Nameable) drawerItem).getNameRes());
                Log.e("ChefMenu", selected);
                /*switch (selected.toLowerCase()){
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
                }*/
            }

        }
    };

    @Override
    protected void getProducts(){
        super.getProducts();
        Observable.from(App.getProductsChef())
                .flatMap(new Func1<Product, Observable<ApiResponse>>() {
                    @Override
                    public Observable<ApiResponse> call(Product product) {
                        product.setMember(App.getMember());
                        Observable<ApiResponse> observable = App.getRestClient()
                                .getPageService()
                                .getImage(Integer.parseInt(product.getImage()));
                        return observable;
                    }
                })
                .map(new Func1<ApiResponse, Product>() {
                    @Override
                    public Product call(ApiResponse apiResponse) {
                        Image image = apiResponse.getImages().get(0);
                        for(Product product : App.getProductsChef()){
                            if(Integer.parseInt(product.getImage()) == image.getId()){
                                product.setImgUrl(image.getFilename());
                                return product;
                            }
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Subscriber<Product>() {
                    @Override
                    public void onCompleted() {
                        Log.e("onComplet", "products");
                        updateUI();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Product product) {
                        for (int i = 0; i < App.getProductsChef().size(); i++) {
                            if (App.getProductsChef().get(i).getID() == product.getID())
                                App.getProductsChef().set(i, product);
                        }
                    }
                });
    }

    private void updateUI(){
        showProgress(false);
        if(mAdapter == null){
            mAdapter = new ProductAdapter(this);
            mAdapter.setOnItemClickListener(this);
            mList.setAdapter(mAdapter);
        }
        mAdapter.setItems(App.getProductsChef());
        mAdapter.setItemCount(App.getProductsChef().size());
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Product product = App.getProductsChef().get(position);

        Intent i = new Intent(this, ProductDetailActivity.class);
        i.putExtra(Constants.PRODUCTID_BUNDLE_KEY, product.getID());

        if(product.getMember() != null){
            i.putExtra(Constants.AVATAR_BUNDLE_KEY, product.getMember().getAvatarFilename());
            String address = product.getMember().getAddress().getCity() + "-" + product.getMember().getAddress().getCountry();
            i.putExtra(Constants.MEMBER_ADDRESS_BUNDLE_KEY, address);
        }
        startActivity(i);
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
