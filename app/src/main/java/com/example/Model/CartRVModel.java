package com.example.Model;

public class CartRVModel {

    private String id,productId,mainCategoryAutoId,mainCategoryId,subCategoryAutoId,
            subCategoryId,name,logo,price,descriptions,status,taxPrice,taxPercent;
    public int prodQty=1;

    public CartRVModel() {
    }

    public CartRVModel(String id, String productId, String mainCategoryAutoId, String mainCategoryId,
                       String subCategoryAutoId, String subCategoryId, String name, String logo, String price,
                       String descriptions, String status, int prodQty,String taxPrice,String taxPercent) {
        this.id = id;
        this.productId = productId;
        this.mainCategoryAutoId = mainCategoryAutoId;
        this.mainCategoryId = mainCategoryId;
        this.subCategoryAutoId = subCategoryAutoId;
        this.subCategoryId = subCategoryId;
        this.name = name;
        this.logo = logo;
        this.price = price;
        this.descriptions = descriptions;
        this.status = status;
        this.prodQty = prodQty;
        this.taxPercent = taxPercent;
        this.taxPrice = taxPrice;

    }

    public String getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(String taxPrice) {
        this.taxPrice = taxPrice;
    }

    public String getTaxPercent() {
        return taxPercent;
    }

    public void setTaxPercent(String taxPercent) {
        this.taxPercent = taxPercent;
    }

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

    public String getSubCategoryAutoId() {
        return subCategoryAutoId;
    }

    public void setSubCategoryAutoId(String subCategoryAutoId) {
        this.subCategoryAutoId = subCategoryAutoId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProdQty() {
        return prodQty;
    }

    public void setProdQty(int prodQty) {
        this.prodQty = prodQty;
    }
}
