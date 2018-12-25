package com.example.sss.wel.Models;

import com.google.gson.annotations.SerializedName;

public class AgentRegistration {

    @SerializedName("username")
    String agentEmail;
    @SerializedName("phone")
    String agentPhone;
    @SerializedName("latitude")
    long agentLatitude;
    @SerializedName("longitude")
    long agentLongitutde;
    @SerializedName("password")
    String agentPassword;
    @SerializedName("aadhar_no")
    long agentAadhar;
    @SerializedName("bank_name")
    String agentBankName;
    @SerializedName("bank_ac_no")
    long agentBankAccount;
    @SerializedName("bank_ifsc_code")
    String agentBankIfsc;
    @SerializedName("pancard_no")
    String agentPanNum;
    @SerializedName("gender")
    String agentGender;
    @SerializedName("profile_pic")
    String agentImage;
    @SerializedName("address")
    String agentAddress;
    @SerializedName("user_type")
    String User_type;

    public String getUser_type() {
        return User_type;
    }

    public void setUser_type(String user_type) {
        User_type = user_type;
    }

    public AgentRegistration(String agentEmail, String agentPhone, long agentLatitude, long agentLongitutde, String agentPassword, long agentAadhar, String agentBankName, long agentBankAccount, String agentBankIfsc, String agentPanNum, String agentGender, String agentImage, String agentAddress, String user_type) {

        this.agentEmail = agentEmail;
        this.agentPhone = agentPhone;
        this.agentLatitude = agentLatitude;
        this.agentLongitutde = agentLongitutde;
        this.agentPassword = agentPassword;
        this.agentAadhar = agentAadhar;
        this.agentBankName = agentBankName;
        this.agentBankAccount = agentBankAccount;
        this.agentBankIfsc = agentBankIfsc;
        this.agentPanNum = agentPanNum;
        this.agentGender = agentGender;
        this.agentImage = agentImage;
        this.agentAddress = agentAddress;
        User_type = user_type;
    }

    public String getAgentEmail() {
        return agentEmail;
    }

    public void setAgentEmail(String agentEmail) {
        this.agentEmail = agentEmail;
    }

    public String getAgentPhone() {
        return agentPhone;
    }

    public void setAgentPhone(String agentPhone) {
        this.agentPhone = agentPhone;
    }

    public long getAgentLatitude() {
        return agentLatitude;
    }

    public void setAgentLatitude(long agentLatitude) {
        this.agentLatitude = agentLatitude;
    }

    public long getAgentLongitutde() {
        return agentLongitutde;
    }

    public void setAgentLongitutde(long agentLongitutde) {
        this.agentLongitutde = agentLongitutde;
    }

    public String getAgentPassword() {
        return agentPassword;
    }

    public void setAgentPassword(String agentPassword) {
        this.agentPassword = agentPassword;
    }

    public long getAgentAadhar() {
        return agentAadhar;
    }

    public void setAgentAadhar(long agentAadhar) {
        this.agentAadhar = agentAadhar;
    }

    public String getAgentBankName() {
        return agentBankName;
    }

    public void setAgentBankName(String agentBankName) {
        this.agentBankName = agentBankName;
    }

    public long getAgentBankAccount() {
        return agentBankAccount;
    }

    public void setAgentBankAccount(long agentBankAccount) {
        this.agentBankAccount = agentBankAccount;
    }

    public String getAgentBankIfsc() {
        return agentBankIfsc;
    }

    public void setAgentBankIfsc(String agentBankIfsc) {
        this.agentBankIfsc = agentBankIfsc;
    }

    public String getAgentPanNum() {
        return agentPanNum;
    }

    public void setAgentPanNum(String agentPanNum) {
        this.agentPanNum = agentPanNum;
    }

    public String getAgentGender() {
        return agentGender;
    }

    public void setAgentGender(String agentGender) {
        this.agentGender = agentGender;
    }

    public String getAgentImage() {
        return agentImage;
    }

    public void setAgentImage(String agentImage) {
        this.agentImage = agentImage;
    }

    public String getAgentAddress() {
        return agentAddress;
    }

    public void setAgentAddress(String agentAddress) {
        this.agentAddress = agentAddress;
    }
}
