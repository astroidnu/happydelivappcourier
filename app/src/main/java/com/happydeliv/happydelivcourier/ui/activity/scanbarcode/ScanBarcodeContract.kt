package com.happydeliv.happydelivcourier.ui.activity.scanbarcode

import com.scoproject.weatherapp.ui.base.BaseView

/**
 * Created by ibnumuzzakkir on 25/02/18.
 * Android Engineer
 * SCO Project
 */
class ScanBarcodeContract{
    interface View : BaseView {
        fun navigateToHome()
        fun showLoading()
        fun hideLoading()
        fun showError(msg :String)
    }

    interface UserActionListener{
        fun addTrackingPackage(trackingId : String)
    }
}