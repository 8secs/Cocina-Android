package com.homecooking.ykecomo.ui.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.homecooking.ykecomo.R;


/**
 * Created by andres on 29/04/15.
 */
public class ProductChefItemView extends CardView {

    private TextView mTitle;
    private ImageView mImage;

    public TextView getTitle() {
        return mTitle;
    }

    public ImageView getImage() {
        return mImage;
    }

    public int getProductID() {
        return mProductID;
    }

    public void setProductID(int mProductID) {
        this.mProductID = mProductID;
    }

    private int mProductID;

    public ProductChefItemView(Context context) {
        super(context);
    }

    public ProductChefItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductChefItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(R.id.product_title);
        this.mImage = (ImageView) findViewById(R.id.product_thumb);
    }

    @Override
    public String toString() {
        return mTitle.getText()
                + ": " + getLeft() + "," + getTop()
                + ": " + getMeasuredWidth() + "x" + getMeasuredHeight();
    }
}
