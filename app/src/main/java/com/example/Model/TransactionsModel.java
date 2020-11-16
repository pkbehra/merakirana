package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionsModel {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("my_customer_auto_id")
    @Expose
    private String myCustomerAutoId;
    @SerializedName("my_contact")
    @Expose
    private String myContact;
    @SerializedName("transfer_contact")
    @Expose
    private String transferContact;
    @SerializedName("transfer_amount")
    @Expose
    private String transferAmount;
    @SerializedName("credited_customer_auto_id")
    @Expose
    private String creditedCustomerAutoId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("transfer_country_code")
    @Expose
    private String transfer_country_code;

    String trans_type;

    public TransactionsModel(String id, String myCustomerAutoId, String myContact, String transferContact, String transferAmount, String creditedCustomerAutoId, String updatedAt, String createdAt, String trans_type,String transfer_country_code) {
        this.id = id;
        this.myCustomerAutoId = myCustomerAutoId;
        this.myContact = myContact;
        this.transferContact = transferContact;
        this.transferAmount = transferAmount;
        this.creditedCustomerAutoId = creditedCustomerAutoId;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.trans_type = trans_type;
        this.transfer_country_code = transfer_country_code;
    }

    public String getTrans_type() {
        return trans_type;
    }

    public void setTrans_type(String trans_type) {
        this.trans_type = trans_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMyCustomerAutoId() {
        return myCustomerAutoId;
    }

    public void setMyCustomerAutoId(String myCustomerAutoId) {
        this.myCustomerAutoId = myCustomerAutoId;
    }

    public String getMyContact() {
        return myContact;
    }

    public void setMyContact(String myContact) {
        this.myContact = myContact;
    }

    public String getTransferContact() {
        return transferContact;
    }

    public void setTransferContact(String transferContact) {
        this.transferContact = transferContact;
    }

    public String getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(String transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getCreditedCustomerAutoId() {
        return creditedCustomerAutoId;
    }

    public void setCreditedCustomerAutoId(String creditedCustomerAutoId) {
        this.creditedCustomerAutoId = creditedCustomerAutoId;
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

    public String getTransfer_country_code() {
        return transfer_country_code;
    }

    public void setTransfer_country_code(String transfer_country_code) {
        this.transfer_country_code = transfer_country_code;
    }
}
