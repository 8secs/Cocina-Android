package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 15/03/15.
 */
public class SetImageMemberProductFunc implements Func1<Product, Observable<Product>> {

    private ProductDetailActivity mActivity;

    public SetImageMemberProductFunc(ProductDetailActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<Product> call(Product product) {
        product.setImgUrl(this.mActivity.getImages().get(0).getFilename());
        product.setMember(this.mActivity.getMembers().get(0));
        return Observable.just(product);
    }
}
