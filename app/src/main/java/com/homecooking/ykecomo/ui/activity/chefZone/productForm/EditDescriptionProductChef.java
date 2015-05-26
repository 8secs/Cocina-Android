package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

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
        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();
        mTitleLabel.setText(getResources().getString(R.string.description_plato));
        mTitle = (EditText) findViewById(R.id.etTitle);
        mTitle.setHint(getResources().getString(R.string.description));
    }

    @Override
    protected void gotoNext(){
        mExtras.putString(Constants.MINI_DESCRIPTION, mTitle.getText().toString());
        Intent i = new Intent(EditDescriptionProductChef.this, EditImageProductChef.class);
        i.putExtras(mExtras);
        startActivity(i);
    }
}
