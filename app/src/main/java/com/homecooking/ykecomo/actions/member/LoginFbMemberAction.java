package com.homecooking.ykecomo.actions.member;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.LoginActivity;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by andres on 14/03/15.
 */
public class LoginFbMemberAction implements Action1<ApiResponse> {

    private LoginActivity mActivity;

    public LoginFbMemberAction(LoginActivity activity){
        this.mActivity = activity;
    }

    @Override
    public void call(ApiResponse apiResponse) {
        ArrayList<Member> members = apiResponse.getMembers();
        if (members.size() > 0) {
            Member member = members.get(0);
            member.setAddressStr(this.mActivity.user.getLocation().getName());
            App.getPref().edit().putString(Constants.MEMBER_ADDRESS, member.getAddressStr());
            if (member != null) this.mActivity.sendBackToActivity(member);
        }else{
            Member member = apiResponse.getMember();
            if(member != null){
                member.setAddressStr(this.mActivity.user.getLocation().getName());
                App.getPref().edit().putString(Constants.MEMBER_ADDRESS, member.getAddressStr());
                this.mActivity.sendBackToActivity(member);
            }
        }
    }
}
