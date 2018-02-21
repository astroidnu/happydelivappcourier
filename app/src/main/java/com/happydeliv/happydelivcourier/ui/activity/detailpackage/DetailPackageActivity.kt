package com.happydeliv.happydelivcourier.ui.activity.detailpackage

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.bumptech.glide.Glide
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.utils.FirebaseDB
import com.scoproject.newsapp.ui.common.BaseActivity
import com.tedpark.tedpermission.rx2.TedRx2Permission
import kotlinx.android.synthetic.main.activity_detail_package.*
import kotlinx.android.synthetic.main.activity_toolbar.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import java.net.URL
import javax.inject.Inject


/**
 * Created by ibnumuzzakkir on 07/02/18.
 * Android Engineer
 * SCO Project
 */
open class DetailPackageActivity : BaseActivity(), DetailPackageContract.View, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener {

    @Inject
    lateinit var mDetailPackagePresenter : DetailPackagePresenter

    @Inject
    lateinit var mFirebaseDB : FirebaseDB

    private val REQUEST_PLACE_PICKER = 100

    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    var mMap: GoogleMap? = null
    var mapFrag: SupportMapFragment? = null
    var latLng : LatLng? = null
    var currLocationMarker : Marker? = null

    var mTrackId :String? = null

    var mGeoDataClient : GeoDataClient? = null
    var mCurrentLat  : String? = null
    var mCurrentLong : String? = null


