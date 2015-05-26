package com.homecooking.ykecomo.operators.image;

import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditImageProductChef;
import com.homecooking.ykecomo.ui.activity.userProfile.BaseUserProfileActivity;

import rx.functions.Func1;


public class SetImageFunc implements Func1<ApiResponse, Image> {

    private BaseUserProfileActivity mActivity;
    private EditImageProductChef mProductImageActivity;

    public SetImageFunc (BaseUserProfileActivity activity) { this.mActivity = activity; }

    public SetImageFunc (EditImageProductChef activity) { this.mProductImageActivity = activity; }

    @Override
    public Image call(ApiResponse apiResponse) {

        Image image = apiResponse.getImages().get(0);
        if(this.mActivity != null)  this.mActivity.setAvatar(image);
        else this.mProductImageActivity.setAvatar(image);

        return image;
    }
}
