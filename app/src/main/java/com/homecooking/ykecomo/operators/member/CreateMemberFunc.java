package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.rest.model.ApiResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 13/03/15.
 */
public class CreateMemberFunc implements Func1<ApiResponse, Observable<ApiResponse>> {

    @Override
    public Observable<ApiResponse> call(ApiResponse apiResponse) {
        if (apiResponse.getMessage() == null) {
            Observable<ApiResponse> observable = App.getRestClient()
                    .getPageService().createMember(apiResponse.getCreateMember());
            return observable;
        }
        return Observable.just(apiResponse);
    }
}
