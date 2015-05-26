package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.operators.image.GetImageFromIntegerFunc;
import com.homecooking.ykecomo.operators.image.SetImageFunc;
import com.homecooking.ykecomo.ui.activity.image.BaseImageUpload;
import com.homecooking.ykecomo.ui.activity.image.NewPhoto;
import com.soundcloud.android.crop.Crop;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mExtras = getIntent().getExtras();

        setContentView(R.layout.edit_image_layout);
        setTitle(R.string.add_image_product);

        mAvatarView = (ImageView) findViewById(R.id.avatar);
        mNext = (Button) findViewById(R.id.next);
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
