package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.operators.image.GetImageFromIntegerFunc;
import com.homecooking.ykecomo.operators.image.SetImageFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.image.BaseImageUpload;
import com.homecooking.ykecomo.ui.activity.image.NewPhoto;
import com.soundcloud.android.crop.Crop;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by: andres
 * User: andres
 * Date: 26/05/15
 * Time: 13 : 41
 */
public class EditImageProductChef extends BaseImageUpload {

    Button mNext;
    Bundle mExtras;
    Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mExtras = getIntent().getExtras();

        setContentView(R.layout.edit_image_layout);
        setTitle(R.string.add_image_product);

        mAvatarView = (ImageView) findViewById(R.id.avatar);
        mNext = (Button) findViewById(R.id.next);

        if(mExtras != null){
            if(mExtras.getInt(Constants.PRODUCT_ITEMS) == Constants.EDIT_PRODUCT_ITEM){
                mNext.setVisibility(View.GONE);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.done_pref_btn));
                getSupportActionBar().setTitle(getResources().getString(R.string.DONE));
                if(mExtras.getString(Constants.IMAGE) != null){
                    this.setImage(mExtras.getString(Constants.IMAGE), mAvatarView);
                }
            }else{
                mNext.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createProduct();
                    }
                });
            }
        }
    }

    protected void createProduct(){
        App.getRestClient()
                .getPageService()
                .createProduct(setParams())
                .subscribe(new Action1<ApiResponse>() {
                    @Override
                    public void call(ApiResponse apiResponse) {
                        Log.e("result", apiResponse.getProducts().toString());
                    }
                });

    }

    protected ArrayList<Map<String, Object>> setParams(){

        mProduct = new Product();

        mProduct.setMember(App.getMember());
        mProduct.setTitle(mExtras.getString(Constants.TITLE));
        mProduct.setModel(mExtras.getString(Constants.TITLE));
        mProduct.setPortions(mExtras.getString(Constants.PORTIONS));
        mProduct.setBasePrice(mExtras.getString(Constants.PRICE));
        mProduct.setMiniDescription(mExtras.getString(Constants.MINI_DESCRIPTION));
        mProduct.setContent(mExtras.getString(Constants.CONTENT));
        mProduct.setParentID(mExtras.getString(Constants.PARENT));
        mProduct.setImage(Integer.toString(mAvatar.getId()));
        mProduct.setAllowPurchase("1");

        ArrayList<Map<String, Object>> params = new ArrayList<Map<String, Object>>();

        Map<String, Object> hasmap = new Hashtable<String, Object>();
        hasmap.put(Constants.CHEF_ID, mProduct.getMember().getId());
        hasmap.put(Constants.TITLE, mProduct.getTitle());
        hasmap.put(Constants.MODEL, mProduct.getModel());
        hasmap.put(Constants.PORTIONS, mProduct.getPortions());
        hasmap.put(Constants.PRICE, mProduct.getBasePrice());
        hasmap.put(Constants.MINI_DESCRIPTION, mProduct.getMiniDescription());
        hasmap.put(Constants.CONTENT, mProduct.getContent());
        hasmap.put(Constants.PARENT, Integer.parseInt(mProduct.getParentID()));
        hasmap.put(Constants.IMAGE_ID, mAvatar.getId());
        hasmap.put(Constants.ALLOW_PURCHASE, mProduct.getAllowPurchase());

        params.add(hasmap);

        return params;
    }

    protected void update(){

        Intent i = new Intent();
        mExtras.putInt(Constants.IMAGE_ID, mAvatar.getId());
        mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
        mExtras.putInt(Constants.COLUMN_PRODUCT_ITEMS, Constants.IMAGE_COLUMN_ITEM);
        i.putExtras(mExtras);
        setResult(RESULT_OK, i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_product_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.new_photo:
                Intent i = new Intent(EditImageProductChef.this, NewPhoto.class);
                i.putExtra(Constants.IS_FRONT_CAMERA, false);
                startActivityForResult(i, REQUEST_CAMERA);
                break;
            case R.id.select_photo:
                Crop.pickImage(EditImageProductChef.this);
                break;
            case android.R.id.home:
                update();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void uploadImage(){
        App.getRestClient()
                .getPageService()
                .uploadImage(mTypeFile)
                .flatMap(new GetImageFromIntegerFunc())
                .map(new SetImageFunc(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Image>() {
                    @Override
                    public void call(Image image) {
                        mImageURL = Constants.BASE_URL.concat(image.getFilename());
                        setImage(mImageURL, mAvatarView);
                    }
                });
    }

}