    override fun onActivityReady(savedInstanceState: Bundle?) {
        mDetailPackagePresenter.attachView(this)
        setupUIListener()
        val bundle = intent.extras
        if (bundle != null) {
            mTrackId = bundle.getString("data")
                mTrackId?.let {
                mDetailPackagePresenter.getPackageDetail(it)
            }
        }
        TedRx2Permission.with(this)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE)
                .request()
                .subscribe({ tedPermissionResult ->
                    if (tedPermissionResult.isGranted) {
                      //Permission Granted
                        mapFrag = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                        mapFrag!!.getMapAsync(this)

                        // Construct a GeoDataClient.
                        mGeoDataClient = Places.getGeoDataClient(this, null)

                    } else {
                        //Denied by user
                    }
                }, { throwable -> throwable.message}, { })
        mDetailPackagePresenter.getTrackingPackageFirebase(mTrackId!!)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        mActivityNavigation.navigateToHomePage()
    }

    private fun setupGoogleClient(){
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build()

        if (mGoogleApiClient != null) {
            mGoogleApiClient?.connect()
        } else {
//            Toast.makeText(this, "Not Connected!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        mGoogleApiClient?.disconnect()
        super.onDestroy()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_detail_package
    }

    override fun setupUIListener() {
        imgbtn_back.visibility = View.VISIBLE
        imgbtn_back.setOnClickListener {
            onBackPressed()
        }

        btn_process_package.setOnClickListener {
            mDetailPackagePresenter.processPackage(mTrackId!!)
        }

        btn_finish_package.setOnClickListener {
            mDetailPackagePresenter.finishPackage(mTrackId!!)
        }

        sv_search_alamat.setOnClickListener {
              try {
                val intentBuilder = PlacePicker.IntentBuilder()
                val intent = intentBuilder.build(this)
                // Start the intent by requesting a result,
                // identified by a request code.
                startActivityForResult(intent, REQUEST_PLACE_PICKER)

            } catch (e : GooglePlayServicesRepairableException) {
                  Log.e(javaClass.name, e.message.toString())
            } catch (e : GooglePlayServicesNotAvailableException) {
                  Log.e(javaClass.name, e.message.toString())
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_PLACE_PICKER && resultCode == Activity.RESULT_OK){
            // The user has selected a place. Extract the name and address.
            val place = PlacePicker.getPlace(this, data)
            val name = place.name
            val latLng = place.latLng
            val address = place.address

            var markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            markerOptions.title("Tujuan")

            //Setup Marker Driver
            val bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.ic_marker_kurir_tujuan)
            val markerDriver = BitmapDescriptorFactory.fromBitmap(bitmap)
            markerOptions.icon(markerDriver)
            currLocationMarker = mMap?.addMarker(markerOptions)

            mMap?.clear()

            //Setup lat long for destination package
            mDetailPackagePresenter.mDestinationLat = place.latLng.latitude.toString()
            mDetailPackagePresenter.mDestinationLong = place.latLng.longitude.toString()

            mDetailPackagePresenter.draw()


        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun addMarker(lati :Double, longi :Double, marker : Int,titleMarker : String){
        latLng = LatLng(lati, longi)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng!!)
        markerOptions.title(titleMarker)
        //Setup Marker Driver
        val bitmap = BitmapFactory.decodeResource(this.resources,marker)
        val markerDriver = BitmapDescriptorFactory.fromBitmap(bitmap)
        markerOptions.icon(markerDriver)

        mMap!!.addMarker(markerOptions)
    }

    @SuppressLint("MissingPermission")
    override fun setupContent(recipientAddress: String,
                              trackingId: String,
                              imageUrl: String,
                              recipientName: String,
                              phoneDriver: String) {
        tv_detail_recipient_address.text = recipientAddress
        tv_detail_recipient_name.text = recipientName
        tv_detail_track_id.text = "Track id : " + trackingId
        Glide.with(this)
                .load(imageUrl)
                .into(iv_detail_profile_photo)

        //Call Action
        btn_phone_customer.setOnClickListener {
            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneDriver))
            startActivity(callIntent)
        }

        //SMS Action
        btn_message_customer.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneDriver, null)))
        }

    }

    override fun showLoading() {
        pb_detail_package.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb_detail_package.visibility = View.GONE
    }

    override fun showError(content: String) {
       Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToDashboard() {
        onBackPressed()
    }

    override fun hideProcessPackageLayout() {
        ll_in_progress_package_driver.visibility = View.VISIBLE
        ll_start_package_driver.visibility = View.GONE
        sv_search_alamat.visibility = View.GONE
        tv_distance?.visibility = View.VISIBLE
        tv_duration?.visibility = View.VISIBLE
    }

    override fun showProcessPackageLayout() {
        ll_in_progress_package_driver.visibility = View.GONE
        ll_start_package_driver.visibility = View.VISIBLE
        sv_search_alamat.visibility = View.VISIBLE
        tv_distance?.visibility = View.GONE
        tv_duration?.visibility = View.GONE
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        setupGoogleClient()
    }

    override fun drawDirection(currentLat: Double?, currentLong: Double?, destinationLat: Double?, destinationLong: Double?){
        // declare bounds object to fit whole route in screen
        val LatLongB = LatLngBounds.Builder()

        // Add markers
        val driver = LatLng(currentLat!!, currentLong!!)
        val destination = LatLng(destinationLat!!,destinationLong!!)

        // Declare polyline object and set up color and width
        val options = PolylineOptions()
        options.color(Color.BLUE)
        options.width(5f)

        // build URL to call API
        val url = getURL(driver, destination)

        async {
            // Connect to URL, download content and convert into string asynchronously
            val result = URL(url).readText()
            uiThread {
                // When API call is done, create parser and convert into JsonObjec
                val parser = Parser()
                val stringBuilder = StringBuilder(result)
                val json: JsonObject = parser.parse(stringBuilder) as JsonObject
                // get to the correct element in JsonObject
                val routes = json.array<JsonObject>("routes")
                val points = routes!!["legs"]["steps"][0] as JsonArray<JsonObject>
                // For every element in the JsonArray, decode the polyline string and pass all points to a List
                val polypts = points.flatMap { decodePoly(it.obj("polyline")?.string("points")!!)  }
                // Add  points to polyline and bounds
                options.add(driver)
                LatLongB.include(driver)
                for (point in polypts)  {
                    options.add(point)
                    LatLongB.include(point)
                }
                options.add(destination)
                LatLongB.include(destination)
                // build bounds
                val bounds = LatLongB.build()
                // add polyline to the map
                mMap?.addPolyline(options)
                // show map with route centered
                mMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))

                val legs  = routes!!["legs"][0] as JsonArray<JsonObject>
                val distanceArr = legs[0]["distance"] as JsonObject
                val distance = distanceArr["text"]
                val durationArr = legs[0]["duration"] as JsonObject
                val duration = durationArr["text"]

                setContentDurationAndDistance(duration.toString(), distance.toString())
            }
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
//        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    override fun setContentDurationAndDistance(duration: String, distance: String) {
        tv_distance?.text = "Jarak : " + distance
        tv_duration?.text = "Waktu tempuh : " + duration
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
//        Toast.makeText(this, "onConnected", Toast.LENGTH_SHORT).show()
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient)
        if (mLastLocation != null) {
//            place marker at current position
            mMap?.clear()
            latLng = LatLng(mLastLocation.latitude, mLastLocation.longitude)
            val markerOptions = MarkerOptions()
            markerOptions.position(latLng!!)
            markerOptions.title("Current Position")
            //Setup Marker Driver
            val bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.ic_marker_kurir)
            val markerDriver = BitmapDescriptorFactory.fromBitmap(bitmap)
            markerOptions.icon(markerDriver)

            currLocationMarker = mMap!!.addMarker(markerOptions)
            mDetailPackagePresenter.mDriverLat = mLastLocation.latitude.toString()
            mDetailPackagePresenter.mDriverLong = mLastLocation.longitude.toString()

            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))

        }

        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = 60000 //5 seconds
        mLocationRequest?.fastestInterval = 10000 //3 seconds
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
        mLocationRequest?.smallestDisplacement = 10F //1/10 meter
        mGoogleApiClient?.let {
            LocationServices.FusedLocationApi.requestLocationUpdates(it, mLocationRequest, this)
        }

    }

    override fun onConnectionSuspended(p0: Int) {
//        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    override fun onLocationChanged(location: Location?) {
        //place marker at current position
        mMap?.clear()
        if (currLocationMarker != null) {
            currLocationMarker!!.remove()
        }

        toast(location?.latitude.toString() + "," + location?.longitude.toString())

        latLng = LatLng(location!!.latitude, location.longitude)
        mDetailPackagePresenter.mDriverLat = location.latitude.toString()
        mDetailPackagePresenter.mDriverLong = location.longitude.toString()

        var markerOptions = MarkerOptions()
        markerOptions.position(latLng!!)
        markerOptions.title("Current Position")
        //Setup Marker Driver
        val bitmap = BitmapFactory.decodeResource(this.resources, R.drawable.ic_marker_kurir)
        val markerDriver = BitmapDescriptorFactory.fromBitmap(bitmap)
        markerOptions.icon(markerDriver)
        currLocationMarker = mMap?.addMarker(markerOptions)

        //zoom to current position:
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))

        mTrackId?.let {
            mDetailPackagePresenter.sendingCourierLocation(it)
        }

        mDetailPackagePresenter.draw()


        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);

    }

    override fun onStop() {
        mFirebaseDB.onDisconnect()
        super.onStop()
    }


    private fun getURL(from : LatLng, to : LatLng) : String {
        val origin = "origin=" + from.latitude + "," + from.longitude
        val dest = "destination=" + to.latitude + "," + to.longitude
        val sensor = "sensor=false"
        val params = "$origin&$dest&$sensor"
        return "https://maps.googleapis.com/maps/api/directions/json?$params"
    }

    /**
     * Method to decode polyline points
     * Courtesy : https://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     */
    private fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

}