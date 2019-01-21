package com.example.sss.wel.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class  AgentData {

    @SerializedName("status")
    String Status;

    @SerializedName("message")
    String Message;

    @SerializedName("user_data")
    List<AgentLoginData> Data;


    public AgentData(String status, String message, List<AgentLoginData> data) {
        Status = status;
        Message = message;
        Data = data;

    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public List<AgentLoginData> getData() {
        return Data;
    }

    public void setData(List<AgentLoginData> data) {
        Data = data;
    }


}
