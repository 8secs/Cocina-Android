package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 17/03/15.
 */
public class GetMemberFunc implements Func1<ApiResponse, Observable<Member>> {

    private BaseUserProfileActivity mActivity;

    public GetMemberFunc(BaseUserProfileActivity activity){ this.mActivity = activity; }

    @Override
    public Observable<Member> call(ApiResponse apiResponse) {
        if(apiResponse.getImages() != null) mActivity.setAvatar(apiResponse.getImages().get(0));
        if(apiResponse.getAddresses() != null) {
            if(apiResponse.getAddresses().size() > 0) mActivity.setAddress(apiResponse.getAddresses().get(0));
        }
        return Observable.from(apiResponse.getMembers());
    }
}
