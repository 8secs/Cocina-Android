package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by andres on 15/03/15.
 */
public class GetMembersAvatarAddressFunc implements Func1<ApiResponse, Observable<Member>> {

    private ProductsActivity mActivity;

    public GetMembersAvatarAddressFunc(ProductsActivity activity) { this.mActivity = activity; }

    @Override
    public Observable<Member> call(ApiResponse apiResponse) {
        this.mActivity.setAddresses(apiResponse.getAddresses());
        this.mActivity.setAvatars(apiResponse.getImages());
        this.mActivity.setMembers(apiResponse.getMembers());
        return Observable.from(apiResponse.getMembers());
    }
}
