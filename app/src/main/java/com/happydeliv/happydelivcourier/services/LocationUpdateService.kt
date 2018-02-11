package com.happydeliv.happydelivcourier.services

import com.firebase.jobdispatcher.JobParameters
import com.happydeliv.happydelivcourier.HappyDelivCourier
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.*
import com.happydeliv.happydelivcourier.utils.FirebaseDB
import javax.inject.Inject
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest




/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
class LocationUpdateService :  BaseJobService() , LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    var mGoogleApiClient: GoogleApiClient? = null
    var mLastLocation: Location? = null
    var mLocationRequest: LocationRequest? = null

    @Inject
    lateinit var mFirebaseDB : FirebaseDB

    override fun setupComponent() {
        HappyDelivCourier.appComponent
                .inject(this)
    }

    override fun onStopJob(job: JobParameters?): Boolean {
        mGoogleApiClient?.disconnect()
        return true
    }

    @SuppressLint("MissingPermission")
    override fun onStartJob(job: JobParameters?): Boolean {
        Log.d(javaClass.name, "Job is running")

        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = 10000    // 10 seconds, in milliseconds
        mLocationRequest?.fastestInterval = 1000   // 1 second, in milliseconds
        mLocationRequest?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        } else {
            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show()
        }

        return true
    }

    override fun onLocationChanged(location: Location?) {
        mLastLocation = location
        Log.d(javaClass.name + "LocationUpdate", mLastLocation?.latitude.toString() + "," + mLastLocation?.longitude.toString())
    }

    override fun onConnected(bundle: Bundle?) {
        getLocation()
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.d(javaClass.name, "onConnectionSuspended")
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d(javaClass.name, "onConnectionFailed")
    }

    @SuppressLint("MissingPermission")
    private fun getLocation(){
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)

        if (mLastLocation == null) {
            Log.i("Current Location", "No data for location found");

            if (!mGoogleApiClient?.isConnected!!) {
                mGoogleApiClient?.connect()
            }
        }else{
            Log.d(javaClass.name + "LocationUpdate -1", mLastLocation?.latitude.toString() + "," + mLastLocation?.longitude.toString())
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this)
    }


}