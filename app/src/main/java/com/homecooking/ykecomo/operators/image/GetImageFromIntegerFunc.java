package com.homecooking.ykecomo.operators.image;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.rest.model.ApiResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 28/03/15.
 */
public class GetImageFromIntegerFunc implements Func1<Integer, Observable<ApiResponse>> {

    @Override
    public Observable<ApiResponse> call(Integer integer) {
        Observable<ApiResponse> observable = App.getRestClient()
                .getPageService()
                .getImage(integer);
        return observable;
    }

}
