package com.happydeliv.happydelivcourier.api

import com.happydeliv.happydelivcourier.api.response.BaseApiResponse
import com.happydeliv.happydelivcourier.vo.*
import io.reactivex.Flowable
import retrofit2.http.*

/**
 * Created by ibnumuzzakkir on 05/02/18.
 * Android Engineer
 * SCO Project
 */
interface NetworkService{

    @FormUrlEncoded
    @POST("/api-courrier/login")
    fun login(@Field("data") param : String) : Flowable<BaseApiResponse<VerifyOtpVo>>

    @FormUrlEncoded
    @POST("/api-courrier/add_package")
    fun addTrackPage(@Field("data") data : String, @Field("user_info") userinfo: String) : Flowable<BaseApiResponse<String>>

    @FormUrlEncoded
    @POST("/api-courrier/list_package")
    fun getPackageList(@Field("user_info") userinfo: String) : Flowable<BaseApiResponse<List<PackageVo>>>

    @FormUrlEncoded
    @POST("/api-courrier/list_history")
    fun getHistoryList(@Field("user_info") userinfo: String) : Flowable<BaseApiResponse<List<PackageVo>>>

    @FormUrlEncoded
    @POST("/api-courrier/detail_package")
    fun getDetailPackage(@Field("data") data : String, @Field("user_info") userinfo: String) : Flowable<BaseApiResponse<DetailPackageVo>>

    @FormUrlEncoded
    @POST("/api-courrier/process_package")
    fun processPackage(@Field("data") data : String, @Field("user_info") userinfo: String) : Flowable<BaseApiResponse<Any>>

    @FormUrlEncoded
    @POST("/api-courrier/finish_package")
    fun finishPackage(@Field("data") data : String, @Field("user_info") userinfo: String) : Flowable<BaseApiResponse<Any>>

    @FormUrlEncoded
    @POST("/api-courrier/best_route")
    fun getBestRoute(@Field("data") data : String, @Field("user_info") userinfo: String) : Flowable<BaseApiResponse<List<BestRouteVo>>>

    @FormUrlEncoded
    @POST("/api-courrier/user_information")
    fun getCourierInformation(@Field("user_info") userinfo: String) : Flowable<BaseApiResponse<CourierInfoVo>>


    @FormUrlEncoded
    @POST("/api-courrier/set_destination_package")
    fun setDestinationPackage(@Field("data") data : String, @Field("user_info") userinfo: String) : Flowable<BaseApiResponse<Any>>

}