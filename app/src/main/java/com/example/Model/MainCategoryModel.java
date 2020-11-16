package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainCategoryModel {
    @SerializedName("_id")
    @Expose
    private String main_category_auto_id;
    @SerializedName("main_category_id")
    @Expose
    private String mainCategoryId;
    @SerializedName("main_category_name")
    @Expose
    private String mainCategoryName;
    @SerializedName("logo")
    @Expose
    private String logo;


    public String getMain_category_auto_id() {
        return main_category_auto_id;
    }

    public void setMain_category_auto_id(String id) {
        this.main_category_auto_id = id;
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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
