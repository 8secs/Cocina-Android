package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;

/**
 * Created by: andres
 * User: andres
 * Date: 26/05/15
 * Time: 10 : 35
 */
public class EditPriceProductChef extends BaseEditProduct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_sigleline_text);
        setTitle(getResources().getString(R.string.sales_price));

        if(mExtras.getString(Constants.PRICE) != null){
            mStr = mExtras.getString(Constants.PRICE);
        }
        mSelectedColumn = Constants.PRICE_COLUMN_ITEM;

        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();
        mTitleLabel.setText(getResources().getString(R.string.sales_price));
        mTitle.setHint(getResources().getString(R.string.price));
        mTitle.setInputType(InputType.TYPE_CLASS_NUMBER);
        if(mStr != null) mTitle.setText(mStr);
    }

    @Override
    protected void update(){

        mExtras.putString(Constants.PRICE, mTitle.getText().toString());
        Intent i;
        if(mStr == null) {
            i = new Intent(EditPriceProductChef.this, EditDescriptionProductChef.class);
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.ADD_PRODUCT_ITEM);
        }
        else {
            i = new Intent();
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
            mExtras.putInt(Constants.COLUMN_PRODUCT_ITEMS, Constants.PRICE_COLUMN_ITEM);
        }

        i.putExtras(mExtras);

        if(mExtras.getInt(Constants.PRODUCT_ITEMS) == Constants.ADD_PRODUCT_ITEM) startActivity(i);
        else setResult(RESULT_OK, i);

    }
}
