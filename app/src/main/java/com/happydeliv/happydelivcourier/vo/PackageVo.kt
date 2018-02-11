package com.happydeliv.happydelivcourier.vo

import com.google.gson.annotations.SerializedName

/**
 * Created by ibnumuzzakkir on 09/02/18.
 * Android Engineer
 * SCO Project
 */
data class PackageVo(
        @SerializedName("recipient_name") val recipientName :String,
        @SerializedName("track_id") val trackId :String,
        @SerializedName("resi_number") val resiNumber :String,
        @SerializedName("status") val status :String,
        @SerializedName("recipient_image") val recipientImage :String,
        @SerializedName("delivered_at") val deliveredAt :String
)