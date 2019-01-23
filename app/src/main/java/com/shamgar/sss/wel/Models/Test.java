package com.shamgar.sss.wel.Models;

import com.google.gson.annotations.SerializedName;

public class Test {



  //  http://www.yesinteriors.online/1wel/users_list/users/reg?username=ramesh&password=666666&phone=6534545

    @SerializedName("username")
    String Username;

    @SerializedName("password")
    String Password;

    @SerializedName("phone")
    String Phone;


    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public Test(String username, String password, String phone) {

        Username = username;
        Password = password;
        Phone = phone;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }
}
