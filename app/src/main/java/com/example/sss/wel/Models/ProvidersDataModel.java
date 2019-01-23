package com.example.sss.wel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProvidersDataModel {

    @SerializedName("status")
    String Status;

    @SerializedName("message")
    List<SearchItems> Message;

    public ProvidersDataModel(String status, List<SearchItems> message) {
        Status = status;
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public List<SearchItems> getMessage() {
        return Message;
    }

    public void setMessage(List<SearchItems> message) {
        Message = message;
    }
}
