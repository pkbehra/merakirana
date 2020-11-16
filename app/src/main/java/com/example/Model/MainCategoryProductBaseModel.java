package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MainCategoryProductBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allproducts")
    @Expose
    private List<MainCategoryProductsModel> allproducts = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MainCategoryProductsModel> getAllproducts() {
        return allproducts;
    }

    public void setAllproducts(List<MainCategoryProductsModel> allproducts) {
        this.allproducts = allproducts;
    }
}
