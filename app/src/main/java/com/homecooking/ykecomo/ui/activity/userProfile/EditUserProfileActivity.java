package com.homecooking.ykecomo.ui.activity.userProfile;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.operators.image.FillMemberFunc;
import com.homecooking.ykecomo.operators.image.GetImageFromIntegerFunc;
import com.homecooking.ykecomo.operators.image.SetImageFunc;
import com.homecooking.ykecomo.operators.image.UpdateAvatarMemberFunc;
import com.squareup.picasso.Picasso;

import rx.functions.Action1;

public class EditUserProfileActivity extends BaseUserProfileActivity {

    private Bitmap mBitmap;
    Button mNewAvatarBtn;

    protected Button mEditUsernameBtn;

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

    @Override
    protected void uploadImage(){
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
