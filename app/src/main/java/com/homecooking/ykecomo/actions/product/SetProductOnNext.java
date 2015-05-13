package com.homecooking.ykecomo.actions.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;

import rx.functions.Action1;


public class SetProductOnNext implements Action1<Product> {

    private ProductDetailActivity mActivity;

    public SetProductOnNext(ProductDetailActivity activity) { this.mActivity = activity; }

    @Override
    public void call(Product product) {
        this.mActivity.setProduct(product);
        this.mActivity.updateUI();
    }
}
