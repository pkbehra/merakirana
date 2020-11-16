package com.example.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Model.MenuModel;
import com.example.Utility.SecondExpandableListView;
import com.example.merakirana.R;

import java.util.HashMap;
import java.util.List;

public class SubChildAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MenuModel> listHeader;
    private HashMap<MenuModel,List<MenuModel>> listChild;
    private HashMap<MenuModel,List<MenuModel>> listsubchild;
    private SecondExpandableListView secondlistView;

    public SubChildAdapter(Context context, List<MenuModel> listHeader, HashMap<MenuModel, List<MenuModel>> listChild, HashMap<MenuModel, List<MenuModel>> listsubchild) {
        this.context = context;
        this.listHeader = listHeader;
        this.listChild = listChild;
        this.listsubchild = listsubchild;
    }


    @Override
    public int getGroupCount() {
        return listHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public MenuModel getGroup(int groupPosition) {
        return this.listHeader.get(groupPosition);
    }

    @Override
    public MenuModel getChild(int groupPosition, int childPosition) {
        return this.listChild.get(this.listHeader.get(groupPosition))
                .get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final String headerTitle = getGroup(groupPosition).getMenuName();
        final int headIcon=getGroup(groupPosition).getImg();
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_head, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.headTitle);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView lblListIcon=convertView.findViewById(R.id.headIcon);
        lblListIcon.setImageResource(headIcon);

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
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        secondlistView=new SecondExpandableListView(context);

        final int gPos=groupPosition;
        final int cPos=childPosition;
        //sub menu header
        final List<MenuModel> secondHeader=listChild.get(listHeader.get(groupPosition));

        if (getGroup(groupPosition).isChild()){
            secondlistView.setAdapter(new CategoryExpandableListAdapter(context,secondHeader,listsubchild));

            secondlistView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                int previousGroup = -1;

                @Override
                public void onGroupExpand(int groupPosition) {
                    if(groupPosition != previousGroup)
                        secondlistView.collapseGroup(previousGroup);
                    previousGroup = groupPosition;
                }
            });

/*
            secondlistView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    if (listsubchild.get(secondHeader.get(groupPosition))!=null){

                        Intent intent=new Intent(context.getApplicationContext(), Main_Category_Products.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("p_type","sub_category");
                        context.getApplicationContext().startActivity(intent);
                        //  }
//
                    }
                    return false;
                }
            });
*/

        }
        return secondlistView;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
