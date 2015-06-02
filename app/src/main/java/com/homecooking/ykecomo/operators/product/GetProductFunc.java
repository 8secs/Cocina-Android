package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;
import com.homecooking.ykecomo.ui.activity.chefZone.EditProductChefActivity;

import rx.Observable;
import rx.functions.Func1;

public class GetProductFunc implements Func1<ApiResponse, Observable<Product>> {

    private ProductDetailActivity mActivity;
    private EditProductChefActivity mEditActivity;

    public GetProductFunc(ProductDetailActivity activity) { this.mActivity = activity; }

    public GetProductFunc(EditProductChefActivity activity) { this.mEditActivity = activity; }

    @Override
    public Observable<Product> call(ApiResponse apiResponse) {
        if(this.mActivity != null){
            this.mActivity.setImages(apiResponse.getImages());
            this.mActivity.setMembers(apiResponse.getMembers());
        }else{
            this.mEditActivity.setImages(apiResponse.getImages());
            this.mEditActivity.setMembers(apiResponse.getMembers());
        }

        return Observable.from(apiResponse.getProducts());
    }
}
