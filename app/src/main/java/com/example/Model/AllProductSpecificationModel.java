package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllProductSpecificationModel {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("specification_key")
    @Expose
    private String specificationKey;
    @SerializedName("specification_value")
    @Expose
    private String specificationValue;

    public AllProductSpecificationModel(String id, String specificationKey, String specificationValue) {
        this.id = id;
        this.specificationKey = specificationKey;
        this.specificationValue = specificationValue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSpecificationKey() {
        return specificationKey;
    }

    public void setSpecificationKey(String specificationKey) {
        this.specificationKey = specificationKey;
    }

    public String getSpecificationValue() {
        return specificationValue;
    }

    public void setSpecificationValue(String specificationValue) {
        this.specificationValue = specificationValue;
    }
}
