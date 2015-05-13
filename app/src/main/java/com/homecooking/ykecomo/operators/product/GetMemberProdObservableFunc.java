package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.Observable;
import rx.functions.Func1;

public class GetMemberProdObservableFunc implements Func1<Product, Observable<ApiResponse>> {

    private ProductsActivity mActivity;

    public GetMemberProdObservableFunc(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<ApiResponse> call(Product product) {
        this.mActivity.updateProducts(product);
        Observable<ApiResponse> members = App.getRestClient()
                .getPageService()
                .getMember(product.getChef());
        return members;
    }
}
