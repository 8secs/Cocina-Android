package com.homecooking.ykecomo.actions.product;

import com.homecooking.ykecomo.ui.activity.MenuPrincipalActivity;

import rx.functions.Action0;

/**
 * Created by andres on 14/03/15.
 */
public class GetProductCategoryAction implements Action0 {

    private MenuPrincipalActivity mActivity;

    public GetProductCategoryAction(MenuPrincipalActivity activity) { this.mActivity = activity; }

    @Override
    public void call() {
        this.mActivity.onProductCategoryComplete();
    }
}
