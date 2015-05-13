package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.ui.activity.MenuPrincipalActivity;

import rx.functions.Func1;

public class SetImageProductCategoryFunc implements Func1<Image, ProductCategory> {

    private MenuPrincipalActivity mActivity;

    public SetImageProductCategoryFunc(MenuPrincipalActivity activity) { this.mActivity = activity; }

    @Override
    public ProductCategory call(Image image) {
        for (ProductCategory category : this.mActivity.getMenuList()) {
            if (Integer.valueOf(category.getPageImage()) == image.getId()) {
                category.setImageFilename(image.getFilename());
                return category;
            }
        }
        return null;
    }
}
