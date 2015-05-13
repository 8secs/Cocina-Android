package com.homecooking.ykecomo.actions.member;

import android.widget.Toast;

import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.SignupActivity;

import rx.functions.Action1;

/**
 * Created by andres on 14/03/15.
 */
public class CreateMemberAction implements Action1<ApiResponse> {

    private SignupActivity mActivity;

    public CreateMemberAction(SignupActivity activity) { this.mActivity = activity; }

    @Override
    public void call(ApiResponse apiResponse) {
        if (apiResponse.getMembers() != null) {
            Toast.makeText(this.mActivity, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }else {
            this.mActivity.updateUI(apiResponse.getMember());
        }
    }
}
