package com.happydeliv.happydelivcourier.vo

import com.google.gson.annotations.SerializedName

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
data class DetailPackageVo(
        @SerializedName("recipient_name") val recipientName :String,
        @SerializedName("track_id") val trackId :String,
        @SerializedName("resi_number") val resiNumber :String,
        @SerializedName("recipient_phone") val recipientPhone :String,
        @SerializedName("recipient_address") val recipientAddress :String,
        @SerializedName("recipient_photo") val recipientPhoto :String
)
