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
 * Time: 10 : 00
 */
public class EditPortionProductChef extends BaseEditProduct {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_sigleline_text);
        setTitle(getResources().getString(R.string.elige_num_raciones));

        if(mExtras.getString(Constants.PORTIONS) != null){
            mStr = mExtras.getString(Constants.PORTIONS);
        }
        mSelectedColumn = Constants.PORTIONS_COLUMN_ITEM;

        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();

        mTitleLabel.setText(getResources().getString(R.string.elige_num_raciones));
        mTitle.setHint(getResources().getString(R.string.raciones));
        mTitle.setInputType(InputType.TYPE_CLASS_NUMBER);
        if(mStr != null) mTitle.setText(mStr);
    }

    @Override
    protected void update(){

        mExtras.putString(Constants.PORTIONS, mTitle.getText().toString());
        Intent i;
        if(mStr == null) {
            i = new Intent(EditPortionProductChef.this, EditPriceProductChef.class);
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.ADD_PRODUCT_ITEM);
        }
        else {
            i = new Intent();
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
            mExtras.putInt(Constants.COLUMN_PRODUCT_ITEMS, Constants.PORTIONS_COLUMN_ITEM);
        }

        i.putExtras(mExtras);

        if(mExtras.getInt(Constants.PRODUCT_ITEMS) == Constants.ADD_PRODUCT_ITEM) startActivity(i);
        else setResult(RESULT_OK, i);
    }
}
