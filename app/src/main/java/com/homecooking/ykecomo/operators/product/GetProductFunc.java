package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;

import rx.Observable;
import rx.functions.Func1;

public class GetProductFunc implements Func1<ApiResponse, Observable<Product>> {

    private ProductDetailActivity mActivity;

    public GetProductFunc(ProductDetailActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<Product> call(ApiResponse apiResponse) {
        this.mActivity.setImages(apiResponse.getImages());
        this.mActivity.setMembers(apiResponse.getMembers());
        return Observable.from(apiResponse.getProducts());
    }
}
