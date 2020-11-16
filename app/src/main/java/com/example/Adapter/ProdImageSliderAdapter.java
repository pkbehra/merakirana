package com.example.Adapter;


import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.Model.AllProductGalleryModel;
import com.example.merakirana.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;

public class ProdImageSliderAdapter extends PagerAdapter {

    private List<AllProductGalleryModel> allProductGalleryModelList;
    private LayoutInflater inflater;
    private Context context;

    public ProdImageSliderAdapter(List<AllProductGalleryModel> allProductGalleryModels, Context context) {
        this.allProductGalleryModelList = allProductGalleryModels;
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return allProductGalleryModelList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.image_item, view, false);

        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);

        String imgUrl=context.getResources().getString(R.string.PROD_GALLERY_IMG_URL)+allProductGalleryModelList.get(position).getLogo();
        Picasso.with(context)
                .load(imgUrl)
                .placeholder(R.drawable.loader)
                .error(R.drawable.autologo)
                .fit().centerInside()
                .into(imageView);


        //   imageView.setImageResource(imageModelArrayList.get(position).getImageDrawable());

        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
}
