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
import com.homecooking.ykecomo.model.Favorite;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by: andres
 * User: andres
 * Date: 2/06/15
 * Time: 20 : 16
 */
public class FavoriteAdapter extends AbstractListAdapter<Favorite, FavoriteAdapter.ViewHolder>  {

    private final Context mContext;
    private final LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public FavoriteAdapter(Context context){
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(
                mInflater.inflate(R.layout.view_product_item, viewGroup, false)
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
        private TextView mChefName;
        private ImageView mImageView;
        private TextView mPrice;
        private TextView mCity;
        private TextView mPortions;

        private Favorite mEntity;

        public ViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.text_shop_title);
            mImageView = (ImageView) v.findViewById(R.id.product_thumb);
            mChefName = (TextView) v.findViewById(R.id.chef_name);
            mPrice = (TextView) v.findViewById(R.id.price_product);
            mCity = (TextView) v.findViewById(R.id.text_city);
            mPortions = (TextView) v.findViewById(R.id.text_portions);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(mEntity);
                    }
                }
            });

        }

        public void bind(Favorite entity) {
            mEntity = entity;

            if(mEntity.getClassName().equals("Product")){
                if(mEntity.getProduct() != null){
                    setTitle(mEntity.getProduct().getTitle());
                    setPrice(mEntity.getProduct().getBasePrice()+mContext.getResources().getString(R.string.euro));
                    setPortions(mContext.getResources().getString(R.string.PORTIONS) + " " + mEntity.getProduct().getPortions());
                }
                if(mEntity.getMember() != null){
                    setChefName(mEntity.getMember().getFirstName() + " " + mEntity.getMember().getSurname());
                }
            }else if(mEntity.getClassName().equals("Member")){
                if(mEntity.getMember() != null){
                    setTitle(mEntity.getMember().getFirstName() + " " + mEntity.getMember().getSurname());
                    if(mEntity.getProduct() != null) setPortions(mContext.getResources().getString(R.string.PORTIONS) + " " + mEntity.getProduct().getPortions());
                    else setPortions(mContext.getResources().getString(R.string.PORTIONS) + ": 0");
                    if(mEntity.getMember().getChefReviews() != null) setPrice(mContext.getResources().getString(R.string.revisiones) + ": " + mEntity.getMember().getChefReviews().size());
                    else setPrice(mContext.getResources().getString(R.string.revisiones) + ": 0" );
                }
            }

            if(mEntity.getImage() != null){
                String imgURL = Constants.BASE_URL.concat(entity.getImage().getFilename());
                setImageView(imgURL);
            }

            if(mEntity.getAddress() != null){
                setCity(mEntity.getAddress().getCity() + ", " + mEntity.getAddress().getCountry());
            }
        }

        @Override
        public String toString() {
            return "ViewHolder{" + mTitle.getText() + "}";
        }

        public void setTitle(CharSequence title) {
            mTitle.setText(title);
        }

        public void setChefName(CharSequence name){ mChefName.setText(name); }

        public void setPrice(CharSequence price){ mPrice.setText(price); }

        public void setPortions(CharSequence portions){ mPortions.setText(portions); }

        public void setCity(CharSequence city){ mCity.setText(city); }

        public void setImageView(String url){
            Picasso.with(mContext)
                    .load(url)
                    .error(android.R.drawable.stat_notify_error)
                    .placeholder(R.drawable.food_revolution_transparente)
                    .transform(transformation)
                    .into(mImageView);
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
        public void onItemClick(Favorite entity);
    }
}
