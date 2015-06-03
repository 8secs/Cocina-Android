package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;

public class EditTitleProductChefActivity extends BaseEditProduct{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_sigleline_text);
        setTitle(getResources().getString(R.string.elige_titulo_producto));

        if(mExtras.getString(Constants.TITLE) != null){
            mStr = mExtras.getString(Constants.TITLE);
        }
        mSelectedColumn = Constants.TITLE_COLUMN_ITEM;

        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();

        mTitleLabel.setText(getResources().getString(R.string.elige_titulo_producto));
        mTitle.setHint(getResources().getString(R.string.nameHint));
        mTitle.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        mTitle.setSingleLine();
        if(mStr != null) mTitle.setText(mStr);
    }

    @Override
    protected void update(){
        mExtras.putString(Constants.TITLE, mTitle.getText().toString());
        Intent i;
        if(mStr == null) {
            i = new Intent(EditTitleProductChefActivity.this, EditPortionProductChef.class);
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.ADD_PRODUCT_ITEM);
        }
        else {
            i = new Intent();
            mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
            mExtras.putInt(Constants.COLUMN_PRODUCT_ITEMS, Constants.TITLE_COLUMN_ITEM);
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
