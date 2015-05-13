package com.homecooking.ykecomo.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.ChefReview;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.adapter.ProductChefAdapter;
import com.homecooking.ykecomo.ui.view.DividerDecoration;
import com.homecooking.ykecomo.ui.view.DynamicHeightLinearLayoutManager;
import com.manuelpeinado.fadingactionbar.extras.actionbarcompat.FadingActionBarHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ChefActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = "ChefsActity";
    protected int mChefID;
    protected Member mMember;
    protected ArrayList<Product> mProducts;
    protected ArrayList<ChefReview> mChefReviews;

    protected TextView mChefName;
    protected TextView mChefDescription;
    protected TextView mChefAddress;
    protected ImageView mAvatarView;

    protected Observable<ApiResponse> mChefObservable;
    protected Subscription mChefSubscription;

    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mList;
    private ProductChefAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        mChefID = extras.getInt(Constants.ID_BUNDLE_KEY);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Constants.CARGANDO_DATOS);

        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.mipmap.ab_background)
                .headerLayout(R.layout.header_fading_scroll)
                .contentLayout(R.layout.activity_chefs)
                .lightActionBar(false);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        mChefName = (TextView) findViewById(R.id.user_name);
        mChefDescription = (TextView) findViewById(R.id.user_description);
        mChefAddress = (TextView) findViewById(R.id.user_address);
        mAvatarView = (ImageView) findViewById(R.id.image_header);

        mList = (RecyclerView) findViewById(R.id.product_list);
        mLayoutManager = getLayoutManager();
        mList.setLayoutManager(mLayoutManager);
        mList.addItemDecoration(getItemDecoration());

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);

    }

    private void getChef(){
        mChefObservable = App.getRestClient()
                .getPageService()
                .getMember(Integer.toString(mChefID));

        mChefSubscription = AndroidObservable.bindActivity(this, mChefObservable)
                .flatMap(new Func1<ApiResponse, Observable<Product>>() {
                    @Override
                    public Observable<Product> call(ApiResponse apiResponse) {
                        mMember = apiResponse.getMembers().get(0);
                        Image avatar = apiResponse.getImages().get(0);
                        mMember.setAvatarFilename(avatar.getFilename());
                        Address address = apiResponse.getAddresses().get(0);
                        mMember.setAddress(address);
                        mProducts = apiResponse.getProducts();
                        mMember.setProductsArray(mProducts);
                        mChefReviews = apiResponse.getChefReviews();
                        mMember.setChefReviews(mChefReviews);
                        return Observable.from(mMember.getProductsArray());
                    }
                })
                .subscribe(new Subscriber<Product>() {
                    @Override
                    public void onCompleted() {

                        setupUI();
                        Log.e("product", "onComplete");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Product product) {
                        Log.e("product", "next");
                    }
                });
    }

    private void setupUI(){
        Log.e("setup", "UI");
        setAvatar();
        setTitle(getResources().getString(R.string.chef));
        mChefName.setText(mMember.getFirstName() + " " + mMember.getSurname());
        mChefDescription.setText(Html.fromHtml(mMember.getDescription()));
        mChefAddress.setText(mMember.getAddress().getCity() + " - " + mMember.getAddress().getCountry());

        getProductImage();
    }


    private void setAvatar(){

        Picasso.with(this)
                .load(Constants.BASE_URL.concat(mMember.getAvatarFilename()))
                .error(android.R.drawable.stat_notify_error)
                .placeholder(R.mipmap.ic_launcher)
                .transform(transformation)
                .into(mAvatarView);
    }

    Transformation transformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = mAvatarView.getWidth();

            double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);
            Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
            if (result != source) { source.recycle(); }
            return result;
        }

        @Override
        public String key() {
            return "transformation" + " desiredWidth";
        }
    };

    private void getProductImage(){
        Observable.from(mProducts)
                .flatMap(new Func1<Product, Observable<ApiResponse>>() {
                    @Override
                    public Observable<ApiResponse> call(Product product) {
                        Observable<ApiResponse> observable = App.getRestClient()
                                .getPageService()
                                .getImage(Integer.parseInt(product.getImage()));
                        return observable;
                    }
                })
                .map(new Func1<ApiResponse, Image>() {
                    @Override
                    public Image call(ApiResponse apiResponse) {
                        Image image = apiResponse.getImages().get(0);

                        return image;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Subscriber<Image>() {
                    @Override
                    public void onCompleted() {

                        if (mAdapter == null) {
                            mAdapter = new ProductChefAdapter(ChefActivity.this);
                            mAdapter.setOnItemClickListener(ChefActivity.this);
                            mAdapter.setItems(mProducts);
                            mAdapter.setItemCount(getDefaultItemCount());
                            mList.setAdapter(mAdapter);
                        }

                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Image image) {
                        for (Product product : mProducts) {
                            if (product.getImage().equals(Integer.toString(image.getId()))) {
                                product.setImgUrl(image.getFilename());
                                Log.e("productImage", product.getImgUrl() + " productID" + product.getID());
                            }
                        }
                    }
                });

    }

    protected int getDefaultItemCount() {
        return mProducts.size();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Product product = mProducts.get(position);

        Intent i = new Intent(this, ProductDetailActivity.class);
        i.putExtra(Constants.PRODUCTID_BUNDLE_KEY, product.getID());

        if(product.getMember() != null){
            i.putExtra(Constants.AVATAR_BUNDLE_KEY, product.getMember().getAvatarFilename());
            String address = product.getMember().getAddress().getCity() + "-" + product.getMember().getAddress().getCountry();
            i.putExtra(Constants.MEMBER_ADDRESS_BUNDLE_KEY, address);
        }

        startActivity(i);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerDecoration(this);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new DynamicHeightLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chefs, menu);
        MenuItem item = menu.findItem(R.id.action_toggle);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_toggle: {

                return true;
            }
            case R.id.action_anchor: {

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onStart(){
        super.onStart();
        getChef();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mChefSubscription.unsubscribe();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        mChefSubscription.unsubscribe();
    }
}
