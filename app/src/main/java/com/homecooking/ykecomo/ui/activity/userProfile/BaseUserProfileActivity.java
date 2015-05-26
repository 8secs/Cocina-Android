package com.homecooking.ykecomo.ui.activity.userProfile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.member.SetViewProfileAction;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.operators.image.FillMemberFunc;
import com.homecooking.ykecomo.operators.image.GetImageFromIntegerFunc;
import com.homecooking.ykecomo.operators.image.SetImageFunc;
import com.homecooking.ykecomo.operators.image.UpdateAvatarMemberFunc;
import com.homecooking.ykecomo.operators.member.GetMemberFunc;
import com.homecooking.ykecomo.operators.member.SetAvatarAddressToMemberFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.image.BaseImageUpload;
import com.homecooking.ykecomo.ui.activity.image.NewPhoto;
import com.soundcloud.android.crop.Crop;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BaseUserProfileActivity extends BaseImageUpload {

    protected Observable<ApiResponse> mUserObservable;

    public Subscription getUserSubscription() {
        return mUserSubscription;
    }

    protected Subscription mUserSubscription;

    protected Address mAddress;
    protected Member mMember;

    protected ImageView mHeaderView;
    protected TextView mUsernameTxt;
    protected TextView mDescriptionTxt;
    protected TextView mEmailTxt;
    protected TextView mFacebookTxt;
    protected TextView mUserAddressTxt;
    protected TextView mEmailVerified;

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

    @Override
    protected void uploadImage(){
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
}
