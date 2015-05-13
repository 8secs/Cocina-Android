package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.rest.model.ApiResponse;

import java.util.ArrayList;
import java.util.Hashtable;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 14/03/15.
 */
public class LoginFbMemberFunc implements Func1<ApiResponse, Observable<ApiResponse>> {
    private ArrayList<Hashtable<String, String>> mParams;

    public LoginFbMemberFunc(ArrayList<Hashtable<String, String>> params){
        this.mParams = params;
    }

    @Override
    public Observable<ApiResponse> call(ApiResponse apiResponse) {
        ArrayList<Member> members = apiResponse.getMembers();
        if(members.size() == 0){
            Observable<ApiResponse> observable = App.getRestClient().getPageService().createMember(this.mParams);
            return observable;
        }
        return Observable.just(apiResponse);
    }
}
