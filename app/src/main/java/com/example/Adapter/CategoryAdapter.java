package com.example.Adapter;


import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Model.MainCategoryModel;
import com.example.merakirana.Main_Category_Products;
import com.example.merakirana.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Holder> {

    private List<MainCategoryModel> list;

    private Activity activity ;

    public CategoryAdapter(Activity activity, List<MainCategoryModel> list) {
        this.list = list;
        this.activity = activity ;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_category, viewGroup, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Holder holder, int pos) {

        MainCategoryModel current = list.get(pos);

        holder.tvCatName.setText(current.getMainCategoryName());

        String img_url=activity.getResources().getString(R.string.MAIN_CATEGORY_IMG)+current.getLogo();

        Picasso.with(activity)
                .load(img_url)
                .placeholder(R.drawable.loader)
                .fit().centerInside()
                .into(holder.ivCatImage);

    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView ivCatImage;
        TextView tvCatName;

        public Holder(@NonNull View itemView) {
            super(itemView);

            ivCatImage = (ImageView) itemView.findViewById(R.id.topmostImageView);
            tvCatName = (TextView) itemView.findViewById(R.id.tvCatName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    Intent intent = new Intent(activity, Main_Category_Products.class);
                    Log.e("category_auto-id",list.get(pos).getMain_category_auto_id());
                    intent.putExtra("category_id",list.get(pos).getMain_category_auto_id());
                    intent.putExtra("p_type","main_category");
                    intent.putExtra("Category_Name",list.get(pos).getMainCategoryName());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    activity.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}