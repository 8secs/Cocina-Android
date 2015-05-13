package com.homecooking.ykecomo.ui.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.TextView;

import com.homecooking.ykecomo.R;

public class ShopItemView extends GridLayout {
    
    private TextView mTitle;

    public ShopItemView(Context context) {
        super(context);
    }

    public ShopItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ShopItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    
    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(R.id.text_shop_title);
    }

    @Override
    public String toString() {
        return mTitle.getText()
                + ": " + getLeft() + "," + getTop()
                + ": " + getMeasuredWidth() + "x" + getMeasuredHeight();
    }
}
