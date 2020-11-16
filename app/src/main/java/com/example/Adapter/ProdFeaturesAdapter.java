package com.example.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Model.AllProductFeatureModel;
import com.example.merakirana.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProdFeaturesAdapter extends RecyclerView.Adapter<ProdFeaturesAdapter.Holder> {

    List<AllProductFeatureModel> list;
    Activity activity;

    public ProdFeaturesAdapter(List<AllProductFeatureModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.prod_feature_item,null);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        AllProductFeatureModel current=list.get(i);
        holder.f_value.setText(current.getFeature());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{
        TextView f_value;
        public Holder(@NonNull View itemView) {
            super(itemView);
            f_value=itemView.findViewById(R.id.feature_value);
        }
    }

}
