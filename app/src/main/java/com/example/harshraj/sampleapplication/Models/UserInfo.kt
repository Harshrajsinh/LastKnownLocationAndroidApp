package com.example.harshraj.sampleapplication.Models

import com.google.gson.annotations.SerializedName

/**
 * Created by Harshraj on 19-02-2018.
    Data Model Class for storing anc converting the Form data to Json.
 */

class UserInfo {

    @SerializedName("firstName")
    var firstName: String? = null

    @SerializedName("lastName")
    var lastName: String? = null

    @SerializedName("streetName")
    var streetName: String? = null

    @SerializedName("cityName")
    var cityName: String? = null

    @SerializedName("stateName")
    var stateName: String? = null

    @SerializedName("countryName")
    var countryName: String? = null

    @SerializedName("postalCode")
    var postalCode: String? = null
}
