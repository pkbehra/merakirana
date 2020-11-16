package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllProductFeatureModel {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("feature")
    @Expose
    private String feature;

    public AllProductFeatureModel(String id, String feature) {
        this.id = id;
        this.feature = feature;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFeature() {
        return feature;
    }

    public void setFeature(String feature) {
        this.feature = feature;
    }
}
