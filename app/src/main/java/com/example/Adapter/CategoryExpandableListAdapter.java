package com.example.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Model.MenuModel;
import com.example.merakirana.Main_Category_Products;
import com.example.merakirana.R;

import java.util.HashMap;
import java.util.List;

public class CategoryExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MenuModel> listHeader;
    private HashMap<MenuModel,List<MenuModel>> listChild;

    public CategoryExpandableListAdapter(Context context, List<MenuModel> listHeader, HashMap<MenuModel, List<MenuModel>> listChild) {
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = getChild(groupPosition, childPosition).getMenuName();
        final String subCategoryId=getChild(groupPosition,childPosition).getAuto_id();
        // final int childIcon=getChild(groupPosition,childPosition).getMenuIcon();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_child, null);
        }

        TextView txtListChild = convertView.findViewById(R.id.childTitle);
        LinearLayout llChildView=convertView.findViewById(R.id.llChild);

        txtListChild.setText(childText);

        llChildView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("sub_category_auto_id",subCategoryId);
                Intent intent=new Intent(context, Main_Category_Products.class);
                intent.putExtra("category_id",subCategoryId);//this is sub category Id
                intent.putExtra("p_type","sub_category");
                intent.putExtra("Category_Name",childText);
                context.startActivity(intent);
            }
        });

        //ImageView listChildIcon=convertView.findViewById(R.id.childIcon);

       /* ImageView plusBtn=convertView.findViewById(R.id.arrow);
        if (getGroup(groupPosition).isChildren()){
            plusBtn.setVisibility(View.VISIBLE);
        }
        else {
            plusBtn.setVisibility(View.GONE);
        }*/
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (this.listChild.get(this.listHeader.get(groupPosition)) == null)
            return 0;
        else
            return this.listChild.get(this.listHeader.get(groupPosition))
                    .size();    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String headerTitle = getGroup(groupPosition).getMenuName();
        final int headerIcon = getGroup(groupPosition).getImg();

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_head, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.headTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView lblListIcon=convertView.findViewById(R.id.headIcon);
        lblListIcon.setImageResource(headerIcon);

        ImageView plusBtn=convertView.findViewById(R.id.arrow);

        if (getGroup(groupPosition).isChild()){
            plusBtn.setVisibility(View.VISIBLE);
        }
        else {
            plusBtn.setVisibility(View.GONE);
        }

        return convertView;
    }

    @Override
    public int getGroupCount() {
        return this.listHeader.size();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
