package com.homecooking.ykecomo.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.ProductCategory;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


/**
 * Created by andres on 20/2/15.
 */
public class ShopAdapter extends AbstractListAdapter<ProductCategory, ShopAdapter.ViewHolder>{

    private final Context mContext;
    private final LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public ShopAdapter(Context context) {
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(
                mInflater.inflate(R.layout.view_shop_item, viewGroup, false)
        );
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.bind(mData.get(position));
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mTitle;
        private ImageView mImageView;
        private       ProductCategory   mEntity;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.text_shop_title);
            mImageView = (ImageView) v.findViewById(R.id.category_thumb);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mEntity);
                    }
                }
            });

        }

        public void bind(ProductCategory entity) {
            mEntity = entity;
            mTitle.setText(entity.getTitle());
            String imgURL = Constants.BASE_URL.concat(entity.getImageFilename());
            setImageView(imgURL);
        }

        public TextView getTextView() {
            return mTitle;
        }


        @Override
        public String toString() {
            return "ViewHolder{" + mTitle.getText() + "}";
        }

        public void setTitle(CharSequence title) {
            mTitle.setText(title);
        }

        public void setImageView(String url){
            Picasso.with(mContext)
                    .load(url)
                    .error(android.R.drawable.stat_notify_error)
                    .placeholder(R.drawable.food_revolution_transparente)
                    .transform(transformation)
                    .into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            //holder.progressBar_picture.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            //Log.e(LOGTAG, "error");
                            //holder.progressBar_picture.setVisibility(View.GONE);
                        }
                    });
        }

        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = mImageView.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) { source.recycle(); }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };
    }

    public static interface OnItemClickListener {
        public void onItemClick(ProductCategory entity);
    }
}
