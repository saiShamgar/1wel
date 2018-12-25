package com.example.sss.wel.Api;

import com.example.sss.wel.Models.AgentRegistration;
import com.example.sss.wel.Models.Services;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    //api for getting all services
    @Headers("X-API-KEY:" + "SHANKAR@111")
    @GET("users_list/users/services/")
    Call<List<Services>> getServices(
    @Query("service_type") String service);

    //api for agent registration
    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/user/")
    Call<AgentRegistration> agentRegistration(
            @Field("username")String username ,
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
            @Field("user_type")String type
            );
}
