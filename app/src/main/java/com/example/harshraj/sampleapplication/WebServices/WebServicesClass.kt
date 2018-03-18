package com.example.harshraj.sampleapplication.WebServices

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by Harshraj on 19-02-2018.
 * This WebService class for making the connection the URL.
 * Using the OKHTTP and HTTPLOGGINGINTERCEPTOR we are making the connection to the Base URL.
 * And then returning the webService.
 */

object WebServicesClass {

    internal var webServices: WebServices? = null
    val BASE_URL = ""

    fun webServicesConnection(): WebServices? {
        if (webServices == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient()
            okHttpClient.interceptors().add(Interceptor { chain -> chain.proceed(chain.request()) })
            //            Retrofit retrofit = new Retrofit().newBuilder().build();
            //            webServices =  retrofit.create(WebServices.class);
        }
        return webServices
    }


}
