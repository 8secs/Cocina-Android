package com.homecooking.ykecomo.ui.activity.image;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.app.Utility;
import com.homecooking.ykecomo.model.Image;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit.mime.TypedFile;

/**
 * Created by: andres
 * User: andres
 * Date: 26/05/15
 * Time: 18 : 38
 */
public class BaseImageUpload extends AppCompatActivity {

    protected static int REQUEST_CAMERA = 0;
    protected static int SELECT_FILE = 1;

    protected TypedFile mTypeFile;
    protected String mImageURL;
    protected Bitmap mBitmap;
    protected ImageView mAvatarView;

    protected Image mAvatar;
    public Image getAvatar() { return mAvatar; }
    public void setAvatar(Image mAvatar) { this.mAvatar = mAvatar; }

    protected Picasso mPicasso;
    public Picasso getPicasso() {
        return mPicasso;
    }
    public void setPicasso(Picasso mPicasso) {
        this.mPicasso = mPicasso;
    }

    public void setImage(String url, ImageView view){

        Picasso.with(BaseImageUpload.this)
                .load(url)
                .error(android.R.drawable.stat_notify_error)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(view);
    }

    protected void uploadImage(){ }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                mBitmap = Utility.decodeBitmapFromFile(data.getExtras().getString("filename"), 1024, 1024);
                Uri uri = getImageUri(BaseImageUpload.this, mBitmap);
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

    @SuppressWarnings("deprecation")
    public void showAlertDialog(Context context, String title, String message, Boolean status) {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.mipmap.ic_launcher);
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertDialog.show();
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
