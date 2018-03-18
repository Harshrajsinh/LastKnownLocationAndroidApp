package com.example.harshraj.sampleapplication.WebServicePresenter

import com.example.harshraj.sampleapplication.WebServicesContractor.Contractor

/**
 * Created by Harshraj on 19-02-2018.
 * Presenter Class for calling the webservice and passing the Json data to Web Service
 * After getting the response, we pass the response to the View interface for updating the UI.
 */

class WebServicePresenter(internal var iWebServiceView: Contractor.IWebServiceView) : Contractor.IWebServicePresenter {

    override fun doWebServiceCall(userInfoJson: String) {
        //here is the place we call the WebService
        //then we pass the response the IWebServiceView.OnwebServiceResult
    }
}
