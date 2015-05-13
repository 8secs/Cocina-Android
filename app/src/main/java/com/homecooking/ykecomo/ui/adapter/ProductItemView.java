package com.homecooking.ykecomo.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.homecooking.ykecomo.R;


/**
 * Created by andres on 21/02/15.
 */
public class ProductItemView extends CardView {

    private TextView mTitle;
    private ImageView mImage;
    private TextView mChefName;
    private TextView mPrice;

    public ProductItemView(Context context) {
        super(context);
    }

    public ProductItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(R.id.text_shop_title);
        this.mImage = (ImageView) findViewById(R.id.product_thumb);
        this.mChefName = (TextView) findViewById(R.id.chef_name);
        this.mPrice = (TextView) findViewById(R.id.price_product);
    }

    @Override
    public String toString() {
        return mTitle.getText()
                + ": " + getLeft() + "," + getTop()
                + ": " + getMeasuredWidth() + "x" + getMeasuredHeight();
    }
}
