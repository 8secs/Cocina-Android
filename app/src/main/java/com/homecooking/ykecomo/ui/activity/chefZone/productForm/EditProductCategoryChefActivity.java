package com.homecooking.ykecomo.ui.activity.chefZone.productForm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.product.GetProductCategoryAction;
import com.homecooking.ykecomo.actions.product.GetProductCategoryOnNext;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.operators.ErrorHandler;
import com.homecooking.ykecomo.operators.product.GetImageObservableProductCategoryFunc;
import com.homecooking.ykecomo.operators.product.GetImageProductGategoryFunc;
import com.homecooking.ykecomo.operators.product.GetProductCategoriesFunc;
import com.homecooking.ykecomo.operators.product.SetImageProductCategoryFunc;
import com.homecooking.ykecomo.ui.adapter.ShopAdapter;
import com.homecooking.ykecomo.ui.view.DividerDecoration;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EditProductCategoryChefActivity extends AppCompatActivity {

    protected ShopAdapter mAdapter;
    protected RecyclerView mList;
    protected CircleProgressBar mProgress;

    //LISTS DATA
    protected ArrayList<ProductCategory> mMenuList = new ArrayList<ProductCategory>();
    public void setMenuList(ArrayList<ProductCategory> mMenuList) {
        this.mMenuList = mMenuList;
    }
    public ArrayList<ProductCategory> getMenuList() { return mMenuList; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product_chef);

        setTitle(getResources().getString(R.string.select_categoria));

        mProgress = (CircleProgressBar) findViewById(R.id.progressBar);

        setupList();
        getProductCategories();
    }

    protected void setupList(){
        mList = (RecyclerView) findViewById(R.id.section_list);
        mList.setLayoutManager(getLayoutManager());
        mList.addItemDecoration(getItemDecoration());

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerDecoration(this);
    }

    protected void getProductCategories(){
        App.getRestClient()
                .getPageService()
                .getCategories()
                .flatMap(new GetProductCategoriesFunc(this))
                .flatMap(new GetImageObservableProductCategoryFunc())
                .flatMap(new GetImageProductGategoryFunc())
                .map(new SetImageProductCategoryFunc(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(new GetProductCategoryOnNext(), new ErrorHandler(), new GetProductCategoryAction(this));
    }

    protected void showProgress(boolean bool){
        if(bool){
            mProgress.setVisibility(View.VISIBLE);
        }else{
            mProgress.setVisibility(View.GONE);
        }
    }

    public void onProductCategoryComplete(){
        showProgress(false);
        if(mAdapter == null){
            mAdapter = new ShopAdapter(this);
            mAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ProductCategory entity) {
                    Intent i = new Intent(EditProductCategoryChefActivity.this, EditTitleProductChefActivity.class);
                    i.putExtra(Constants.PRODUCT_ITEMS, Constants.ADD_PRODUCT_ITEM);
                    i.putExtra(Constants.PARENT, Integer.toString(entity.getID()));
                    i.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(i);
                    finish();
                }
            });
        }
        mAdapter.setData(mMenuList);
        mList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
