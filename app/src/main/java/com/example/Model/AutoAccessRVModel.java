package com.example.Model;


public class AutoAccessRVModel {

    // Image productImage;
    public String title,price,id,desp;
    public int img;

    public AutoAccessRVModel(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDesp(){
        return desp;

    }

    public void setDesp(String desp){
        this.desp=desp;

    }



}
