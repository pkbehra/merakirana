package com.example.Model;


public class ProdImageSliderModel {
    String id,prodImage;

    public ProdImageSliderModel() {
    }

    public ProdImageSliderModel(String id, String prodImage) {
        this.id = id;
        this.prodImage = prodImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProdImage() {
        return prodImage;
    }

    public void setProdImage(String prodImage) {
        this.prodImage = prodImage;
    }
}
