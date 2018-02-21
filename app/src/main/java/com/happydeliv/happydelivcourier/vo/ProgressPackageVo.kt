package com.happydeliv.happydelivcourier.vo

import com.google.gson.annotations.SerializedName

/**
 * Created by ibnumuzzakkir on 11/02/18.
 * Android Engineer
 * SCO Project
 */

data class ProgressPackageVo(
        @SerializedName("track_id") val trackId : String,
        @SerializedName("driver_current_lat") val driverCurrentLat : String,
        @SerializedName("driver_current_long") val driverCurrentLong : String,
        @SerializedName("destination_current_lat") val destinationCurrentLat : String,
        @SerializedName("destination_current_long") val destinationCurrentLong : String,
        @SerializedName("duration") val duration : String,
        @SerializedName("distance") val distance : String
)