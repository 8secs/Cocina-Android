package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.functions.Func1;

/**
 * Created by andres on 15/03/15.
 */
public class SetMemberToProductFunc implements Func1<Product, Product> {

    private ProductsActivity mActivity;

    public SetMemberToProductFunc(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public Product call(Product product) {
        return setMemberToProduct(product);
    }

    private Product setMemberToProduct(Product product){
        for(Member member : this.mActivity.getMembers()){
            if(product.getChef().equals(Integer.toString(member.getId()))){
                product.setMember(member);
                this.mActivity.updateProducts(product);
                return product;
            }
        }
        return null;
    }
}
