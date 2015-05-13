package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.rest.model.ApiResponse;

import rx.Observable;
import rx.functions.Func1;

public class GetImageProductGategoryFunc implements Func1<ApiResponse, Observable<Image>> {

    @Override
    public Observable<Image> call(ApiResponse apiResponse) {
        return Observable.from(apiResponse.getImages());
    }
}
