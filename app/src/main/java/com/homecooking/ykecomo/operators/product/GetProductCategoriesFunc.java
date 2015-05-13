package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.MenuPrincipalActivity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 14/03/15.
 */
public class GetProductCategoriesFunc implements Func1<ApiResponse, Observable<ProductCategory>> {

    private MenuPrincipalActivity mActivity;

    public GetProductCategoriesFunc(MenuPrincipalActivity activity){ this.mActivity = activity; }

    @Override
    public Observable<ProductCategory> call(ApiResponse apiResponse) {
        this.mActivity.setMenuList(apiResponse.getProductCategories());
        return Observable.from(apiResponse.getProductCategories());
    }
}
