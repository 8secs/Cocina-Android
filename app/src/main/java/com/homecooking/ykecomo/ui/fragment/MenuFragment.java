package com.homecooking.ykecomo.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.github.clans.fab.FloatingActionButton;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Member;
import com.homecooking.ykecomo.model.Product;
import com.homecooking.ykecomo.model.ProductCategory;
import com.homecooking.ykecomo.ui.activity.ChefActivity;
import com.homecooking.ykecomo.ui.activity.ProductDetailActivity;
import com.homecooking.ykecomo.ui.activity.ProductsActivity;
import com.homecooking.ykecomo.ui.adapter.ChefAdapter;
import com.homecooking.ykecomo.ui.adapter.ProductAdapter;
import com.homecooking.ykecomo.ui.adapter.ShopAdapter;
import com.homecooking.ykecomo.ui.view.DividerDecoration;

import java.util.ArrayList;


public class MenuFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener {

    protected int mSelectedMode;

    private int mScrollOffset = 4;

    protected static String SELECTED_MODE = "SELECTED_MODE";

    //private OnFragmentInteractionListener mListener;

    public RecyclerView getList() {
        return mList;
    }

    protected RecyclerView mList;
    protected SwipeRefreshLayout mRefreshLayout;
    protected FloatingActionButton mFab;

    private ShopAdapter mAdapter;
    private ArrayList<ProductCategory> mMenuList = new ArrayList<ProductCategory>();

    private ChefAdapter mChefAdapter;
    private ArrayList<Member> mMenuChefList = new ArrayList<Member>();

    private ProductAdapter mProductAdapter;
    private ArrayList<Product> mMenuProductList = new ArrayList<>();

    private static Context mContext;

    private View mRootView;

    public void setMenuList(ArrayList<ProductCategory> mMenuList) { this.mMenuList = mMenuList; }
    public void setMenuChefList(ArrayList<Member> menuList){
        this.mMenuChefList = menuList;
    }
    public void setMenuProductList(ArrayList<Product> menuList) { this.mMenuProductList = menuList; }

    public static MenuFragment newInstance(int selectedMode, Context context) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();

        args.putInt(SELECTED_MODE, selectedMode);
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null) {
            mSelectedMode = getArguments().getInt(SELECTED_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.menu_fragment, container, false);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.recycler_swipe);
        mList = (RecyclerView) mRootView.findViewById(R.id.section_list);
        mList.setLayoutManager(getLayoutManager());
        mList.addItemDecoration(getItemDecoration());
        mList.setHasFixedSize(true);

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);
        mRefreshLayout.setOnRefreshListener(this);

        mFab = (FloatingActionButton) mRootView.findViewById(R.id.fab);

        if(mSelectedMode == Constants.USER_ENVIRONMENT_MODE){
            mFab.setVisibility(View.GONE);
            if(mMenuList != null && mMenuList.size() > 0) onProductCategoriesComplete();
            if(mMenuChefList != null && mMenuChefList.size() > 0) onMemberComplete();
        }else{
            mList.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (Math.abs(dy) > mScrollOffset) {
                        if (dy > 0) {
                            mFab.hide(true);
                        } else {
                            mFab.show(true);
                        }
                    }
                }
            });
            if(mMenuProductList != null && mMenuProductList.size() > 0) onProductsChefComplete();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("menuFragment", "onActivityCreated");

    }

    public void onProductCategoriesComplete(){
        if(mList != null){
            mAdapter = new ShopAdapter(mContext);
            mAdapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ProductCategory entity) {

                    Intent intent = new Intent(getActivity(), ProductsActivity.class);
                    intent.putExtra(Constants.ID_BUNDLE_KEY, entity.getID());
                    intent.putExtra(Constants.TITLE_BUNDLE_KEY, entity.getTitle());
                    startActivity(intent);
                }
            });

            mAdapter.setData(mMenuList);
            mList.setAdapter(mAdapter);
            mRefreshLayout.setRefreshing(false);
            mAdapter.notifyDataSetChanged();
        }
    }

    public void onMemberComplete(){
        if(mList != null){
            mChefAdapter = new ChefAdapter(getActivity());
            mChefAdapter.setOnItemClickListener(new ChefAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Member entity) {
                    Intent i = new Intent(getActivity(), ChefActivity.class);
                    Bundle extras = new Bundle();
                    extras.putInt(Constants.ID_BUNDLE_KEY, entity.getId());
                    i.putExtras(extras);
                    startActivity(i);
                }
            });

            mChefAdapter.setData(mMenuChefList);
            mList.setAdapter(mChefAdapter);
            mRefreshLayout.setRefreshing(false);
            mChefAdapter.notifyDataSetChanged();
        }
    }

    public void onProductsChefComplete(){
        if(mList != null){
            mProductAdapter = new ProductAdapter(getActivity());
            mProductAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Product product = mMenuProductList.get(position);

                    Intent i = new Intent(getActivity(), ProductDetailActivity.class);
                    i.putExtra(Constants.PRODUCTID_BUNDLE_KEY, product.getID());

                    if(product.getMember() != null){
                        i.putExtra(Constants.AVATAR_BUNDLE_KEY, product.getMember().getAvatarFilename());
                        String address = product.getMember().getAddress().getCity() + "-" + product.getMember().getAddress().getCountry();
                        i.putExtra(Constants.MEMBER_ADDRESS_BUNDLE_KEY, address);
                    }
                    startActivity(i);
                }
            });
            mList.setAdapter(mProductAdapter);

            mProductAdapter.setItems(mMenuProductList);
            mProductAdapter.setItemCount(mMenuProductList.size());
            mRefreshLayout.setRefreshing(false);
            mProductAdapter.notifyDataSetChanged();
        }
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerDecoration(getActivity());
    }


    @Override
    public void onRefresh() {
        /**
         * TODO: VERIFICAR CON QUE LISTA TRABAJAMOS PARA ACTUALIZAR
         */
    }

    public void onButtonPressed(Uri uri) {
        /*if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(int position);
    }

}
