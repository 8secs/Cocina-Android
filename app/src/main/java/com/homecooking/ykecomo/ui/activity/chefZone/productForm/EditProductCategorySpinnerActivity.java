package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.ProductCategory;

import java.util.ArrayList;

/**
 * Created by: andres
 * User: andres
 * Date: 1/06/15
 * Time: 10 : 56
 */
public class EditProductCategorySpinnerActivity extends BaseEditProduct  implements AdapterView.OnItemSelectedListener{

    ArrayList<ProductCategory> mCategories;
    ArrayList<String> mCategoryStr = new ArrayList<>();
    String mSelectedCategory;
    ProductCategory mSelected;

    Spinner mSpinnerCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_category_product_spinner);
        setTitle(getResources().getString(R.string.select_categoria));

        if(mExtras.getString(Constants.CATEGORY) != null) mSelectedCategory = mExtras.getString(Constants.CATEGORY);

        if(mExtras.getSerializable(Constants.CATEGORIES) != null){
            mCategories = (ArrayList<ProductCategory>) mExtras.getSerializable(Constants.CATEGORIES);
            for(ProductCategory cat : mCategories){
                mCategoryStr.add(cat.getTitle());
            }
        }

        mSelectedColumn = Constants.CATEGORY_COLUMN_ITEM;
        setupUI();
    }

    @Override
    protected void setupUI(){
        super.setupUI();

        mTitleLabel.setText(getResources().getString(R.string.categoria));
        mSpinnerCategory = (Spinner) findViewById(R.id.spinnerCategory);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_row, mCategoryStr);
        mSpinnerCategory.setAdapter(adapter);
        mSpinnerCategory.setOnItemSelectedListener(this);

        int index = mCategoryStr.indexOf(mSelectedCategory);
        mSelected = getSelectedCategory(index);
        mSpinnerCategory.setSelection(index);

    }

    @Override
    protected void update(){
        mExtras.putString(Constants.PARENT_ID, Integer.toString(mSelected.getID()));
        Intent i = new Intent();
        mExtras.putInt(Constants.PRODUCT_ITEMS, Constants.EDIT_PRODUCT_ITEM);
        mExtras.putInt(Constants.COLUMN_PRODUCT_ITEMS, Constants.CATEGORY_COLUMN_ITEM);
        i.putExtras(mExtras);
        setResult(RESULT_OK, i);
    }

    protected ProductCategory getSelectedCategory(int index){
        ProductCategory category = mCategories.get(index);
        return category;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        String selected = parent.getItemAtPosition(pos).toString();
        int index = mCategoryStr.indexOf(selected);
        mSelectedCategory = selected;
        mSelected = getSelectedCategory(index);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_address_member, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home){
            update();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
