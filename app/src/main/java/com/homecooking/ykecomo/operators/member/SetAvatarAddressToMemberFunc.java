package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import rx.Observable;
import rx.functions.Func1;

public class SetAvatarAddressToMemberFunc implements Func1<Member, Observable<Member>> {

    private BaseUserProfileActivity mActivity;

    public SetAvatarAddressToMemberFunc(BaseUserProfileActivity activity){ this.mActivity = activity; }

    @Override
    public Observable<Member> call(Member member) {
        if(!App.isIsFbMember()){
            member.setAvatarFilename(mActivity.getAvatar().getFilename());
            member.setAddress(mActivity.getAddress());
            member.setAddressStr(App.getMemberAddressStr(member));
        }
        return Observable.just(member);
    }
}
