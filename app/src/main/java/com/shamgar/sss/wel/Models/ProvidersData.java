package com.shamgar.sss.wel.Models;

public class ProvidersData {

    String username;
    String phone;
    String profile_pic;
    String website_url;
    String address;
    String service_desc;
    String service;


    public ProvidersData(String username, String phone, String profile_pic, String website_url, String address, String service_desc, String service) {
        this.username = username;
        this.phone = phone;
        this.profile_pic = profile_pic;
        this.website_url = website_url;
        this.address = address;
        this.service_desc = service_desc;
        this.service = service;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getWebsite_url() {
        return website_url;
    }

    public void setWebsite_url(String website_url) {
        this.website_url = website_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getService_desc() {
        return service_desc;
    }

    public void setService_desc(String service_desc) {
        this.service_desc = service_desc;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
