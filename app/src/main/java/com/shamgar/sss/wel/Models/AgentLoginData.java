package com.shamgar.sss.wel.Models;
public class AgentLoginData {

    String username;
    String user_id;
    String token;
    String profile_pic;
    String logged_in;

    public AgentLoginData(String username, String user_id, String token, String profile_pic, String logged_in) {
        this.username = username;
        this.user_id = user_id;
        this.token = token;
        this.profile_pic = profile_pic;
        this.logged_in = logged_in;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getLogged_in() {
        return logged_in;
    }

    public void setLogged_in(String logged_in) {
        this.logged_in = logged_in;
    }
}
