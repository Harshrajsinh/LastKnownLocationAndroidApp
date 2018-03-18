package com.example.harshraj.sampleapplication

import android.app.IntentService
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.ResultReceiver
import android.util.Log

import java.io.IOException
import java.util.Locale

import android.content.ContentValues.TAG

/**
 * Created by Harshraj Thakor, 02/19/2018
 */
class GettingAddressFromLATLONG : IntentService("GettingAddressFromLATLONG") {

    //for passing the result back to the requester
    var resultReceiver: ResultReceiver? = null

    internal var geoCoder = Geocoder(this, Locale.getDefault())

    override fun onHandleIntent(intent: Intent?) {

        //if any error message
        var errorMessage = ""

        //getting the location(LATLONG) from the intent
        val location = intent!!.getParcelableExtra<Location>(Constants.LOCATION_DATA)

        //getting the class instance for passing back the results
        resultReceiver = intent.getParcelableExtra(Constants.RECEIVER)

        //for the results
        val bundle = Bundle()

        //if more than 1 address
        //but we will make max count to 1
        var addresses: List<Address>? = null

        try {
            addresses = geoCoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    // In this sample, get just a single address.
                    1)
        } catch (ioException: IOException) {

            // Catch network or other I/O problems.
            Log.e(TAG, Constants.SERVICE_NOT_AVAILABLE, ioException)

        } catch (illegalArgumentException: IllegalArgumentException) {

            // Catch invalid latitude or longitude values.
            Log.e(TAG, Constants.WRONG_COORDINATES + ". " + "Latitude = " + location.latitude + ", Longitude = " + location.longitude, illegalArgumentException)
        }

        // Handle case where no address was found.
        if (addresses == null || addresses.size == 0) {
            if (errorMessage.isEmpty()) {
                Log.e(TAG, Constants.NO_ADDRESS_NOT_FOUND)
            }
            bundle.putString(Constants.NO_ADDRESS_NOT_FOUND,Constants.NO_ADDRESS_NOT_FOUND)
            resultReceiver?.send(Constants.FAILURE,bundle)
        } else {
            bundle.putString(Constants.COUNTRY_NAME, addresses[0].countryName)
            bundle.putString(Constants.CITY_NAME, addresses[0].locality)
            bundle.putString(Constants.POSTAL_CODE, addresses[0].postalCode)
            bundle.putString(Constants.STATE_NAME, addresses[0].adminArea)
            bundle.putString(Constants.STREET_NAME, addresses[0].thoroughfare)
            resultReceiver?.send(Constants.SUCCESS, bundle)
        }

    }
}
