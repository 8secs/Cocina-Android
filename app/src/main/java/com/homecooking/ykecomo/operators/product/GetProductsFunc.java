package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.Observable;
import rx.functions.Func1;

public class GetProductsFunc implements Func1<ApiResponse, Observable<Product>> {

    private ProductsActivity mActivity;

    public GetProductsFunc(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<Product> call(ApiResponse apiResponse) {
        if (apiResponse != null){
            this.mActivity.setImages(apiResponse.getImages());
            this.mActivity.setProducts(apiResponse.getProducts());
        }
        return Observable.from(apiResponse.getProducts());
    }
}
