package com.shamgar.sss.wel.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.shamgar.sss.wel.R;

public class SharedPreferenceConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferenceConfig(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);

    }
    public void writeAgentGender(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_gender_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentGender(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_gender_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentRefID(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_ref_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentRefID(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_ref_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeAgentLoggedIn(String logged){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_login_preference), logged);
        Log.i("SharedPreferanceWrite: ",""+logged);
        editor.commit();
    }

    public String readAgentLoggedin(){
        String logged;
        logged = sharedPreferences.getString(context.getResources().getString(R.string.agent_login_preference),"no");
        Log.i("SharedPreferanceRead: ",""+logged);
        return logged;
    }

    public void writeAgentEmail(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_emial_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentEmail(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_emial_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentphone(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_phone_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentPhone(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_phone_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentLocation(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_location_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentLocation(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_location_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentPassword(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_confirm_pass_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentPassword(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_confirm_pass_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeAgentAadhar(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_aadhar_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentAadhar(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_aadhar_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentLatitude(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_latitude_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentLatitude(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_latitude_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentLongitude(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_longitude_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentLongitude(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_longitude_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }


    public void writeAgentBankName(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_bank_name_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentBankName(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_bank_name_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentBankNumber(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_bank_account_no__preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentBankNumber(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_bank_account_no__preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentBankIfsc(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_bank_ifsc_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentBankIfsc(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_bank_ifsc_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeAgentPanNumber(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_pan_num_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentPanNumber(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_pan_num_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeAgentDob(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_dob_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentDob(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_dob_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeAgentPic(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.agent_pan_num_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readAgentPic(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.agent_pan_num_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderPic(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_pic_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderPic(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_pic_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderName(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_name_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderName(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_name_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeProviderPhone(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_phone_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderPhone(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_phone_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderLocation(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_location_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderLocation(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_location_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeProviderService(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_service_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderService(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_service_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeProviderWebsite(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_website_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderWebsite(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_website_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeProviderGender(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_gender_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderGender(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_gender_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderDob(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_dob_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderDob(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_dob_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderServiceDesc(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_serviceDesc_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderServiceDesc(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_serviceDesc_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderREfID(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_refID_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderRefID(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_refID_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderDiscount(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_discount_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderDiscount(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_discount_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderStatus(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_status_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderStatus(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_status_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderLatitude(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_latitude_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderLatitude(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_latitude_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }

    public void writeProviderLongitude(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_longitude_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderLongitude(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_longitude_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
    public void writeProviderLandmark(String gender){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getResources().getString(R.string.provider_landmark_preference), gender);
        Log.i("SharedPreferanceWrite: ",""+gender);
        editor.commit();
    }

    public String readProviderLandmark(){
        String gender;
        gender = sharedPreferences.getString(context.getResources().getString(R.string.provider_landmark_preference),"no");
        Log.i("SharedPreferanceRead: ",""+gender);
        return gender;
    }
}
