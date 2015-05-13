package com.homecooking.ykecomo.ui.preferences;

import android.content.Context;
import android.os.Parcelable;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.ui.activity.userProfile.UserProfileSettings;
import com.squareup.picasso.Picasso;


public class ImagePreference extends Preference {

    ImageView mImage;
    String mUrl;
    Context mContext;
    UserProfileSettings mActivity;

    public void setActivity(UserProfileSettings activity){ this.mActivity = activity; }

    public ImageView getImage() {
        return mImage;
    }

    public void setUrl(String url){
        this.mUrl = url;

    }

    public ImagePreference(Context context){
        super(context);
        mContext = context;
    }

    public ImagePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setLayoutResource(R.layout.image_layout);
    }

    public ImagePreference(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }

    @Override
    public View getView(View convertView, ViewGroup parent) {
        return super.getView(convertView, parent);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }

    @Override
    protected void onBindView(View view) {
        mImage = (ImageView) view.findViewById(R.id.avatar);
        mActivity.setAvatarView(mImage);
        Picasso.with(mContext)
                .load(this.mUrl)
                .error(android.R.drawable.stat_notify_error)
                .placeholder(mContext.getResources().getDrawable(R.mipmap.ic_launcher))
                .fit()
                .into(mImage);
        super.onBindView(view);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        return super.onSaveInstanceState();
    }

    @Override
    protected void onSetInitialValue(boolean restorePersistedValue, Object defaultValue) {
        super.onSetInitialValue(restorePersistedValue, defaultValue);
    }
}
