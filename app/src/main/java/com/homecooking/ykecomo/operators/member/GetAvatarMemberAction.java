package com.homecooking.ykecomo.operators.member;


import com.homecooking.ykecomo.ui.activity.MenuPrincipalActivity;

import rx.functions.Action0;

public class GetAvatarMemberAction implements Action0 {

    private MenuPrincipalActivity mActivity;

    public GetAvatarMemberAction(MenuPrincipalActivity activity) { this.mActivity = activity; }

    @Override
    public void call() {

        this.mActivity.onMemberComplete();
    }
}
