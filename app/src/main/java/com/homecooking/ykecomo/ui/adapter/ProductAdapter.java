package com.homecooking.ykecomo.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.homecooking.ykecomo.R;
import com.homecooking.ykecomo.app.Constants;
import com.homecooking.ykecomo.model.Product;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;


public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductItemHolder> {

    public void setItems(ArrayList<Product> mItems) {
        this.mItems = mItems;
    }

    private ArrayList<Product> mItems;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public Context getContext() {
        return mContext;
    }

    private Context mContext;

    public ProductAdapter(Context context) {
        mItems = new ArrayList<Product>();
        mContext = context;
    }

    public void setItemCount(int count) {
        notifyDataSetChanged();
    }

    @Override
    public ProductItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_product_item, container, false);
        return new ProductItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(ProductItemHolder itemHolder, int position) {
        Product item = mItems.get(position);
        itemHolder.setTitle(item.getTitle());
        /**
         * * TODO: Tenemos que recoger el tipo de moneda que esta utilizando la tienda y asignarla.
         */
        itemHolder.setPrice(item.getBasePrice()+"€");
        itemHolder.setPortions(mContext.getResources().getString(R.string.PORTIONS) + " " + item.getPortions());
        if(item.getMember() != null){
            itemHolder.setChefName(item.getMember().getFirstName() + " " + item.getMember().getSurname());
            itemHolder.setCity(item.getMember().getAddress().getCity() + "-" + item.getMember().getAddress().getCountry());
        }
        //String base_URL = "http://cocina.visitaelaljarafe.com/";
        String imgURL = Constants.BASE_URL.concat(item.getImgUrl());
        itemHolder.setImageView(imgURL);

    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ProductItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView, itemHolder.getPosition(), itemHolder.getItemId());
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public static class ProductItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mChefName;
        private ImageView mImageView;
        private TextView mPrice;
        private TextView mPortions;
        private TextView mCity;
        private ImageView mFavoriteBtn;

        private ProductAdapter mAdapter;

        public ProductItemHolder(View itemView, ProductAdapter adapter) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.text_shop_title);
            mChefName = (TextView) itemView.findViewById(R.id.chef_name);
            mImageView = (ImageView) itemView.findViewById(R.id.product_thumb);
            mPrice = (TextView) itemView.findViewById(R.id.price_product);
            mPortions = (TextView) itemView.findViewById(R.id.text_portions);
            mCity = (TextView) itemView.findViewById(R.id.text_city);
            mFavoriteBtn = (ImageView) itemView.findViewById(R.id.fab);
            itemView.setOnClickListener(this);
            mAdapter = adapter;

        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setTitle(CharSequence title) {
            mTitle.setText(title);
        }

        public void setChefName(CharSequence name){ mChefName.setText(name); }

        public void setPrice(CharSequence price){ mPrice.setText(price); }

        public void setPortions(CharSequence portions){ mPortions.setText(portions); }

        public void setCity(CharSequence city){ mCity.setText(city); }

        public void setImageView(String url){
            Picasso.with(mAdapter.getContext())
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
}
