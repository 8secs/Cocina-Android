package com.homecooking.ykecomo.operators.image;

import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import rx.functions.Func1;

/**
 * Created by andres on 28/03/15.
 */
public class SetImageFunc implements Func1<ApiResponse, Image> {

    private BaseUserProfileActivity mActivity;

    public SetImageFunc (BaseUserProfileActivity activity) { this.mActivity = activity; }

    @Override
    public Image call(ApiResponse apiResponse) {
        Image image = apiResponse.getImages().get(0);
        this.mActivity.setAvatar(image);
        return image;
    }
}
