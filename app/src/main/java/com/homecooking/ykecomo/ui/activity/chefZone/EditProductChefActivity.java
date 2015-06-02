package com.homecooking.ykecomo.ui.activity.chefZone;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.product.SetProductOnNext;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.operators.product.GetProductFunc;
import com.homecooking.ykecomo.operators.product.SetImageMemberProductFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.view.ItemEditView;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import com.manuelpeinado.fadingactionbar.extras.actionbarcompat.FadingActionBarHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class EditProductChefActivity extends AppCompatActivity {

    private int mProductID;
    private Product mProduct;
    private ArrayList<Image> mImages;
    private ArrayList<Member> mMembers;
    private ArrayList<ProductCategory> mCategories;

    protected CircleProgressBar mProgress;
    protected ItemEditView mTitleCard;
    protected ItemEditView mCategoryCard;
    protected ItemEditView mPortionCard;
    protected ItemEditView mPriceCard;
    protected ItemEditView mMiniDescriptionCard;
    protected ItemEditView mContenidoCard;
    protected RelativeLayout mHeaderView;
    protected ImageView mImage;
    protected String mImageURL;

    private Observable<ApiResponse> mProductObservable;
    private Subscription mProductSubscription;


    public void setProduct(Product mProduct) { this.mProduct = mProduct; }
    public ArrayList<Member> getMembers() { return mMembers; }
    public void setMembers(ArrayList<Member> mMembers) { this.mMembers = mMembers; }
    public ArrayList<Image> getImages() { return mImages; }
    public void setImages(ArrayList<Image> mImages) { this.mImages = mImages; }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        mProductID = extras.getInt(Constants.PRODUCTID_BUNDLE_KEY);
        getProductObservable(mProductID);
        setupUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        setupSubscription();
        getProductCategories();
    }

    protected void setupUI(){
        FadingActionBarHelper helper = new FadingActionBarHelper()
                .actionBarBackground(R.mipmap.ab_background)
                .headerLayout(R.layout.image_edit_view)
                .contentLayout(R.layout.activity_edit_product_chef)
                .lightActionBar(false);
        setContentView(helper.createView(this));
        helper.initActionBar(this);

        mHeaderView = (RelativeLayout) findViewById(R.id.header_view);
        mImage = (ImageView) findViewById(R.id.image_header);

        mTitleCard = (ItemEditView) findViewById(R.id.title_card);
        mCategoryCard = (ItemEditView) findViewById(R.id.category_card);
        mPortionCard = (ItemEditView) findViewById(R.id.portion_card);
        mPriceCard = (ItemEditView) findViewById(R.id.price_card);
        mMiniDescriptionCard = (ItemEditView) findViewById(R.id.mini_description_card);
        mContenidoCard = (ItemEditView) findViewById(R.id.content_card);

        mTitleCard.setOnClickListener(cardListener);
        mCategoryCard.setOnClickListener(cardListener);
        mPortionCard.setOnClickListener(cardListener);
        mPriceCard.setOnClickListener(cardListener);
        mMiniDescriptionCard.setOnClickListener(cardListener);
        mContenidoCard.setOnClickListener(cardListener);
        mHeaderView.setOnClickListener(cardListener);
    }

    protected void getProductObservable(int id){
        mProductObservable = App.getRestClient()
                .getPageService()
                .getProductDetail(id);
    }

    protected void setupSubscription(){

        mProductSubscription = AndroidObservable.bindActivity(this, mProductObservable)
                .flatMap(new GetProductFunc(this))
                .flatMap(new SetImageMemberProductFunc(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new SetProductOnNext(this));
    }

    protected void getProductCategories(){
        App.getRestClient()
                .getPageService()
                .getCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(ApiResponse apiResponse) {
                        if(apiResponse.getProductCategories() != null) mCategories = apiResponse.getProductCategories();
                        if(mProduct != null) setupCatetoryCard();
                    }
                });
    }

    protected void setupCatetoryCard(){
        if(mCategories != null && mCategories.size() > 0){
            for(ProductCategory category :mCategories){
                if(category.getID() == Integer.parseInt(mProduct.getParentID())){
                    mCategoryCard.setTitle(category.getTitle());
                    mCategoryCard.getFlag().setChecked(true);
                }
            }
        }
    }

    public void updateUI(){
        setTitle(mProduct.getTitle());
        if(mProduct.getTitle() != null){
            mTitleCard.setTitle(mProduct.getTitle());
            mTitleCard.getFlag().setChecked(true);
        }

        if(mProduct.getPortions() != null){
            mPortionCard.setTitle(getResources().getString(R.string.raciones)+ ": "+mProduct.getPortions());
            mPortionCard.getFlag().setChecked(true);
        }

        if(mProduct.getBasePrice() != null){
            mPriceCard.setTitle(getResources().getString(R.string.price) + ": " + mProduct.getBasePrice() + "" + getResources().getString(R.string.euro));
            mPriceCard.getFlag().setChecked(true);
        }

        if(mProduct.getMiniDescription() != null){
            mMiniDescriptionCard.setText(mProduct.getMiniDescription());
            mMiniDescriptionCard.getFlag().setChecked(true);
        }

        if(mProduct.getContent() != null){
            mContenidoCard.getTitle().setText(getResources().getString(R.string.contenido));
            String content = Html.fromHtml(mProduct.getContent()).toString();
            mContenidoCard.setText(content);
            mContenidoCard.getFlag().setChecked(true);
        }

        if(mProduct.getImgUrl() != null){
            mImageURL = Constants.BASE_URL+mProduct.getImgUrl();
            Picasso.with(this)
                    .load(mImageURL)
                    .error(android.R.drawable.stat_notify_error)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(mImage);
        }

        if(mCategories != null && mProduct != null) setupCatetoryCard();
    }

    protected void updateProduct(Map<String, Object> map){
        App.getRestClient()
                .getPageService()
                .updateProduct(mProductID, setUpadateParams(map))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(ApiResponse apiResponse) {
                        if (apiResponse.getProduct() != null) {
                            setProduct(apiResponse.getProduct());
                            if(apiResponse.getImages() != null){
                                setImages(apiResponse.getImages());
                                mProduct.setImage(Integer.toString(mImages.get(0).getId()));
                                mProduct.setImgUrl(mImages.get(0).getFilename());
                            }
                            updateUI();
                        }
                    }
                });
    }

    private ArrayList<Map<String, Object>> setUpadateParams(Map<String, Object> map){
        ArrayList<Map<String, Object>> params = new ArrayList<Map<String, Object>>();
        params.add(map);
        return params;
    }

    protected View.OnClickListener cardListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i;
            Log.e("onClickListnere", v.getTag().toString());
            switch (v.getTag().toString()){
                case Constants.TITLE:
                    i = new Intent("com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditTitleProductChefActivity");
                    i.putExtra(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
                    i.putExtra(Constants.TITLE, mTitleCard.getTitle().getText().toString());
                    startActivityForResult(i, Constants.EDIT_PRODUCT_REQUEST_CODE);
                    break;
                case Constants.CATEGORY:
                    i = new Intent("com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditProductCategorySpinnerActivity");
                    i.putExtra(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
                    i.putExtra(Constants.CATEGORY, mCategoryCard.getTitle().getText().toString());
                    i.putExtra(Constants.CATEGORIES, mCategories);
                    startActivityForResult(i, Constants.EDIT_PRODUCT_REQUEST_CODE);
                    break;
                case Constants.PORTIONS:
                    i = new Intent("com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditPortionProductChef");
                    i.putExtra(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
                    String[] portions = mPortionCard.getTitle().getText().toString().split(":");
                    i.putExtra(Constants.PORTIONS, portions[1]);
                    startActivityForResult(i, Constants.EDIT_PRODUCT_REQUEST_CODE);
                    break;
                case Constants.PRICE:
                    i = new Intent("com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditPriceProductChef");
                    i.putExtra(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
                    String[] priceStr = mPriceCard.getTitle().getText().toString().split(":");
                    String price = priceStr[1].substring(0, priceStr[1].length()-1);

                    i.putExtra(Constants.PRICE, price);
                    startActivityForResult(i, Constants.EDIT_PRODUCT_REQUEST_CODE);
                    break;
                case Constants.MINI_DESCRIPTION:
                    i = new Intent("com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditDescriptionProductChef");
                    i.putExtra(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
                    i.putExtra(Constants.MINI_DESCRIPTION, mMiniDescriptionCard.getSubtitle().getText().toString());
                    startActivityForResult(i, Constants.EDIT_PRODUCT_REQUEST_CODE);
                    break;
                case Constants.CONTENT:
                    i = new Intent("com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditContentProductChef");
                    i.putExtra(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
                    i.putExtra(Constants.CONTENT, mContenidoCard.getSubtitle().getText().toString());
                    startActivityForResult(i, Constants.EDIT_PRODUCT_REQUEST_CODE);
                    break;
            }

        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            Map<String, Object> map = new Hashtable<String, Object>();
            Bundle extras = data.getExtras();
            int type = extras.getInt(Constants.COLUMN_PRODUCT_ITEMS);

            switch (type){
                case 1:
                    map.put(Constants.TITLE, extras.getString(Constants.TITLE));
                    break;
                case 2:
                    map.put(Constants.PORTIONS, extras.getString(Constants.PORTIONS));
                    break;
                case 3:
                    map.put(Constants.PRICE, extras.getString(Constants.PRICE));
                    break;
                case 4:
                    map.put(Constants.MINI_DESCRIPTION, extras.getString(Constants.MINI_DESCRIPTION));
                    break;
                case 5:
                    map.put(Constants.CONTENT, extras.getString(Constants.CONTENT));
                    break;
                case 6:
                    map.put(Constants.PARENT_ID, extras.getString(Constants.PARENT_ID));
                    break;
                case 7:
                    map.put(Constants.IMAGE_ID, extras.getInt(Constants.IMAGE_ID));
                    break;
            }
            updateProduct(map);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_product_chef, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.edit_photo:
                Intent i = new Intent("com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditImageProductChef");
                i.putExtra(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
                i.putExtra(Constants.IMAGE, mImageURL);
                startActivityForResult(i, Constants.EDIT_PRODUCT_REQUEST_CODE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
