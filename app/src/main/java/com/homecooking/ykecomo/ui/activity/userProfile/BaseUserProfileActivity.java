package com.homecooking.ykecomo.ui.activity.userProfile;

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
import android.widget.TextView;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.member.SetViewProfileAction;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.operators.image.FillMemberFunc;
import com.homecooking.ykecomo.operators.image.GetImageFromIntegerFunc;
import com.homecooking.ykecomo.operators.image.SetImageFunc;
import com.homecooking.ykecomo.operators.image.UpdateAvatarMemberFunc;
import com.homecooking.ykecomo.operators.member.GetMemberFunc;
import com.homecooking.ykecomo.operators.member.SetAvatarAddressToMemberFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.image.NewPhoto;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit.mime.TypedFile;
import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BaseUserProfileActivity extends AppCompatActivity {

    private static int REQUEST_CAMERA = 0;
    private static int SELECT_FILE = 1;

    private TypedFile mTypeFile;
    private String mImageURL;
    private Bitmap mBitmap;
    protected Picasso mPicasso;

    protected Observable<ApiResponse> mUserObservable;

    public Subscription getUserSubscription() {
        return mUserSubscription;
    }

    protected Subscription mUserSubscription;

    protected Image mAvatar;
    protected Address mAddress;
    protected Member mMember;

    protected ImageView mAvatarView;
    protected ImageView mHeaderView;
    protected TextView mUsernameTxt;
    protected TextView mDescriptionTxt;
    protected TextView mEmailTxt;
    protected TextView mFacebookTxt;
    protected TextView mUserAddressTxt;
    protected TextView mEmailVerified;

    public Picasso getPicasso() {
        return mPicasso;
    }

    public void setPicasso(Picasso mPicasso) {
        this.mPicasso = mPicasso;
    }

    public Image getAvatar() {
        return mAvatar;
    }

    public void setAvatar(Image mAvatar) {
        this.mAvatar = mAvatar;
    }

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address mAddress) {
        this.mAddress = mAddress;
    }

    public Member getMember() {
        return mMember;
    }

    public void setMember(Member mMember) {
        this.mMember = mMember;
    }

    public ImageView getHeaderView() {
        return mHeaderView;
    }

    public ImageView getAvatarView() {
        return mAvatarView;
    }

    public void setAvatarView(ImageView view){ this.mAvatarView = view; }

    public TextView getUsernameTxt() {
        return mUsernameTxt;
    }


    public TextView getDescriptionTxt() {
        return mDescriptionTxt;
    }

    public TextView getEmailTxt() {
        return mEmailTxt;
    }

    public TextView getFacebookTxt() {
        return mFacebookTxt;
    }

    public TextView getUserAddressTxt() {
        return mUserAddressTxt;
    }

    public TextView getEmailVerified() {
        return mEmailVerified;
    }

    protected void createObservable(int type ){
        if(type == Constants.VIEW_PROFILE_OBSERVABLE_TYPE){
            if(!App.isIsFbMember()) mUserObservable = App.getRestClient().getPageService().getMember(Integer.toString(App.getMember().getId()));
            else mUserObservable = App.getRestClient().getPageService().getFbMember(App.getMember().getFacebookUID());
            mUserSubscription = AndroidObservable.bindActivity(this, mUserObservable)
                    .flatMap(new GetMemberFunc(this))
                    .flatMap(new SetAvatarAddressToMemberFunc(this))
                    .subscribe(new SetViewProfileAction(this));
        }
    }

    public void setImage(String url, ImageView view){

        Picasso.with(BaseUserProfileActivity.this)
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
                .flatMap(new UpdateAvatarMemberFunc(this))
                .map(new FillMemberFunc(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Member>() {
                    @Override
                    public void call(Member member) {
                        setMember(member);
                        App.setMember(getMember());
                        mImageURL = Constants.BASE_URL.concat(getMember().getAvatarFilename());
                        App.setMemberPrefs();
                    }
                });
    }

    public void selectImage(){
        final CharSequence[] items = {getResources().getString(R.string.HACER_PHOTO),
                getResources().getString(R.string.SELECCIONAR_DESDE_LIBRERIA),
                getResources().getString(R.string.cancelled) };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.change_image));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getResources().getString(R.string.HACER_PHOTO))) {

                    Intent i = new Intent(BaseUserProfileActivity.this, NewPhoto.class);
                    i.putExtra(Constants.IS_FRONT_CAMERA, true);
                    startActivityForResult(i, REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.SELECCIONAR_DESDE_LIBRERIA))) {
                    Crop.pickImage(BaseUserProfileActivity.this);
                } else if (items[item].equals(getResources().getString(R.string.cancelled))) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == REQUEST_CAMERA) {
                mBitmap = App.decodeBitmapFromFile(data.getExtras().getString("filename"), 1024, 1024);
                Uri uri = getImageUri(BaseUserProfileActivity.this, mBitmap);
                beginCrop(uri);
            }else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
                beginCrop(data.getData());
            }else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        }else{
            //showAlertDialog(BaseUserProfileActivity.this, "ERROR", "Hemos tenido problemas al guardar la imagen.", false);
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
