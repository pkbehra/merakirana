package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryModel {
    @SerializedName("main_category_name")
    @Expose
    private String mainCategoryName;
    @SerializedName("subcategory")
    @Expose
    private List<SubCategoryModel> subcategory = null;

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }

    public List<SubCategoryModel> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(List<SubCategoryModel> subcategory) {
        this.subcategory = subcategory;
    }
}