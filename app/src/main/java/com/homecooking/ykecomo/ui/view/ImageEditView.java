package com.homecooking.ykecomo.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.homecooking.ykecomo.R;
import com.squareup.picasso.Picasso;

/**
 * Created by: andres
 * User: andres
 * Date: 29/05/15
 * Time: 07 : 37
 */
public class ImageEditView extends RelativeLayout {

    LayoutInflater mInflater;

    private View mRoot;
    private ImageView mImage;
    private TextView mTitle;
    private CheckBox mFlag;

    private Context mContext;

    public ImageEditView (Context context){
        super(context);
        //initialize(context, null, 0);
    }

    public ImageEditView(Context context, AttributeSet attrs){
        super(context, attrs);
        //initialize(context, attrs, 0);
    }

    public ImageEditView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        //initialize(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate(){
        super.onFinishInflate();
        this.mTitle = (TextView) findViewById(R.id.product_title);
        this.mImage = (ImageView) findViewById(R.id.product_thumb);
        mFlag = (CheckBox) findViewById(R.id.flag);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyle) {
        /*LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflater.inflate(R.layout.image_edit_view, this, true);

        mContext = context;

        mTitle = (TextView) mRoot.findViewById(R.id.title);
        mImage = (ImageView) mRoot.findViewById(R.id.image_header);
        mFlag = (CheckBox) mRoot.findViewById(R.id.flag);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemEditCard, defStyle, 0);
        String title = a.getString(R.styleable.ItemEditCard_messageTitle);
        setTitle(title);*/
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setImage(String src) {
        Picasso.with(mContext)
                .load(src)
                .error(android.R.drawable.stat_notify_error)
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .into(mImage);
    }
}
