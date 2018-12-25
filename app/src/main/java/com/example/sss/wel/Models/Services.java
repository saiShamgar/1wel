package com.example.sss.wel.Models;

import com.google.gson.annotations.SerializedName;

public class Services {

        @SerializedName("service_id")
        String Service_id;

        @SerializedName("service")
        String Service;

    public Services(String service_id, String service) {
       this.Service_id = service_id;
        this.Service = service;
    }

    public String getService_id() {
        return Service_id;
    }

    public void setService_id(String service_id) {
        this.Service_id = service_id;
    }

    public String getService() {
        return Service;
    }

    public void setService(String service) {
        this.Service = service;
    }
}
