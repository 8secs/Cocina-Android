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


public class ProductChefAdapter extends RecyclerView.Adapter<ProductChefAdapter.ItemHolder> {

    public void setItems(ArrayList<Product> mItems) {
        this.mItems = mItems;
    }

    private ArrayList<Product> mItems;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public Context getContext() {
        return mContext;
    }

    public ProductChefAdapter(Context context){
        mItems = new ArrayList<Product>();
        mContext = context;
    }

    private Context mContext;

    public void setItemCount(int count) {
        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.view_product_chef_item, container, false);
        return new ItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {
        Product item = mItems.get(position);
        itemHolder.setTitle(item.getTitle());

        //String base_URL = "http://cocina.visitaelaljarafe.com/";
        String imgURL = Constants.BASE_URL.concat(item.getImgUrl());
        itemHolder.setImageView(imgURL);

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @SuppressWarnings("deprecation")
    private void onItemHolderClick(ItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getPosition(), itemHolder.getItemId());
        }
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private ImageView mImageView;


        private ProductChefAdapter mAdapter;

        public ItemHolder(View itemView, ProductChefAdapter adapter) {
            super(itemView);

            mTitle = (TextView) itemView.findViewById(R.id.product_title);
            mImageView = (ImageView) itemView.findViewById(R.id.product_thumb);
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
