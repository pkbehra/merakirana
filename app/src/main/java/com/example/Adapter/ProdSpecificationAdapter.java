package com.example.Adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Model.AllProductSpecificationModel;
import com.example.merakirana.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProdSpecificationAdapter extends RecyclerView.Adapter<ProdSpecificationAdapter.Holder> {

    List<AllProductSpecificationModel> list;
    Activity activity;

    public ProdSpecificationAdapter(List<AllProductSpecificationModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View view=inflater.inflate(R.layout.prod_specification_item,null);
        return new Holder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        AllProductSpecificationModel current=list.get(i);

        holder.spec_key.setText(current.getSpecificationKey()+" :  ");
        holder.spec_value.setText(current.getSpecificationValue());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Holder extends RecyclerView.ViewHolder{

        TextView spec_key,spec_value;
        public Holder(@NonNull View itemView) {
            super(itemView);
            spec_key=itemView.findViewById(R.id.spec_key);
            spec_value=itemView.findViewById(R.id.spec_value);

        }
    }
}
