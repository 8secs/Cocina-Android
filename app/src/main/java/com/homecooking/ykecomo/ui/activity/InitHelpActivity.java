package com.homecooking.ykecomo.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.App;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.ui.fragment.ScreenSlidePageFragment;

public class InitHelpActivity extends Activity {

    private static final int NUM_PAGES = 5;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_init_help);

        Bundle extras = getIntent().getExtras();
        String from = extras.getString("from");

        if(from.equals("splash")){
            //getActionBar().hide();
            //getActionBar().setDisplayShowHomeEnabled(false);
        }

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }
        });


        Button gotoNext = (Button) findViewById(R.id.goto_content_button);
        if(from.equals("splash")){
            gotoNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences.Editor editor = App.getPref().edit();
                    editor.putBoolean(Constants.IS_FIRST_TIME_STR, true);
                    editor.commit();
                    Intent i = new Intent(InitHelpActivity.this, MenuPrincipalActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }else{
            gotoNext.setVisibility(View.GONE);
        }



    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //delayedHide(100);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }
}
