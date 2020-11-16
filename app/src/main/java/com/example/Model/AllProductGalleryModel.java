package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllProductGalleryModel {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("logo")
    @Expose
    private String logo;

    public AllProductGalleryModel(String id, String logo) {
        this.id = id;
        this.logo = logo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
