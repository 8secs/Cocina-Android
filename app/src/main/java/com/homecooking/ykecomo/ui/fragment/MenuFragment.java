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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.github.clans.fab.FloatingActionButton;
import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
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

    private int mScrollOffset = 4;

    protected static String SELECTED_MODE = "SELECTED_MODE";

    private OnFragmentInteractionListener mListener;

    protected RecyclerView mList;
    protected SwipeRefreshLayout mRefreshLayout;
    protected FloatingActionButton mFab;

    protected int mSelectedMode;

    private ShopAdapter mAdapter;
    private ArrayList<ProductCategory> mMenuList = new ArrayList<ProductCategory>();

    private ChefAdapter mChefAdapter;
    private ArrayList<Member> mMenuChefList = new ArrayList<Member>();

    private ProductAdapter mProductAdapter;

    private static Context mContext;

    public void setMenuList(ArrayList<ProductCategory> mMenuList) {
        this.mMenuList = mMenuList;
    }

    public void setMenuChefList(ArrayList<Member> menuList){
        this.mMenuChefList = menuList;
    }

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
        if (getArguments() != null) {
            mSelectedMode = getArguments().getInt(SELECTED_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.menu_fragment, container, false);
        mRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.recycler_swipe);
        mList = (RecyclerView) mRefreshLayout.findViewById(R.id.section_list);
        mList.setLayoutManager(getLayoutManager());
        mList.addItemDecoration(getItemDecoration());

        mList.getItemAnimator().setAddDuration(1000);
        mList.getItemAnimator().setChangeDuration(1000);
        mList.getItemAnimator().setMoveDuration(1000);
        mList.getItemAnimator().setRemoveDuration(1000);
        mRefreshLayout.setOnRefreshListener(this);

        mFab = (FloatingActionButton) rootView.findViewById(R.id.fab);

        if(mSelectedMode == Constants.USER_ENVIRONMENT_MODE){
            mFab.setVisibility(View.GONE);
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
        }
        return rootView;
    }

    public void onProductCategoriesComplete(){
        if(mAdapter == null){
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
        }
        mAdapter.setData(mMenuList);
        mList.setAdapter(mAdapter);
        mRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    public void onMemberComplete(){
        if(mChefAdapter == null){
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
        }
        mChefAdapter.setData(mMenuChefList);
        mList.setAdapter(mChefAdapter);
        mRefreshLayout.setRefreshing(false);
        mChefAdapter.notifyDataSetChanged();
    }

    public void onProductsChefComplete(){
        if(mProductAdapter == null){
            mProductAdapter = new ProductAdapter(getActivity());
            mProductAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    Product product = App.getProductsChef().get(position);

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
        }
        mProductAdapter.setItems(App.getProductsChef());
        mProductAdapter.setItemCount(App.getProductsChef().size());
        mProductAdapter.notifyDataSetChanged();
        mRefreshLayout.setRefreshing(false);
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
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(int position);
    }

}
