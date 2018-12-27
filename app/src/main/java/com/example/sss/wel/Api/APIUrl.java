package com.example.sss.wel.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIUrl {
    public static final String BASE_URL = "http://31.22.4.222/1wel/";
    public static final String BASE_URL1 = "http://www.yesinteriors.online/1wel/";
    public static Retrofit retrofit=null;

    public static Retrofit getApiClient(){
        if (retrofit==null){
            retrofit=new Retrofit.Builder().baseUrl(BASE_URL1).
                    addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit;
    }

}
