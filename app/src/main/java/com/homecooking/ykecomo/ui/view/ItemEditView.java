package com.homecooking.ykecomo.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.homecooking.ykecomo.R;

/**
 * Created by: andres
 * User: andres
 * Date: 27/05/15
 * Time: 19 : 28
 */
public class ItemEditView extends CardView implements View.OnClickListener {

    public static final int ANIM_DURATION = 200;

    private OnItemCardClicked mListener = null;

    LayoutInflater mInflater;

    private View mRoot;

    TextView mTitle;
    TextView mSubtitle;
    CheckBox mFlag;

    public TextView getTitle() { return mTitle; }
    public TextView getSubtitle() { return mSubtitle; }
    public CheckBox getFlag() { return mFlag; }

    public interface OnItemCardClicked {
        public void OnItemCardClicked(String tag);
    }

    public ItemEditView(Context context){
        super(context);
        mInflater = LayoutInflater.from(context);
        initialize(context, null, 0);
    }

    public ItemEditView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        mInflater = LayoutInflater.from(context);
        initialize(context, attrs, defStyle);
    }

    public ItemEditView(Context context, AttributeSet attrs){
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        initialize(context, attrs, 0);
    }

    public void setListener(OnItemCardClicked listener) {
        mListener = listener;
    }

    private void initialize(Context context, AttributeSet attrs, int defStyle) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        mRoot = inflater.inflate(R.layout.item_edit_view, this, true);
        mTitle = (TextView) mRoot.findViewById(R.id.title);
        mSubtitle = (TextView) mRoot.findViewById(R.id.subtitle);
        mFlag = (CheckBox) mRoot.findViewById(R.id.flag);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemEditCard, defStyle, 0);
        String title = a.getString(R.styleable.ItemEditCard_messageTitle);
        setTitle(title);
        String text = a.getString(R.styleable.ItemEditCard_messageText);
        if (text != null) {
            setText(text);
        }

        /*String button1text = a.getString(R.styleable.ItemEditCard_button1text);
        boolean button1emphasis = a.getBoolean(R.styleable.ItemEditCard_button1emphasis, false);
        String button1tag = a.getString(R.styleable.ItemEditCard_button1tag);
        String button2text = a.getString(R.styleable.ItemEditCard_button2text);
        boolean button2emphasis = a.getBoolean(R.styleable.ItemEditCard_button2emphasis, false);
        String button2tag = a.getString(R.styleable.ItemEditCard_button2tag);
        int emphasisColor = a.getColor(R.styleable.ItemEditCard_emphasisColor,
                getResources().getColor(R.color.primary_light));*/


        setRadius(getResources().getDimensionPixelSize(R.dimen.card_corner_radius));
        setCardElevation(getResources().getDimensionPixelSize(R.dimen.card_elevation));
        setPreventCornerOverlap(false);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setText(String text) {
        mSubtitle.setText(text);
    }

    @Override
    public void onClick(View v) {
        if (mListener == null) {
            return;
        }
        Log.e("onClick", v.toString());
         mListener.OnItemCardClicked(this.getTag().toString());
    }
}
