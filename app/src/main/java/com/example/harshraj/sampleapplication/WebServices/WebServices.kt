package com.example.harshraj.sampleapplication.WebServices

import com.example.harshraj.sampleapplication.Models.UserInfo

import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by Harshraj on 19-02-2018.
 * Declaration of the Web Service
 * Using Retrofit third party library we can define what type of the Web Service it it, i.e. GET, POST, PUT, PATCH, QUERY
 * We also define how the response will look like in call class.
 * In this instance, we are using same model class for sending the Json data and getting back the response.
 */

interface WebServices {

    //Web Service Call
    @GET("WebServiceURL")
    fun webServiceRespose(): Call<UserInfo>
}
