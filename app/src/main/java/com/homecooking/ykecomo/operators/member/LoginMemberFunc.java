package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.model.Auth;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.LoginActivity;

import rx.Observable;
import rx.functions.Func1;

public class LoginMemberFunc implements Func1<Auth, Observable<ApiResponse>> {

    private LoginActivity mActivity;

    public LoginMemberFunc(LoginActivity activity){
        this.mActivity = activity;
    }

    @Override
    public Observable<ApiResponse> call(Auth auth) {
        this.mActivity.setmAuth(auth);
        Observable<ApiResponse> member = Observable.empty();
        if (auth.getResult() == true) {
            member = App.getRestClient()
                    .getPageService()
                    .getMember(Integer.toString(auth.getUserID()));
        }
        return member;
    }
}
