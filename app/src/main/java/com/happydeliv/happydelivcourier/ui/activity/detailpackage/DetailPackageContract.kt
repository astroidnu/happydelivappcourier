package com.happydeliv.happydelivcourier.ui.activity.detailpackage

import com.scoproject.weatherapp.ui.base.BaseView

/**
 * Created by ibnumuzzakkir on 07/02/18.
 * Android Engineer
 * SCO Project
 */
class DetailPackageContract{
    interface View : BaseView {
        fun setupUIListener()
        fun setupContent(recipientAddress: String,
                         trackingId: String,
                         imageUrl: String,
                         recipientName: String,
                         phoneDriver: String)
        fun showLoading()
        fun hideLoading()
        fun showError(content :String)
        fun navigateToDashboard()
        fun hideProcessPackageLayout()
        fun showProcessPackageLayout()
        fun drawDirection(currentLat: Double?, currentLong: Double?, destinationLat: Double?, destinationLong: Double?)
        fun addMarker(lati :Double, longi :Double, marker :Int, titleMarker : String)
    }
    interface UserActionListener {
        fun getPackageDetail(packageId : String)
        fun processPackage(trackingID: String)
        fun finishPackage(trackingID :String)
        fun checkingPackageStatus(status :String)
        fun getTrackingPackageFirebase(trackingID : String)
        fun deleteTrackingId(trackingID: String)
    }
}