package com.homecooking.ykecomo.ui.activity.userProfile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.operators.image.FillMemberFunc;
import com.homecooking.ykecomo.operators.image.GetImageFromIntegerFunc;
import com.homecooking.ykecomo.operators.image.SetImageFunc;
import com.homecooking.ykecomo.operators.image.UpdateAvatarMemberFunc;
import com.homecooking.ykecomo.ui.activity.image.NewPhoto;
import com.soundcloud.android.crop.Crop;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;

import retrofit.mime.TypedFile;
import rx.functions.Action1;

public class EditUserProfileActivity extends BaseUserProfileActivity {

    private static int REQUEST_CAMERA = 0;
    private static int SELECT_FILE = 1;

    private Bitmap mBitmap;
    Button mNewAvatarBtn;

    protected Button mEditUsernameBtn;

    private TypedFile mTypeFile;
    private String mExtension;
    private String mImageURL;
    private Picasso mPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_edit_user_profile);

        mAvatarView = (ImageView) findViewById(R.id.avatar);

        mNewAvatarBtn = (Button) findViewById(R.id.new_photo_btn);
        mNewAvatarBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        mUsernameTxt = (TextView) findViewById(R.id.user_name);
        mEditUsernameBtn = (Button) findViewById(R.id.edit_user_name_btn);

        mEditUsernameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mPicasso = Picasso.with(this);

        if(App.getMember() != null){
            setMember(App.getMember());
            String url;
            if(!App.isIsFbMember()){
                url = Constants.BASE_URL.concat(App.getMember().getAvatarFilename());
            }else{
                url = Constants.GRAPH_FB_URL + App.getMember().getFacebookUID() + Constants.PICTURE_FB_URL_PARAMS;
            }
            mUsernameTxt.setText(App.getMember().getFirstName() + " " + App.getMember().getSurname());
            setImage(url, getAvatarView());
        }
    }

    private void uploadImage(){
        App.getRestClient()
                .getPageService()
                .uploadImage(mTypeFile)
                .flatMap(new GetImageFromIntegerFunc())
                .map(new SetImageFunc(this))
                .flatMap(new UpdateAvatarMemberFunc(this))
                .map(new FillMemberFunc(this))
                .subscribe(new Action1<Member>() {
                    @Override
                    public void call(Member member) {
                        setMember(member);
                        App.setMember(getMember());
                        mImageURL = Constants.BASE_URL.concat(getMember().getAvatarFilename());
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

                    Intent i = new Intent(EditUserProfileActivity.this, NewPhoto.class);
                    i.putExtra(Constants.IS_FRONT_CAMERA, true);
                    startActivityForResult(i, REQUEST_CAMERA);
                } else if (items[item].equals(getResources().getString(R.string.SELECCIONAR_DESDE_LIBRERIA))) {
                    Crop.pickImage(EditUserProfileActivity.this);
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
                Uri uri = getImageUri(EditUserProfileActivity.this, mBitmap);
                beginCrop(uri);
            }else if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
                beginCrop(data.getData());
            }else if (requestCode == Crop.REQUEST_CROP) {
                handleCrop(resultCode, data);
            }
        }else{
            showAlertDialog(EditUserProfileActivity.this, "ERROR", "Hemos tenido problemas al guardar la imagen.", false);
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_edit_user_profile, menu);
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
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}
