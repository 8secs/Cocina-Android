package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;

/**
 * Created by: andres
 * User: andres
 * Date: 26/05/15
 * Time: 11 : 04
 */
public class EditDescriptionProductChef extends BaseEditProduct {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_multiline_text);
        setTitle(getResources().getString(R.string.description_plato));

        if(mExtras.getString(Constants.MINI_DESCRIPTION) != null){
            mStr = mExtras.getString(Constants.MINI_DESCRIPTION);
        }
        mSelectedColumn = Constants.DESC_COLUMN_ITEM;

        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();

        mTitleLabel.setText(getResources().getString(R.string.description_plato));
        mTitle.setHint(getResources().getString(R.string.description));
        mTitle.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mTitle.setSingleLine(false);
        mTitle.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        if(mStr != null) mTitle.setText(mStr);
    }

    @Override
    protected void update(){

        mExtras.putString(Constants.MINI_DESCRIPTION, mTitle.getText().toString());
        Intent i;
        if(mStr == null) {
            i = new Intent(EditDescriptionProductChef.this, EditContentProductChef.class);
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.ADD_PRODUCT_ITEM);
        }
        else {
            i = new Intent();
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
            mExtras.putInt(Constants.COLUMN_PRODUCT_ITEMS, Constants.DESC_COLUMN_ITEM);
        }

        i.putExtras(mExtras);

        if(mExtras.getInt(Constants.PRODUCT_ITEMS) == Constants.ADD_PRODUCT_ITEM) {
            i.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
            startActivity(i);
        }
        else setResult(RESULT_OK, i);

        finish();
    }
}
