package com.homecooking.ykecomo.actions.member;


import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.LoginActivity;

import rx.functions.Action1;

/**
 * Created by andres on 14/03/15.
 */
public class LoginMemberAction implements Action1<ApiResponse> {

    private LoginActivity mActivity;

    public LoginMemberAction(LoginActivity activity){
        this.mActivity = activity;
    }

    @Override
    public void call(ApiResponse apiResponse) {
        this.mActivity.setMembers(apiResponse.getMembers());
        this.mActivity.setImages(apiResponse.getImages());
        this.mActivity.setAddresses(apiResponse.getAddresses());
        this.mActivity.setupUser();
    }
}
