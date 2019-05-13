package com.shamgar.sss.wel.Api;


import com.shamgar.sss.wel.Models.AgentData;
import com.shamgar.sss.wel.Models.ProvidersDataModel;
import com.shamgar.sss.wel.Models.SearchItems;
import com.shamgar.sss.wel.Models.Services;
import com.shamgar.sss.wel.Models.Status;

import java.util.List;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiService {

    //api for getting all services
    @Headers("X-API-KEY:" + "SHANKAR@111")
    @GET("users_list/users/services/")
    //Call<Status> getServices(@Query("service_key") String service);
    Call<List<Services>> getServices(@Query("service_key") String service);


    // http://www.yesinteriors.online/1wel/users_list/users/services?service_key=cfsdsd
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
            @Field("referer_id")String referer_id);


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
            @Field("referer_id")String referer_id,
            @Field("service_discount")String service_discount,
            @Field("phone_status")String phone_status,
            @Field("website_url")String website_url,
            @Field("service")String service,
            @Field("land_mark")String land_mark,
            @Field("service_desc")String service_des);


    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/user/")
    Call<Status> agent(@Part("username")RequestBody username,
                       @Part MultipartBody.Part photo);


   //http://www.yesinteriors.online/1wel/users_list/users/usersearch

    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/usersearch/")
    Call<List<SearchItems>> search(
            @Field("latitude")String latitude,
            @Field("longitude")String longitude,
            @Field("service_key")String service);


    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/login/")
    Call<AgentData> agentLogin(
            @Field("username")String username,
            @Field("password")String password);

    @Headers("X-API-KEY:" + "SHANKAR@111")
    @GET("users_list/users/logout/")
    Call<Status> agentLogout();

    @Headers("X-API-KEY:" + "SHANKAR@111")
    @GET("users_list/users/providers/")
    Call<ProvidersDataModel> providersDataModel(
            @Query("token") String token);

    @Headers("X-API-KEY:" + "SHANKAR@111")
    @GET("users_list/users/chekprovider/")
    Call<Status> checkProvider(
            @Query("user_id") String user_id,
            @Query("phone") String phone);

    @Headers("X-API-KEY:" + "SHANKAR@111")
    @GET("users_list/users/provider_delete/")
    Call<Status> deleteprovider(
            @Query("user_id") String user_id,
            @Query("phone") String phone);


    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/customer/")
    Call<Status> customerRegistration(
            @Field("username") String user,
            @Field("phone") String phone,
            @Field("referer_id")String referer_id);

    @Headers("X-API-KEY:" + "SHANKAR@111")
    @FormUrlEncoded
    @POST("users_list/users/verify_customer/")
    Call<Status> customerVerification(
            @Field("customer_id") String customerId,
            @Field("provider_id") String verificationID,
            @Field("purchased_amount") String purchasedAmount);

   // http://www.yesinteriors.online/1wel/users_list/users/verify_customer

   // http://www.yesinteriors.online/1wel/users_list/users/customer


    //http://www.yesinteriors.online/1wel/users_list/users/provider_delete?user_id=106&phone=9642542514

//    {
//        "status": true,
//            "message": "Delete Successfully"
//    }

    //http://www.yesinteriors.online/1wel/users_list/users/chekprovider?user_id=106&phone=9642542514
//
//        {
//                "username": "eswar",
//                "phone": "1234567890",
//                "profile_pic": "http://www.yesinteriors.online/1wel/uploads/users/e7LwvMfK.jpg",
//                "website_url": "http://www.yesinteriors.online/1wel/users_list/users/provider",
//                "address": "Mvpcolony",
//                "service_desc": "fgsdfdssdfgdsfd",
//                "service": "plumber"
//        }

}


