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
        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();
        mTitleLabel.setText(getResources().getString(R.string.sales_price));
        mTitle.setHint(getResources().getString(R.string.price));
        mTitle.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    @Override
    protected void gotoNext(){
        mExtras.putString(Constants.PORTIONS, mTitle.getText().toString());
        Intent i = new Intent(EditPriceProductChef.this, EditDescriptionProductChef.class);
        i.putExtras(mExtras);
        startActivity(i);
    }
}
