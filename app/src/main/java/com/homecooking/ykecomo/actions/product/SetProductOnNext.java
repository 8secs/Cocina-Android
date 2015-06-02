package com.homecooking.ykecomo.actions.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;
import com.homecooking.ykecomo.ui.activity.chefZone.EditProductChefActivity;

import rx.functions.Action1;


public class SetProductOnNext implements Action1<Product> {

    private ProductDetailActivity mActivity;
    private EditProductChefActivity mEditActivity;

    public SetProductOnNext(ProductDetailActivity activity) { this.mActivity = activity; }

    public SetProductOnNext(EditProductChefActivity activity) { this.mEditActivity = activity; }

    @Override
    public void call(Product product) {
        if(mActivity != null){
            this.mActivity.setProduct(product);
            this.mActivity.updateUI();
        }else{
            this.mEditActivity.setProduct(product);
            this.mEditActivity.updateUI();
        }
    }
}
