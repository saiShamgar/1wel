package com.example.sss.wel.Api;

import com.example.sss.wel.Models.AgentRegistration;
import com.example.sss.wel.Models.Services;
import com.example.sss.wel.Models.Status;
import com.example.sss.wel.Models.Test;

import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit2.Call;

import retrofit2.Callback;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

import static com.example.sss.wel.Api.APIUrl.retrofit;

public interface ApiService {

    //api for getting all services
    @Headers("X-API-KEY:" + "SHANKAR@111")
    @GET("users_list/users/services/")
    Call<List<Services>> getServices(@Query("service_type") int service);


    //http://www.yesinteriors.online/1wel/users_list/users/reg?username=ramji
    // &password=666666&phone=6534545&address=viskahpatnam&
    // profile_pic=fgsdfgd&user_type=1&latitude=17.743210&
    // longitude=83.329773&aadhar_no=323543245354&
    // bank_name=state bank&bank_ac_no=4523443a&bank_ifsc_code=fsdfg43437&dob=1999/4/04&gender=male
    //api for agent registration

    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/agent/")
     Call<Status> agentRegistration(
            @Field("username") String username ,
            @Field("phone")String phone,
            @Field("latitude")String latitude,
            @Field("longitude")String longitude,
            @Field("password")String password,
            @Field("aadhar_no")String aadhar,
            @Field("bank_name")String bank_name,
            @Field("bank_ac_no")String bank_ac_no,
            @Field("bank_ifsc_code")String bank_ifsc,
            @Field("pancard_no")String pancard,
            @Field("gender")String gender,
            @Field("profile_pic")String profile_pic,
            @Field("address")String address,
            @Field("dob")String dob);


    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/provider/")
    Call<Status> ProviderRegistration(
            @Field("username") String username ,
            @Field("phone")String phone,
            @Field("latitude")String latitude,
            @Field("longitude")String longitude,
            @Field("gender")String gender,
            @Field("profile_pic")String profile_pic,
            @Field("address")String address,
            @Field("service_type")int service_type,
            @Field("service_id")int service_id,
            @Field("referer_id")String referer_id,
            @Field("dob")String dob,
            @Field("website_url")String website_url);


    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/user/")
    Call<Status> agent(@Part("username")RequestBody username,
                       @Part MultipartBody.Part photo);

}
