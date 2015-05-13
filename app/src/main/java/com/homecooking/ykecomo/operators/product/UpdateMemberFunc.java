package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 16/3/15.
 */
public class UpdateMemberFunc implements Func1<Member, Observable<Product>> {

    private ProductsActivity mActivity;

    public UpdateMemberFunc(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<Product> call(Member member) {
        updateMembers(member);
        return Observable.from(this.mActivity.getProducts());
    }

    private void updateMembers(Member member){
        for(int i = 0; i < this.mActivity.getMembers().size(); i++){
            if(this.mActivity.getMembers().get(i).getId() == member.getId()){
                this.mActivity.getMembers().set(i, member);
            }
        }
    }
}
