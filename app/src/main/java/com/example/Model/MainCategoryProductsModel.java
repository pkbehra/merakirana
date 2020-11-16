package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainCategoryProductsModel {


    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("product_main_category_auto_id")
    @Expose
    private String productMainCategoryAutoId;
    @SerializedName("product_main_category_id")
    @Expose
    private String productMainCategoryId;
    @SerializedName("product_main_category_name")
    @Expose
    private String productMainCategoryName;
    @SerializedName("product_sub_category_auto_id")
    @Expose
    private String productSubCategoryAutoId;
    @SerializedName("product_sub_category_id")
    @Expose
    private String productSubCategoryId;
    @SerializedName("product_sub_category_name")
    @Expose
    private String productSubCategoryName;
    @SerializedName("product_name_english")
    @Expose
    private String productNameEnglish;
    @SerializedName("product_logo")
    @Expose
    private String productLogo;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("product_qty")
    @Expose
    private String productQty;
    @SerializedName("product_weight")
    @Expose
    private String productWeight;
    @SerializedName("descriptions_english")
    @Expose
    private String descriptionsEnglish;
    @SerializedName("new_arrival_status")
    @Expose
    private String newArrivalStatus;
    @SerializedName("product_status")
    @Expose
    private String productStatus;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("product_name")
    @Expose
    private String productName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductMainCategoryAutoId() {
        return productMainCategoryAutoId;
    }

    public void setProductMainCategoryAutoId(String productMainCategoryAutoId) {
        this.productMainCategoryAutoId = productMainCategoryAutoId;
    }

    public String getProductMainCategoryId() {
        return productMainCategoryId;
    }

    public void setProductMainCategoryId(String productMainCategoryId) {
        this.productMainCategoryId = productMainCategoryId;
    }

    public String getProductMainCategoryName() {
        return productMainCategoryName;
    }

    public void setProductMainCategoryName(String productMainCategoryName) {
        this.productMainCategoryName = productMainCategoryName;
    }

    public String getProductSubCategoryAutoId() {
        return productSubCategoryAutoId;
    }

    public void setProductSubCategoryAutoId(String productSubCategoryAutoId) {
        this.productSubCategoryAutoId = productSubCategoryAutoId;
    }

    public String getProductSubCategoryId() {
        return productSubCategoryId;
    }

    public void setProductSubCategoryId(String productSubCategoryId) {
        this.productSubCategoryId = productSubCategoryId;
    }

    public String getProductSubCategoryName() {
        return productSubCategoryName;
    }

    public void setProductSubCategoryName(String productSubCategoryName) {
        this.productSubCategoryName = productSubCategoryName;
    }

    public String getProductNameEnglish() {
        return productNameEnglish;
    }

    public void setProductNameEnglish(String productNameEnglish) {
        this.productNameEnglish = productNameEnglish;
    }

    public String getProductLogo() {
        return productLogo;
    }

    public void setProductLogo(String productLogo) {
        this.productLogo = productLogo;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public String getProductWeight() {
        return productWeight;
    }

    public void setProductWeight(String productWeight) {
        this.productWeight = productWeight;
    }

    public String getDescriptionsEnglish() {
        return descriptionsEnglish;
    }

    public void setDescriptionsEnglish(String descriptionsEnglish) {
        this.descriptionsEnglish = descriptionsEnglish;
    }

    public String getNewArrivalStatus() {
        return newArrivalStatus;
    }

    public void setNewArrivalStatus(String newArrivalStatus) {
        this.newArrivalStatus = newArrivalStatus;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
