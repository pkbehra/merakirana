package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MaincategoryBaseModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allmaincategories")
    @Expose
    private List<MainCategoryModel> allmaincategories = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MainCategoryModel> getAllmaincategories() {
        return allmaincategories;
    }

    public void setAllmaincategories(List<MainCategoryModel> allmaincategories) {
        this.allmaincategories = allmaincategories;
    }
}
