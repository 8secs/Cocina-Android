package com.homecooking.ykecomo.operators.image;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import rx.functions.Func1;

/**
 * Created by andres on 28/03/15.
 */
public class FillMemberFunc implements Func1<ApiResponse, Member> {

    private BaseUserProfileActivity mActivity;

    public FillMemberFunc (BaseUserProfileActivity activity) { this.mActivity = activity; }

    @Override
    public Member call(ApiResponse apiResponse) {
        Member member = apiResponse.getMember();
        member.setAddressStr(App.getPref().getString(Constants.MEMBER_ADDRESS, ""));
        member.setAvatarFilename(this.mActivity.getAvatar().getFilename());
        return member;
    }
}
