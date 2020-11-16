package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCartDetails {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("wallet")
    @Expose
    private String wallet;
    @SerializedName("cgst")
    @Expose
    private String cgst;
    @SerializedName("sgst")
    @Expose
    private String sgst;

    @SerializedName("tax")
    @Expose
    private String tax;

    @SerializedName("taxprice")
    @Expose
    private Float taxprice;

    @SerializedName("cgstprice")
    @Expose
    private Float cgstprice;
    @SerializedName("sgstprice")
    @Expose
    private Float sgstprice;
    @SerializedName("cutbalance")
    @Expose
    private Float cutbalance;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharges;
    @SerializedName("total_price")
    @Expose
    private Float final_total;
    @SerializedName("final_total")
    @Expose
    private Float payprice;
    @SerializedName("paidprice")
    @Expose
    private Float paidprice;


    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public Float getTaxprice() {
        return taxprice;
    }

    public void setTaxprice(Float taxprice) {
        this.taxprice = taxprice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getWallet() {
        return wallet;
    }

    public void setWallet(String wallet) {
        this.wallet = wallet;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public Float getCgstprice() {
        return cgstprice;
    }

    public void setCgstprice(Float cgstprice) {
        this.cgstprice = cgstprice;
    }

    public Float getSgstprice() {
        return sgstprice;
    }

    public void setSgstprice(Float sgstprice) {
        this.sgstprice = sgstprice;
    }

    public Float getCutbalance() {
        return cutbalance;
    }

    public void setCutbalance(Float cutbalance) {
        this.cutbalance = cutbalance;
    }

    public String getDeliveryCharges() {
        return deliveryCharges;
    }

    public void setDeliveryCharges(String deliveryCharges) {
        this.deliveryCharges = deliveryCharges;
    }

    public Float getTotalPrice() {
        return final_total;
    }

    public void setTotalPrice(Float totalPrice) {
        this.final_total = totalPrice;
    }

    public Float getPayprice() {
        return payprice;
    }

    public void setPayprice(Float payprice) {
        this.payprice = payprice;
    }

    public Float getPaidprice() {
        return paidprice;
    }

    public void setPaidprice(Float paidprice) {
        this.paidprice = paidprice;
    }

}
