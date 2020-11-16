package com.example.Adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Interface.CancelOrderListener;
import com.example.Model.ChildModel;
import com.example.Model.ParentModel;
import com.example.Utility.SessionManager;
import com.example.merakirana.R;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.List;

public class CustomAdapter extends BaseExpandableListAdapter {

    Activity activity;
    Context context;
    SessionManager sessionManager;
    private List<ParentModel> deptList;
    CancelOrderListener cancelOrderListener;
    ExpandableListView recyclerView;
    String newfeaturesList;
    String[] featureslist;
    List<String> listOfFeature;



    public CustomAdapter(Context context, List<ParentModel> deptList, Activity activity, CancelOrderListener cancelOrderListener, ExpandableListView recyclerView) {
        this.context = context;
        this.deptList = deptList;
        this.activity = activity;
        sessionManager = new SessionManager(activity);
        this.cancelOrderListener=cancelOrderListener;
        this.recyclerView=recyclerView;
        newfeaturesList=sessionManager.getFeaturesArray();
        featureslist = newfeaturesList.split(",");
        listOfFeature = Arrays.asList(featureslist);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        List<ChildModel> productList = deptList.get(groupPosition).getChildItemList();
        return productList.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                             View view, ViewGroup parent) {

        ChildModel detailInfo = (ChildModel) getChild(groupPosition, childPosition);

        if (view == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // view = infalInflater.inflate(R.layout., null);
            view = infalInflater.inflate(R.layout.order_child_listitem, null);

        }


        TextView mProdName = (TextView) view.findViewById(R.id.prod_name_id);

        TextView mProdPrice = (TextView) view.findViewById(R.id.prod_price_id);
        TextView mProdquntity = (TextView) view.findViewById(R.id.qty_val_id);
        TextView qty_id = (TextView) view.findViewById(R.id.qty_id);
        ImageView imageView = (ImageView) view.findViewById(R.id.prod_image_id);


//        Animation aniSlide = AnimationUtils.loadAnimation(context,R.anim.slide_down);
//        cardView.startAnimation(aniSlide);
        Picasso.with(context)
                .load(detailInfo.getProduct_image())
                .error(R.drawable.logo)
                .placeholder(R.drawable.loader)
                .fit().centerInside()
                .into(imageView);
        // mProdName.setText(detailInfo.getProduct_nameenglish() + "  " + "(" + detailInfo.getProduct_weight() + ")");
        mProdName.setText(detailInfo.getProduct_nameenglish());
        mProdPrice.setText("$ " + detailInfo.getProduct_price());
        mProdquntity.setText(detailInfo.getProduct_quanity());
        return view;

    }

    @Override
    public int getChildrenCount(int groupPosition) {

        List<ChildModel> productList = deptList.get(groupPosition).getChildItemList();
        return productList.size();

    }

    @Override
    public Object getGroup(int groupPosition) {
        return deptList.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return deptList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isLastChild, View view,
                             ViewGroup parent) {

        final ParentModel headerInfo = (ParentModel) getGroup(groupPosition);
        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inf.inflate(R.layout.cosmorder_parent_listitem, null);


        }

        TextView mOrderId = (TextView) view.findViewById(R.id.order_id);
        TextView mOrderTime = (TextView) view.findViewById(R.id.order_date_id);
        TextView mOrderSubtotal = (TextView) view.findViewById(R.id.subtotal_id);
        TextView mOrderDelivery_charges = (TextView) view.findViewById(R.id.delivery_charges_id);
        TextView mOrderAllTotal = (TextView) view.findViewById(R.id.total_id);
        TextView showmoreless = (TextView) view.findViewById(R.id.tvShowMore);

        TextView subtotaltext = (TextView) view.findViewById(R.id.subtotaltext);
        TextView deliverychargestext = (TextView) view.findViewById(R.id.deliverychargestext);
        TextView totalpricetext = (TextView) view.findViewById(R.id.totalpricetext);
        Button btn_cancel=(Button)view.findViewById(R.id.btn_cancel);
        ImageView imgCancelStamp=(ImageView)view.findViewById(R.id.imgCancelStamp);
        LinearLayout llmyorders=(LinearLayout)view.findViewById(R.id.llmyorders);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrderListener.onClickCancelOrder(headerInfo.getOrderId());

            }
        });


        mOrderId.setText(headerInfo.getOrderId());
        mOrderTime.setText(headerInfo.getOrder_time());
        mOrderDelivery_charges.setText(" $ " + headerInfo.getDelivery_charges());
        mOrderSubtotal.setText("$ " + headerInfo.getSub_total());
        mOrderAllTotal.setText("$ " + headerInfo.getTotal_price());
        if (isLastChild) {
            showmoreless.setText("Show Less");

        } else {
            showmoreless.setText("Show More");

        }





        llmyorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(recyclerView.isGroupExpanded(groupPosition)){
                    recyclerView.collapseGroup(groupPosition);
                }else {
                    recyclerView.expandGroup(groupPosition);
                }
            }
        });


        //

        if (listOfFeature.contains("Cancel Order")) {
            btn_cancel.setVisibility(View.VISIBLE);

            //check is order is cancelled
            if(headerInfo.getStatus().equalsIgnoreCase("Cancelled"))
            {
                imgCancelStamp.setVisibility(View.VISIBLE);
                btn_cancel.setVisibility(View.GONE);
                //btn_cancel.setEnabled(false);

            }else {
                imgCancelStamp.setVisibility(View.GONE);

                //btn_cancel.setVisibility(View.VISIBLE);
                // btn_cancel.setEnabled(true);
            }
        }else {
            btn_cancel.setVisibility(View.GONE);
            imgCancelStamp.setVisibility(View.GONE);
        }



        return view;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }




}