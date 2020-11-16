package com.example.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionBaseModel {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("credited_history")
    @Expose
    private List<TransactionsModel> creditedHistory = null;
    @SerializedName("debitted_history")
    @Expose
    private List<TransactionsModel> debittedHistory = null;

    public TransactionBaseModel(Integer status, List<TransactionsModel> creditedHistory, List<TransactionsModel> debittedHistory) {
        this.status = status;
        this.creditedHistory = creditedHistory;
        this.debittedHistory = debittedHistory;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<TransactionsModel> getCreditedHistory() {
        return creditedHistory;
    }

    public void setCreditedHistory(List<TransactionsModel> creditedHistory) {
        this.creditedHistory = creditedHistory;
    }

    public List<TransactionsModel> getDebittedHistory() {
        return debittedHistory;
    }

    public void setDebittedHistory(List<TransactionsModel> debittedHistory) {
        this.debittedHistory = debittedHistory;
    }
}
