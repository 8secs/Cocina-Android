package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.rest.model.ApiResponse;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 14/03/15.
 */
public class GetImageObservableProductCategoryFunc implements Func1<ProductCategory, Observable<ApiResponse>> {

    @Override
    public Observable<ApiResponse> call(ProductCategory productCategory) {
        Observable<ApiResponse> observable = App.getRestClient()
                .getPageService()
                .getImage(Integer.valueOf(productCategory.getPageImage()));
        return observable;
    }
}
