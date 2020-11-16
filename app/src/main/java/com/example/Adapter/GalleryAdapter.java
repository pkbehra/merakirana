package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Model.GalleryRVModel;
import com.example.merakirana.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ItemViewHolder>  {

    private Context mContext;
    private List<GalleryRVModel> galleryRVModelList;


    public GalleryAdapter(Context context, List<GalleryRVModel> galleryRVModelList) {
        this.mContext = context;
        this.galleryRVModelList = galleryRVModelList;

    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView topmostImageView, ivTopMostWishlist, ivTopmostCart, ivPopularWishlistRed;
        TextView topmostName, topmostPrice, tvQuantityTopMost;
        Button btnTopMostAdd, btnTopMostDecrease, btnTopMostIncrease;

        ItemViewHolder(final View itemView) {
            super(itemView);

            topmostImageView = itemView.findViewById(R.id.ivGallery);

        }
    }


    @NonNull
    @Override
    public GalleryAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_gallery_item, viewGroup, false);

        return new GalleryAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryAdapter.ItemViewHolder holder, int position) {

        final GalleryRVModel galleryRVModel = galleryRVModelList.get(position);

        holder.topmostImageView.setImageResource(galleryRVModel.getProdImage());

    }

    @Override
    public int getItemCount() {
        return galleryRVModelList.size();
    }
}
