package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubCategoryModel {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("sub_category_id")
    @Expose
    private String subCategoryId;
    @SerializedName("main_category_auto_id")
    @Expose
    private String mainCategoryAutoId;
    @SerializedName("main_category_id")
    @Expose
    private String mainCategoryId;
    @SerializedName("main_category_name")
    @Expose
    private String mainCategoryName;


    @SerializedName("sub_category_name")
    @Expose
    private String subcategoryname;

    public String getSubcategoryname() {
        return subcategoryname;
    }

    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getMainCategoryAutoId() {
        return mainCategoryAutoId;
    }

    public void setMainCategoryAutoId(String mainCategoryAutoId) {
        this.mainCategoryAutoId = mainCategoryAutoId;
    }

    public String getMainCategoryId() {
        return mainCategoryId;
    }

    public void setMainCategoryId(String mainCategoryId) {
        this.mainCategoryId = mainCategoryId;
    }

    public String getMainCategoryName() {
        return mainCategoryName;
    }

    public void setMainCategoryName(String mainCategoryName) {
        this.mainCategoryName = mainCategoryName;
    }
}
