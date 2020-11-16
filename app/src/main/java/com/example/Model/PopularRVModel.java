package com.example.Model;


public class PopularRVModel {


    public String prodName, prodPrice, productID,prodImage;
    public int img, prodQty;

    public PopularRVModel() {
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProdImage() {
        return img;
    }

    public void setProdImage(int img) {
        this.img = img;
    }

    public int getProdQty() {
        return prodQty;
    }

    public void setProdQty(int prodQty) {
        this.prodQty = prodQty;
    }



    public String getProductImage() {
        return prodImage;
    }

    public void setProductImage(String prodImage) {
        this.prodImage = prodImage;
    }


}
