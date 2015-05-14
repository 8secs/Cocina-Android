package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.MenuPrincipalActivity;
import com.homecooking.ykecomo.ui.fragment.MenuFragment;

import rx.Observable;
import rx.functions.Func1;

public class GetProductCategoriesFunc implements Func1<ApiResponse, Observable<ProductCategory>> {

    private MenuPrincipalActivity mActivity;
    private MenuFragment mFragment;

    public GetProductCategoriesFunc(MenuPrincipalActivity activity){ this.mActivity = activity; }

    @Override
    public Observable<ProductCategory> call(ApiResponse apiResponse) {
        this.mActivity.setMenuList(apiResponse.getProductCategories());
        return Observable.from(apiResponse.getProductCategories());
    }
}
