package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;

public class EditTitleProductChefActivity extends BaseEditProduct{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_sigleline_text);
        setTitle(getResources().getString(R.string.elige_titulo_producto));

        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();
        mTitleLabel.setText(getResources().getString(R.string.elige_titulo_producto));
        mTitle.setHint(getResources().getString(R.string.nameHint));
    }

    @Override
    protected void gotoNext(){
        mExtras.putString(Constants.TITLE_BUNDLE_KEY, mTitle.getText().toString());
        Intent i = new Intent(EditTitleProductChefActivity.this, EditPortionProductChef.class);
        i.putExtras(mExtras);
        startActivity(i);
    }
}
