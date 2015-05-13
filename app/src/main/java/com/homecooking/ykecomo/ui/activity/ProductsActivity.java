package com.homecooking.ykecomo.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.actions.product.GetProductCompletedAction;
import com.homecooking.ykecomo.actions.product.GetProductOnNext;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Address;
import com.homecooking.ykecomo.model.Image;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.operators.ErrorHandler;
import com.homecooking.ykecomo.operators.product.GetMemberProdObservableFunc;
import com.homecooking.ykecomo.operators.product.GetMembersAvatarAddressFunc;
import com.homecooking.ykecomo.operators.product.GetProductsFunc;
import com.homecooking.ykecomo.operators.product.SetAvatarAddressMemberFunc;
import com.homecooking.ykecomo.operators.product.SetImageToProductFunc;
import com.homecooking.ykecomo.operators.product.SetMemberToProductFunc;
import com.homecooking.ykecomo.operators.product.UpdateMemberFunc;
import com.homecooking.ykecomo.rest.model.ApiResponse;
import com.homecooking.ykecomo.ui.adapter.ProductAdapter;
import com.homecooking.ykecomo.ui.view.DividerDecoration;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscription;
import rx.android.observables.AndroidObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ProductsActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, ObservableScrollViewCallbacks {

    private String mParentID;

    private ObservableRecyclerView mList;
    private ProductAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    protected CircleProgressBar mProgress;

    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Address> mAddresses;

    private Observable<ApiResponse> mProductsObservable;
    private Subscription mProductSubscription;
    private String mAvatarChef;
    private ArrayList<Product> mProducts = new ArrayList<Product>();

    private ArrayList<Image> mImages;
    private ArrayList<Image> mAvatars;
    private ArrayList<Member> mMembers;

    public ArrayList<Image> getImages() { return mImages; }
    public ArrayList<Image> getAvatars() { return mAvatars; }
    public ArrayList<Product> getProducts() { return mProducts; }
    public ArrayList<Member> getMembers() { return mMembers; }
    public void setAvatars(ArrayList<Image> mAvatars) { this.mAvatars = mAvatars; }
    public void setAvatarChef(String mAvatarChef) {this.mAvatarChef = mAvatarChef;}
    public void setAddresses(ArrayList<Address> mAddresses) { this.mAddresses = mAddresses;}
    public void setImages(ArrayList<Image> mImages) {
        this.mImages = mImages;
    }
    public void setProducts(ArrayList<Product> products) { this.mProducts = products; }
    public void setMembers(ArrayList<Member> members) { this.mMembers = members; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent().getExtras() != null){
            Bundle bundle = getIntent().getExtras();
            int id = bundle.getInt(Constants.ID_BUNDLE_KEY);
            mParentID = Integer.toString(id);
            setTitle(bundle.getString(Constants.TITLE_BUNDLE_KEY));
        }

        setContentView(R.layout.product_recycler);

        createObservables();

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.products_layout);
        mProgress = (CircleProgressBar) findViewById(R.id.progressBar);
        mLayoutManager = getLayoutManager();

        mList = (ObservableRecyclerView) findViewById(R.id.product_list);
        mList.setLayoutManager(mLayoutManager);
        mList.addItemDecoration(getItemDecoration());

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);

        mAdapter = new ProductAdapter(this);
        mAdapter.setOnItemClickListener(this);
        mList.setAdapter(mAdapter);
        
        mList.setScrollViewCallbacks(this);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                createSubscription();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        createSubscription();
    }

    @Override
    protected  void onStop(){
        super.onStop();
        mProductSubscription.unsubscribe();
    }
    
    private void createObservables(){
        mProductsObservable = App.getRestClient()
                .getPageService()
                .getProductsCategory(mParentID);
    }
    
    private void createSubscription(){

        mProductSubscription = AndroidObservable.bindActivity(this, mProductsObservable)
                .flatMap(new GetProductsFunc(this))
                .flatMap(new SetImageToProductFunc(this))
                .flatMap(new GetMemberProdObservableFunc(this))
                .flatMap(new GetMembersAvatarAddressFunc(this))
                .flatMap(new SetAvatarAddressMemberFunc(this))
                .flatMap(new UpdateMemberFunc(this))
                .map(new SetMemberToProductFunc(this))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .cache()
                .subscribe(
                        new GetProductOnNext(this),
                        new ErrorHandler(),
                        new GetProductCompletedAction(this)
                );

    }
    
    public void updateUI(){
        mProgress.setVisibility(View.GONE);
        mRefreshLayout.setVisibility(View.VISIBLE);
        mAdapter.setItems(mProducts);
        mAdapter.setItemCount(getDefaultItemCount());
        mAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
    }

    public String getImage(String productId, ArrayList<Image> images){
        for(Image image : images){
            if(Integer.toString(image.getId()).equals(productId)){
                return image.getFilename();
            }
        }
        return "default";
    }

    public Address getAddress(String memberId){
        for(Address address : mAddresses){
            if(memberId.equals(Integer.toString(address.getId()))){
                return address;
            }
        }
        return null;
    }

    public void updateProducts(Product product){
        for(int i = 0; i < mProducts.size(); i++){
            if(mProducts.get(i).getID() == product.getID()){
                mProducts.set(i, product);
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        
        Product product = mProducts.get(position);

        Intent i = new Intent(this, ProductDetailActivity.class);
        i.putExtra(Constants.PRODUCTID_BUNDLE_KEY, product.getID());

        if(product.getMember() != null){
            i.putExtra(Constants.AVATAR_BUNDLE_KEY, product.getMember().getAvatarFilename());
            String address = product.getMember().getAddress().getCity() + "-" + product.getMember().getAddress().getCountry();
            i.putExtra(Constants.MEMBER_ADDRESS_BUNDLE_KEY, address);
        }

        startActivity(i);
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerDecoration(this);
    }

    protected int getDefaultItemCount() {
        return mProducts.size();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_products, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        ActionBar ab = getSupportActionBar();
        if (scrollState == ScrollState.UP) {
            if (ab.isShowing()) {
                ab.hide();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (!ab.isShowing()) {
                ab.show();
            }
        }
    }
}
