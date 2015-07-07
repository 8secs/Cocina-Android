package com.homecooking.ykecomo.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;
import com.homecooking.ykecomo.R;

//import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

/**
 * Created by: andres
 * User: andres
 * Date: 5/06/15
 * Time: 11 : 00
 */
public class AdvancedMenuFragment extends Fragment {

    protected static String SELECTED_MODE = "SELECTED_MODE";

    protected int mSelectedMode;
    protected static Context mContext;

    protected View mRootView;
    protected FloatingActionButton mFab;
    protected RecyclerView mList;
    protected SwipeRefreshLayout mRefreshLayout;
    //protected RecyclerViewExpandableItemManager mRecyclerViewExpandableItemManager;

    public RecyclerView getList() {
        return mList;
    }

    public AdvancedMenuFragment newInstance(int selectedMode, Context context){
        AdvancedMenuFragment fragment = new AdvancedMenuFragment();
        Bundle args = new Bundle();

        args.putInt(SELECTED_MODE, selectedMode);
        fragment.setArguments(args);
        mContext = context;
        return fragment;
    }

    public AdvancedMenuFragment(){
        super();
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

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) { }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mRefreshLayout = (SwipeRefreshLayout) mRootView.findViewById(R.id.recycler_swipe);
        mList = (RecyclerView) mRootView.findViewById(R.id.section_list);
    }
}
