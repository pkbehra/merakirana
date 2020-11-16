package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductDetailBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allproduct_gallery")
    @Expose
    private List<AllProductGalleryModel> allproductGallery = null;
    @SerializedName("allproduct_features")
    @Expose
    private List<AllProductFeatureModel> allproductFeatures = null;
    @SerializedName("allproduct_specifications")
    @Expose
    private List<AllProductSpecificationModel> allproductSpecifications = null;

    public ProductDetailBaseModel(Integer status, List<AllProductGalleryModel> allproductGallery, List<AllProductFeatureModel> allproductFeatures, List<AllProductSpecificationModel> allproductSpecifications) {
        this.status = status;
        this.allproductGallery = allproductGallery;
        this.allproductFeatures = allproductFeatures;
        this.allproductSpecifications = allproductSpecifications;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AllProductGalleryModel> getAllproductGallery() {
        return allproductGallery;
    }

    public void setAllproductGallery(List<AllProductGalleryModel> allproductGallery) {
        this.allproductGallery = allproductGallery;
    }

    public List<AllProductFeatureModel> getAllproductFeatures() {
        return allproductFeatures;
    }

    public void setAllproductFeatures(List<AllProductFeatureModel> allproductFeatures) {
        this.allproductFeatures = allproductFeatures;
    }

    public List<AllProductSpecificationModel> getAllproductSpecifications() {
        return allproductSpecifications;
    }

    public void setAllproductSpecifications(List<AllProductSpecificationModel> allproductSpecifications) {
        this.allproductSpecifications = allproductSpecifications;
    }
}
