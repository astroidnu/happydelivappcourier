package com.happydeliv.happydelivcourier.vo

import com.google.gson.annotations.SerializedName

/**
 * Created by ibnumuzzakkir on 24/02/18.
 * Android Engineer
 * SCO Project
 */
data class CourierInfoVo(
        @SerializedName("id") val id : String,
        @SerializedName("name") val name : String,
        @SerializedName("email") val email : String,
        @SerializedName("phone") val phone : String,
        @SerializedName("company") val company : String
)