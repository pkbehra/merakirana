package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allcategories")
    @Expose
    private List<CategoryModel> allcategories = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<CategoryModel> getAllcategories() {
        return allcategories;
    }

    public void setAllcategories(List<CategoryModel> allcategories) {
        this.allcategories = allcategories;
    }
}
