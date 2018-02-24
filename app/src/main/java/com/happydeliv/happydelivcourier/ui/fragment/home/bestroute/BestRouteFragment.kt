package com.happydeliv.happydelivcourier.ui.fragment.home.bestroute

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View.inflate
import android.widget.TextView
import android.widget.Toast
import com.beust.klaxon.JsonArray
import com.beust.klaxon.JsonObject
import com.beust.klaxon.Parser
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.happydeliv.happydelivapp.ui.common.BaseFragment
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.ui.activity.home.HomeActivity
import com.happydeliv.happydelivcourier.utils.ImageUtil
import com.tedpark.tedpermission.rx2.TedRx2Permission
import kotlinx.android.synthetic.main.fragment_best_route.*
import kotlinx.android.synthetic.main.item_marker_best_route.*
import org.jetbrains.anko.custom.async
import org.jetbrains.anko.uiThread
import java.net.URL
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
class BestRouteFragment : BaseFragment(), BestRouteContract.View, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, android.location.LocationListener {

    @Inject
    lateinit var  mBestRoutePresenter : BestRoutePresenter

    var mGoogleApiClient: GoogleApiClient? = null
    var mLocationRequest: LocationRequest? = null
    var mMap: GoogleMap? = null
    var mapFrag: SupportMapFragment? = null
    var latLng : LatLng? = null
    var currLocationMarker : Marker? = null
    private var mBitmapMarker : Bitmap? = null

    private val mLocationManager by lazy {
        this.context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    override fun getLayoutId(): Int {
       return R.layout.fragment_best_route
    }

    override fun onLoadFragment(saveInstance: Bundle?) {
        (activity as HomeActivity).hideBtnAddPackage()
        TedRx2Permission.with(context)
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE)
                .request()
                .subscribe({ tedPermissionResult ->
                    if (tedPermissionResult.isGranted) {
                        //Permission Granted
                        mapFrag = childFragmentManager.findFragmentById(R.id.mapBestRoute) as SupportMapFragment
                        mapFrag!!.getMapAsync(this)
                    } else {
                        //Denied by user
                    }
                }, { throwable -> throwable.message}, { })

        mBestRoutePresenter.getBestRouteData("6.121212","101.23242")
    }

    private fun setupGoogleClient(){
        context?.let {
            mGoogleApiClient = GoogleApiClient.Builder(it)
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
    }


    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap
        mMap?.mapType = GoogleMap.MAP_TYPE_TERRAIN
        setupGoogleClient()
        //TODO Remove this line after proof of concept
    }

    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        val mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient)
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
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
        }else{
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0,
                    0.1f, this)

            Log.d("GPS Enabled", "GPS Enabled");
            val location = mLocationManager
                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location != null) {
                latLng = LatLng(location.latitude, location.longitude)
                var markerOptions = MarkerOptions()
                markerOptions.position(latLng!!)
                markerOptions.title("Current Position")
                //Setup Marker Driver
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_marker_kurir)
                val markerDriver = BitmapDescriptorFactory.fromBitmap(bitmap)
                markerOptions.icon(markerDriver)
                currLocationMarker = mMap?.addMarker(markerOptions)
                //zoom to current position:
                mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))
            }
        }

        mLocationRequest = LocationRequest()
        mLocationRequest?.interval = 60000 //5 seconds
        mLocationRequest?.fastestInterval = 30000 //3 seconds
        mLocationRequest?.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//        mLocationRequest?.smallestDisplacement = 0.1F //1/10 meter
        mGoogleApiClient?.let {
            LocationServices.FusedLocationApi.requestLocationUpdates(it, mLocationRequest, this)
        }

        //Add Marker
        addMarker(-6.135200,106.813301, R.drawable.ic_marker_kurir, "Lokasi Anda","", true)
        addMarker(-6.343782,106.737461, R.drawable.ic_marker_kurir_tujuan, "Tujuan","1", false)
        addMarker(-6.312822,106.746341, R.drawable.ic_marker_kurir_tujuan, "Tujuan","2", false)
        //Direction 1 & 2
        drawDirection(-6.135200,106.813301,-6.343782,106.737461, Color.BLUE)
        drawDirection(-6.343782,106.737461,-6.312822,106.746341, Color.GRAY)

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        showError(connectionResult.errorMessage.toString())
    }

    override fun onPause() {
        mGoogleApiClient?.disconnect()
        super.onPause()
    }

    override fun onResume() {
        mGoogleApiClient?.connect()
        super.onResume()
    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            latLng = LatLng(it.latitude, it.longitude)
            var markerOptions = MarkerOptions()
            markerOptions.position(latLng!!)
            markerOptions.title("Current Position")
            //Setup Marker Driver
            val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_marker_kurir)
            val markerDriver = BitmapDescriptorFactory.fromBitmap(bitmap)
            markerOptions.icon(markerDriver)
            currLocationMarker = mMap?.addMarker(markerOptions)

            //zoom to current position:
            mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15F))

        }
    }

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun addMarker(lati :Double, longi :Double, marker : Int,titleMarker : String, sequence : String, isInitializeState : Boolean){
        latLng = LatLng(lati, longi)
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng!!)
        markerOptions.title(titleMarker)
        //New marker best route concept

        if(isInitializeState){
            //Create Marker for first state
            mBitmapMarker = BitmapFactory.decodeResource(this.resources,marker)

        }else{
            //Create best route marker
            val view = (activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                    .inflate(R.layout.item_marker_best_route, null)
            val tvBestRouteSequnce = view.findViewById<TextView>(R.id.tv_marker_best_route_sequence)
            tvBestRouteSequnce.text = sequence
            view.isDrawingCacheEnabled = true
            view.buildDrawingCache()
            mBitmapMarker= ImageUtil.createDrawableFromView(context,view)
        }

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(mBitmapMarker))
        mMap!!.addMarker(markerOptions)
    }

    override fun drawDirection(currentLat: Double?, currentLong: Double?, destinationLat: Double?, destinationLong: Double?, color :Int){
        // declare bounds object to fit whole route in screen
        val LatLongB = LatLngBounds.Builder()

        // Add markers
        val driver = LatLng(currentLat!!, currentLong!!)
        val destination = LatLng(destinationLat!!,destinationLong!!)

        // Declare polyline object and set up color and width
        val options = PolylineOptions()
        options.color(color)
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

//                mDetailPackagePresenter.mDuration = duration.toString()
//                mDetailPackagePresenter.mDistance = distance.toString()
//
//                setContentDurationAndDistance(duration.toString(), distance.toString())
            }
        }
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