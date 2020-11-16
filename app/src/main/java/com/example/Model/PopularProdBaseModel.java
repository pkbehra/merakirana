package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PopularProdBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allproducts")
    @Expose
    private List<PopularProdModel> allproducts = null;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<PopularProdModel> getAllproducts() {
        return allproducts;
    }

    public void setAllproducts(List<PopularProdModel> allproducts) {
        this.allproducts = allproducts;
    }
}
