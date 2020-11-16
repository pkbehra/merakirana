package com.example.Model;


import com.bignerdranch.expandablerecyclerview.Model.ParentListItem;

import java.util.ArrayList;
import java.util.List;

public class ParentModel implements ParentListItem {

    String orderId;
    String order_date;
    String order_time;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

    public String getPrdoctlist() {
        return prdoctlist;
    }

    public void setPrdoctlist(String prdoctlist) {
        this.prdoctlist = prdoctlist;
    }

    String prdoctlist;

    String total_price;
    List<ChildModel> childModels ;
    ArrayList children;

    public void setChildModels(List<ChildModel> childModels) {
        this.childModels = childModels;
    }



    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDelivery_charges() {
        return delivery_charges;
    }

    public void setDelivery_charges(String delivery_charges) {
        this.delivery_charges = delivery_charges;
    }

    String sub_total;
    String delivery_charges;


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    @Override
    public List<ChildModel> getChildItemList() {
        return childModels;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return true;
    }
}
