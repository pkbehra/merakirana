package com.example.Adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Model.MenuModel;
import com.example.merakirana.R;

import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<MenuModel> listTitles;

    private HashMap<MenuModel, List<MenuModel>> listDataChild;

    public ExpandableListAdapter(Context context, List<MenuModel> listTitle, HashMap<MenuModel, List<MenuModel>> listChildData) {
        this.mContext = context;
        this.listTitles = listTitle;
        this.listDataChild = listChildData;

    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosition) {
        return this.listDataChild.get(this.listTitles.get(groupPosition))
                .get(childPosition);
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText=getChild(groupPosition,childPosition).getMenuName();
        final int groupImage=getChild(groupPosition,childPosition).getImg();

//        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_items, null);

        }
        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.expandableListItem);
        txtListChild.setText(childText);


        ImageView childIcon = (ImageView) convertView.findViewById(R.id.tvGroupLogo);
        childIcon.setImageResource(groupImage);

        return convertView;


    }


    @Override
    public int getChildrenCount(int groupPosition) {
        if (this.listDataChild.get(this.listTitles.get(groupPosition)) == null)
            return 0;
        else
            return this.listDataChild.get(this.listTitles.get(groupPosition))
                    .size(); }

    @Override
    public MenuModel getGroup(int groupPosition) {

        return this.listTitles.get(groupPosition);
    }



    @Override
    public int getGroupCount() {
        return this.listTitles.size();

    }


    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition).getMenuName();
        int groupImage=getGroup(groupPosition).getImg();


        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.expandable_list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.expandableListTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView groupIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
        groupIcon.setImageResource(groupImage);

        return convertView;
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
