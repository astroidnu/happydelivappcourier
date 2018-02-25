package com.happydeliv.happydelivcourier.ui.common.navigationcontroller

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import com.happydeliv.happydelivcourier.ui.activity.addtracking.AddTrackingActivity
import com.happydeliv.happydelivcourier.ui.activity.detailpackage.DetailPackageActivity
import com.happydeliv.happydelivcourier.ui.activity.home.HomeActivity
import com.happydeliv.happydelivcourier.ui.activity.login.LoginActivity
import com.happydeliv.happydelivcourier.ui.activity.scanbarcode.ScanBarcodeActivity
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 24/01/18.
 * Android Engineer
 * SCO Project
 */

class ActivityNavigation @Inject constructor(val activity:AppCompatActivity){
    /**
     * Navigate To detail page
     * */

    fun navigateToDetailPage(trackId :String){
        val intentDetailPackage = newIntent(activity, DetailPackageActivity::class.java)
        intentDetailPackage.apply {
            putExtra("data", trackId)
        }
        activity.startActivity(intentDetailPackage)
    }

    /**
     * Intent to login page
     * */

    fun navigateToLoginPage(){
        val intentLogin = newIntent(activity, LoginActivity::class.java)
        activity.startActivity(intentLogin)
        activity.finish()
    }

    /**
     * Navigate To Home Page
     * */

    fun navigateToHomePage(){
        val intentHomePage = newIntent(activity, HomeActivity::class.java)
        activity.startActivity(intentHomePage)
    }

    /**
     * Navigate To Home Page
     * */

    fun navigateToQRScan(){
        val scanbarcode = newIntent(activity, ScanBarcodeActivity::class.java)
        activity.startActivity(scanbarcode)
    }


    /**
     * Navigate To Add Tracking Page
     * */

    fun navigateToAddTrackingPage(){
        val intentAddTracking = newIntent(activity, AddTrackingActivity::class.java)
        activity.startActivity(intentAddTracking)
    }

    /**
     * Intent Common Function
     * Handling new intent
     * */
    fun <T> newIntent(context: Context, cls: Class<T>): Intent {
        return Intent(context, cls)
    }

    fun newIntentUri(label: String, uri: Uri): Intent {
        return Intent(label, uri)
    }
}