package com.homecooking.ykecomo.actions.product;

import com.homecooking.ykecomo.ui.activity.MenuPrincipalActivity;
import com.homecooking.ykecomo.ui.activity.chefZone.productForm.EditProductCategoryChefActivity;

import rx.functions.Action0;


public class GetProductCategoryAction implements Action0 {

    private MenuPrincipalActivity mActivity;
    private EditProductCategoryChefActivity mChefActivity;

    public GetProductCategoryAction(MenuPrincipalActivity activity) { this.mActivity = activity; }

    public GetProductCategoryAction(EditProductCategoryChefActivity activity) { this.mChefActivity = activity; }

    @Override
    public void call() {
        if(mActivity != null){
            this.mActivity.onProductCategoryComplete();
        }else{
            this.mChefActivity.onProductCategoryComplete();
        }

    }
}
