package com.shamgar.sss.wel.Models;

import com.google.gson.annotations.SerializedName;

public class SearchItems {

    @SerializedName("username")
    String name;
    @SerializedName("user_id")
    String user_id;
    @SerializedName("phone")
    String phone;
    @SerializedName("service")
    String service;
    @SerializedName("address")
    String address;
    @SerializedName("website_url")
    String webSiteUrl;
    @SerializedName("service_desc")
    String service_des;
    @SerializedName("profile_pic")
    String image;

    public SearchItems(String name, String user_id, String phone, String service, String address, String webSiteUrl, String service_des, String image) {
        this.name = name;
        this.user_id = user_id;
        this.phone = phone;
        this.service = service;
        this.address = address;
        this.webSiteUrl = webSiteUrl;
        this.service_des = service_des;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebSiteUrl() {
        return webSiteUrl;
    }

    public void setWebSiteUrl(String webSiteUrl) {
        this.webSiteUrl = webSiteUrl;
    }

    public String getService_des() {
        return service_des;
    }

    public void setService_des(String service_des) {
        this.service_des = service_des;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}