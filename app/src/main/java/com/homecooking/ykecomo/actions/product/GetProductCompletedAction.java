package com.homecooking.ykecomo.actions.product;


import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.functions.Action0;

/**
 * Created by andres on 15/03/15.
 */
public class GetProductCompletedAction implements Action0 {

    private ProductsActivity mActivity;

    public GetProductCompletedAction(ProductsActivity activity){ this.mActivity = activity; }

    @Override
    public void call() {
        this.mActivity.updateUI();
    }
}
