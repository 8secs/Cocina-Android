package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 15/03/15.
 */
public class SetImageToProductFunc implements Func1<Product, Observable<Product>> {

    private ProductsActivity mActivity;

    public SetImageToProductFunc(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<Product> call(Product product) {
        product.setImgUrl(this.mActivity.getImage(product.getImage(), this.mActivity.getImages()));
        return Observable.just(product);
    }
}
