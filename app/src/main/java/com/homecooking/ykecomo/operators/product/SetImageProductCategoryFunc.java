package com.homecooking.ykecomo.operators.product;


import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.ui.activity.MenuPrincipalActivity;
import com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditProductCategoryChefActivity;

import java.util.ArrayList;

import rx.functions.Func1;

public class SetImageProductCategoryFunc implements Func1<Image, ProductCategory> {

    private MenuPrincipalActivity mActivity;
    private EditProductCategoryChefActivity mChefActivity;

    public SetImageProductCategoryFunc(MenuPrincipalActivity activity) { this.mActivity = activity; }

    public SetImageProductCategoryFunc(EditProductCategoryChefActivity activity) { this.mChefActivity = activity; }

    @Override
    public ProductCategory call(Image image) {
        ProductCategory category;
        if(this.mActivity != null){
            category = getCategory(0, image);
        }else{
            category = getCategory(1, image);
        }
        return category;
    }

    private ProductCategory getCategory(int activity, Image image){
        ArrayList<ProductCategory> categories;
        if(activity == 0){
            categories = this.mActivity.getMenuList();
        }else{
            categories = this.mChefActivity.getMenuList();
        }
        for (ProductCategory category : categories) {
            if (Integer.valueOf(category.getPageImage()) == image.getId()) {
                category.setImageFilename(image.getFilename());
                return category;
            }
        }
        return null;
    }
}
