package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.Observable;
import rx.functions.Func1;

public class SetAvatarAddressMemberFunc implements Func1<Member, Observable<Member>> {

    private ProductsActivity mActivity;

    public SetAvatarAddressMemberFunc(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<Member> call(Member member) {
        member.setAvatarFilename(this.mActivity.getImage(member.getAvatar(), this.mActivity.getAvatars()));
        member.setAddress(this.mActivity.getAddress(member.getDefaultShippingAddress()));
        this.mActivity.setAvatarChef(member.getAvatarFilename());
        return Observable.just(member);
    }
}
