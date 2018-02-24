package com.happydeliv.happydelivcourier.vo

import com.google.gson.annotations.SerializedName

/**
 * Created by ibnumuzzakkir on 23/02/18.
 * Android Engineer
 * SCO Project
 */

data class BestRouteVo(
        @SerializedName("track_id") val trackId  : String,
        @SerializedName("recipient_name") val recipientName  : String,
        @SerializedName("resi_number") val resiNumber  : String,
        @SerializedName("recipient_address") val recipientAddress  : String,
        @SerializedName("lat_address") val latAddress  : String,
        @SerializedName("longi_address") val longinAddress  : String,
        @SerializedName("previous_lat") val previousLati  : String,
        @SerializedName("previous_longi") val previousLongi  : String,
        @SerializedName("sequence") val sequence  : String
)