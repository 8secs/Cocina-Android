package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.operators.image.GetImageFromIntegerFunc;
import com.homecooking.ykecomo.operators.image.SetImageFunc;
import com.homecooking.ykecomo.ui.activity.image.NewPhoto;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit.mime.TypedFile;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by: andres
 * User: andres
 * Date: 26/05/15
 * Time: 13 : 41
 */
public class EditImageProductChef extends AppCompatActivity {

    private static int REQUEST_CAMERA = 0;
    private static int SELECT_FILE = 1;

    private TypedFile mTypeFile;
    private String mImageURL;
    private Bitmap mBitmap;
    protected Picasso mPicasso;

    ImageView mAvatarView;
    Button mNext;

    Bundle mExtras;

    protected Image mAvatar;
    public Image getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Image mAvatar) {
        this.mAvatar = mAvatar;
    }

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

    public void setImage(String url, ImageView view){

        Picasso.with(EditImageProductChef.this)
                .load(url)
                .error(android.R.drawable.stat_notify_error)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(view);
    }

    private void uploadImage(){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                mBitmap = App.decodeBitmapFromFile(data.getExtras().getString("filename"), 1024, 1024);
                Uri uri = getImageUri(EditImageProductChef.this, mBitmap);
                beginCrop(uri);
            } else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
                beginCrop(data.getData());
            } else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        } else {
            // Nada que hacer
        }
    }

    private void beginCrop(Uri source) {
        source.getPath();
        Uri outputUri = Uri.fromFile(new File(getFilesDir(), Constants.AVATAR_BUNDLE_KEY));

        new Crop(source).output(outputUri).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if (data != null){
                Uri path = Crop.getOutput(data);
                String selectedImagePath = null;
                Cursor cursor = getContentResolver().query(path, null, null, null, null);
                if (cursor == null) {
                    selectedImagePath = path.getPath();
                } else {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    selectedImagePath = cursor.getString(idx);
                }
                mTypeFile = new TypedFile(Constants.UPLOAD_MIME_TYPE, new File(selectedImagePath));
                uploadImage();
                mAvatarView.setImageURI(Crop.getOutput(data));
            }
        }else if(resultCode == Crop.RESULT_ERROR){
            Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);

        return Uri.parse(path);
    }

}
