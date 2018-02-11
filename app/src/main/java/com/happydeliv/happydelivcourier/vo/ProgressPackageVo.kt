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
        @SerializedName("driver_current_long") val driverCurrentLong : String
)