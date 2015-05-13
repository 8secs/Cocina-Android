package com.homecooking.ykecomo.actions.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.functions.Action1;

/**
 * Created by andres on 15/03/15.
 */
public class GetProductOnNext implements Action1<Product> {

    private ProductsActivity mActivity;

    public GetProductOnNext(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public void call(Product product) {
        //Log.e("getProduct ", product.getID() + " " + this.mActivity.getProducts().indexOf(product));
        //Log.e("getProduct", "member " + product.getMember().getFirstName());
    }

}
