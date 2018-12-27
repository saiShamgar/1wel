package com.example.sss.wel.Models;

import com.google.gson.annotations.SerializedName;

public class Status {
    @SerializedName("status")
    String Status;

    @SerializedName("message")
    String Message;

    public Status(String status, String message) {
        Status = status;
        Message = message;
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
}
