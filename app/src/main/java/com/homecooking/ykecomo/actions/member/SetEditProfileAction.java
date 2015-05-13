package com.homecooking.ykecomo.actions.member;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import rx.functions.Action1;

public class SetEditProfileAction implements Action1<Member> {

    private BaseUserProfileActivity mActivity;

    public SetEditProfileAction(BaseUserProfileActivity activity) { this.mActivity = activity; }

    @Override
    public void call(Member member) {
        String url;
        if(!App.isIsFbMember()){
            url = Constants.BASE_URL.concat(member.getAvatarFilename());
        }else{
            url = Constants.GRAPH_FB_URL + member.getFacebookUID() + Constants.PICTURE_FB_URL_PARAMS;
        }
        this.mActivity.setImage(url, this.mActivity.getAvatarView());

    }
}
