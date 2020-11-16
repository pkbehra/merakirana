package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SliderBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("allslider")
    @Expose
    private List<SliderImgModel> allslider = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<SliderImgModel> getAllslider() {
        return allslider;
    }

    public void setAllslider(List<SliderImgModel> allslider) {
        this.allslider = allslider;
    }
}
