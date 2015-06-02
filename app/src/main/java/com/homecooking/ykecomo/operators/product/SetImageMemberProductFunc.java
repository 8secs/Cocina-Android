package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;
import com.homecooking.ykecomo.ui.activity.chefZone.EditProductChefActivity;

import rx.Observable;
import rx.functions.Func1;

public class SetImageMemberProductFunc implements Func1<Product, Observable<Product>> {

    private ProductDetailActivity mActivity;
    private EditProductChefActivity mEditActivity;

    public SetImageMemberProductFunc(ProductDetailActivity activity) { this.mActivity = activity; }

    public SetImageMemberProductFunc(EditProductChefActivity activity) { this.mEditActivity = activity; }

    @Override
    public Observable<Product> call(Product product) {
        if(mActivity != null){
            product.setImgUrl(this.mActivity.getImages().get(0).getFilename());
            product.setMember(this.mActivity.getMembers().get(0));
        }else{
            product.setImgUrl(this.mEditActivity.getImages().get(0).getFilename());
            product.setMember(this.mEditActivity.getMembers().get(0));
        }
        return Observable.just(product);
    }
}
