package com.example.harshraj.sampleapplication.WebServicesContractor

import com.example.harshraj.sampleapplication.Models.UserInfo

/**
 * Created by Harshraj on 19-02-2018.
 * This class is Contractor class in which we define View and Presenter for interacting with the Web Services.
 */

class Contractor {
    interface IWebServiceView {
        fun onWebServiceResult(userInfo: UserInfo)
    }

    interface IWebServicePresenter {
        fun doWebServiceCall(userInfoJson: String)
    }
}
