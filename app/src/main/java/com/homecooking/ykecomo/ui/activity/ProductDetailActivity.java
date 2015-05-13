package com.homecooking.ykecomo.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.product.SetProductOnNext;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.operators.product.GetProductFunc;
import com.homecooking.ykecomo.operators.product.SetImageMemberProductFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ProductDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {
    
    private Product mProduct;
    private String mAvatarName;
    
    private ObservableScrollView mScrollView;
    private TextView mProductTitle;
    private ImageView mProductImageView;
    private TextView mProductPrice;
    private TextView mProductDescription;
    
    private ImageView mAvatarChef;
    private TextView mChefName;
    private TextView mChefSlogan;
    private TextView mChefAddress;

    protected CircleProgressBar mProgress;
    private ImageView mActiveImageView;
    private String mAddress;
    
    
    private Observable<ApiResponse> mProductObservable;
    private Subscription mProductSubscription;

    private ArrayList<Image> mImages;
    private ArrayList<Member> mMembers;

    public void setProduct(Product mProduct) { this.mProduct = mProduct; }
    public ArrayList<Member> getMembers() { return mMembers; }
    public void setMembers(ArrayList<Member> mMembers) { this.mMembers = mMembers; }
    public ArrayList<Image> getImages() { return mImages; }
    public void setImages(ArrayList<Image> mImages) { this.mImages = mImages; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(Constants.CARGANDO_DATOS);

        setContentView(R.layout.activity_product_detail);

        mScrollView = (ObservableScrollView) findViewById(R.id.detail_scrollview);
        mProgress = (CircleProgressBar) findViewById(R.id.progressBar);

        mProductTitle = (TextView) findViewById(R.id.product_title);
        mProductPrice = (TextView) findViewById(R.id.price_product);
        mProductDescription = (TextView) findViewById(R.id.product_description);
        mProductImageView = (ImageView) findViewById(R.id.product_image);
        
        mAvatarChef = (ImageView) findViewById(R.id.avatar_chef);
        mChefName = (TextView) findViewById(R.id.chef_name);
        mChefSlogan = (TextView) findViewById(R.id.chef_slogan);
        mChefAddress = (TextView) findViewById(R.id.chef_address);
        
        Bundle bundle = getIntent().getExtras();
        int productID = bundle.getInt(Constants.PRODUCTID_BUNDLE_KEY);
        mAvatarName = bundle.getString(Constants.AVATAR_BUNDLE_KEY);
        mAddress = bundle.getString(Constants.MEMBER_ADDRESS_BUNDLE_KEY);
        mScrollView.setScrollViewCallbacks(this);
        getProductObservable(productID);
        
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupSubscription();
    }
    
    private void getProductObservable(int id){
        mProductObservable = App.getRestClient()
                .getPageService()
                .getProductDetail(id);        
    }
    
    private void setupSubscription(){

        mProductSubscription = AndroidObservable.bindActivity(this, mProductObservable)
                .flatMap(new GetProductFunc(this))
                .flatMap(new SetImageMemberProductFunc(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new SetProductOnNext(this));
    }

    public void updateUI(){

        setTitle(mProduct.getTitle());
        mProductTitle.setText(mProduct.getTitle());
        mProductPrice.setText(mProduct.getBasePrice().concat("€"));
        mProductDescription.setText(Html.fromHtml(mProduct.getContent()));

        mChefName.setText(mProduct.getMember().getFirstName().concat(mProduct.getMember().getSurname()));
        mChefSlogan.setText(Html.fromHtml(mProduct.getMember().getDescription()));
        mChefAddress.setText(mAddress);
        setAvatar(mAvatarName);
        setImage(mProduct.getImgUrl());

        mProgress.setVisibility(View.GONE);
        mScrollView.setVisibility(View.VISIBLE);
    }
    
    private void setImage(String filename){
        mActiveImageView = mProductImageView;
        Picasso.with(this)
                .load(Constants.BASE_URL.concat(filename))
                .error(android.R.drawable.stat_notify_error)
                .transform(transformation)
                .into(mProductImageView);
        
    }

    private void setAvatar(String filename){
        if(filename != null ){
            Picasso.with(this)
                    .load(Constants.BASE_URL.concat(filename))
                    .error(android.R.drawable.stat_notify_error)
                    .fit()
                    .into(mAvatarChef);
        }
    }

    Transformation transformation = new Transformation() {

        @Override
        public Bitmap transform(Bitmap source) {
            int targetWidth = mActiveImageView.getWidth();

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_product_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
