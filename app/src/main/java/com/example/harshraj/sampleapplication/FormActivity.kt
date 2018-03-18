package com.example.harshraj.sampleapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.ResultReceiver
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.example.harshraj.sampleapplication.Models.UserInfo
import com.example.harshraj.sampleapplication.WebServicePresenter.WebServicePresenter
import com.example.harshraj.sampleapplication.WebServicesContractor.Contractor
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.google.gson.Gson

class FormActivity : AppCompatActivity(), Contractor.IWebServiceView {

    //initialization of the android widgets
    internal var submitButton: Button? = null
    internal var countryName: EditText? = null
    internal var stateName: EditText? = null
    internal var cityName: EditText? = null
    internal var zipCode: EditText? = null
    internal var firstName: EditText? = null
    internal var lastName: EditText? = null
    internal var streetName: EditText? = null

    //creating instance of the fusedLocationProvider and Location
    internal var fusedLocationProviderClient: FusedLocationProviderClient? = null
    internal var mLocation: Location? = null

    //for converting User object to Json
    internal var gson = Gson()

    internal var user = UserInfo()

    //presenter for the Web Service
    internal var webServicePresenter: WebServicePresenter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        //checking for the permission for using the location services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission()
        }

        val fab = findViewById<View>(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Sphere is my Future work place", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        //getting client
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //finding the id
        countryName = findViewById(R.id.CountryName)
        cityName = findViewById(R.id.CityName)
        stateName = findViewById(R.id.StateName)
        zipCode = findViewById(R.id.PostalCode)
        firstName = findViewById(R.id.FirstName)
        lastName = findViewById(R.id.LastName)
        streetName = findViewById(R.id.StreetName)
        submitButton = findViewById(R.id.SubmitButton)

        //passing the application context in the presenter
        // which will be useful in getting back results from the view.
        webServicePresenter = WebServicePresenter(this)

        if (ActivityCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }

        fusedLocationProviderClient?.lastLocation?.addOnSuccessListener(OnSuccessListener { location ->
            mLocation = location
            if (mLocation == null) {
                Toast.makeText(applicationContext,Constants.LOCATION_NOT_FOUND, Toast.LENGTH_LONG)
                return@OnSuccessListener
            }

            if (!Geocoder.isPresent()) {
                Toast.makeText(applicationContext, Constants.GEOCODER_IS_NOT_PRESENT, Toast.LENGTH_LONG)
                return@OnSuccessListener
            }

            //calling the intentService for converting
            // Latitude and Longitude to actual address
            startIntentService()
        })

        submitButton?.setOnClickListener {
            user.firstName = firstName?.text.toString()
            user.lastName = lastName?.text.toString()
            user.streetName = streetName?.text.toString()
            user.cityName = cityName?.text.toString()
            user.stateName = stateName?.text.toString()
            user.countryName = countryName?.text.toString()
            user.postalCode = zipCode?.text.toString()

            //making the webservice call
            webServicePresenter?.doWebServiceCall(gson.toJson(user))

            //small code snippet that shows user data model was converted into JSON string
            Toast.makeText(applicationContext,gson.toJson(user),Toast.LENGTH_LONG).show()
        }
    }

    //starting the intent service because GeoCoder is synchronous
    // work and it should not called from main worker (UI) thread.
    fun startIntentService() {
        val intent = Intent(this, GettingAddressFromLATLONG::class.java)
        intent.putExtra(Constants.LOCATION_DATA, mLocation)
        val addressResult = AddressResult(Handler())
        intent.putExtra(Constants.RECEIVER, addressResult)
        startService(intent)
    }

    override fun onWebServiceResult(userInfo: UserInfo) {
        //here you the Update the UI on getting back the result from the webservice.
    }

    //inner class for handling the results from the intentService
    internal inner class AddressResult(handler: Handler) : ResultReceiver(handler) {
        override fun onReceiveResult(resultCode: Int, resultData: Bundle) {
            countryName?.setText(resultData.getString(Constants.COUNTRY_NAME))
            stateName?.setText(resultData.getString(Constants.STATE_NAME))
            cityName?.setText(resultData.getString(Constants.CITY_NAME))
            zipCode?.setText(resultData.getString(Constants.POSTAL_CODE))
        }
    }

    //method for checking the COARSE_LOCATION and FINE_LOCATION permissions
    //it will ask the user for accepting or declining the permission for using the location services for the application.
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    123)
        }
    }

}

