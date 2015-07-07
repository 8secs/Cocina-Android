package com.homecooking.ykecomo.actions.member;

import android.text.Html;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.app.Utility;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import rx.functions.Action1;

public class SetViewProfileAction implements Action1<Member> {

    private BaseUserProfileActivity mActivity;

    public SetViewProfileAction(BaseUserProfileActivity activity) { this.mActivity = activity; }

    @Override
    public void call(Member member) {
        String url = "";
        if (!App.isIsFbMember()) {
            url = Constants.BASE_URL.concat(member.getAvatarFilename());
            this.mActivity.getFacebookTxt().setText(this.mActivity.getResources().getString(R.string.no_tenemos_facebook));
            if (member.getVerified().equals("1"))
                this.mActivity.getEmailVerified().setText(this.mActivity.getResources().getString(R.string.email_verified));
            else
                this.mActivity.getEmailVerified().setText(this.mActivity.getResources().getString(R.string.email_no_verified));

            if (member.getAddress() != null) {
                this.mActivity.getUserAddressTxt().setText(Utility.getMemberAddressStr(member));
            }

        } else {
            url = Constants.GRAPH_FB_URL + member.getFacebookUID() + Constants.PICTURE_FB_URL_PARAMS;
            this.mActivity.getFacebookTxt().setText(this.mActivity.getResources().getString(R.string.registro_en_facebook));
            this.mActivity.getEmailVerified().setText(this.mActivity.getResources().getString(R.string.email_verified));
            if (member.getAddress() != null) {
                this.mActivity.getUserAddressTxt().setText(Utility.getMemberAddressStr(member));
            } else {
                this.mActivity.getUserAddressTxt().setText(App.getFbLocation());
            }
        }

        this.mActivity.setImage(url, this.mActivity.getHeaderView());
        this.mActivity.getUsernameTxt().setText(member.getFirstName() + " " + member.getSurname());

        if (member.getDescription() != null)
            this.mActivity.getDescriptionTxt().setText(Html.fromHtml(member.getDescription()));
        else
            this.mActivity.getDescriptionTxt().setText(this.mActivity.getResources().getString(R.string.no_description_profile));

        this.mActivity.getEmailTxt().setText(member.getEmail());
    }
}
