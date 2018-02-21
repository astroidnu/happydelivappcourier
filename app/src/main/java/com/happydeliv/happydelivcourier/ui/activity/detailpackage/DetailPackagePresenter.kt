package com.happydeliv.happydelivcourier.ui.activity.detailpackage

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.gson.Gson
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.utils.FirebaseDB
import com.happydeliv.happydelivcourier.utils.SchedulerProvider
import com.happydeliv.happydelivcourier.vo.PackageStatus
import com.happydeliv.happydelivcourier.vo.ProgressPackageVo
import com.scoproject.newsapp.ui.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 07/02/18.
 * Android Engineer
 * SCO Project
 */
class DetailPackagePresenter @Inject constructor(private val networkService: NetworkService,
                                                 disposable : CompositeDisposable,
                                                 scheduler : SchedulerProvider,
                                                 val loginSession: LoginSession,
                                                 val gson: Gson,
                                                 val firebaseDB: FirebaseDB) : BasePresenter<DetailPackageContract.View>(disposable,scheduler),DetailPackageContract.UserActionListener{
    var mDriverLat : String? = null
    var mDriverLong : String? = null
    var mDestinationLat : String? = null
    var mDestinationLong : String? = null
    var mDuration : String? = null
    var mDistance : String? = null

    override fun getPackageDetail(trackId : String) {
        view?.showLoading()
        var userInfo = HashMap<String, Any>()
        userInfo.put("phone", loginSession.getPhoneNumber())
        userInfo.put("token", loginSession.getLoginToken())

        var data = HashMap<String, String>()
        data.put("track_id", trackId)
        if(disposable.size() > 0){
            disposable.clear()
        }
        disposable.add(
                networkService.getDetailPackage(gson.toJson(data),gson.toJson(userInfo))
                        .subscribeOn(scheduler.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            result ->
                            view?.hideLoading()
                            val detailPackage = result.data
                            if(result.resultCode == 1){
                                checkingPackageStatus(detailPackage.status)
                                view?.setupContent(detailPackage.recipientAddress,detailPackage.trackId,
                                        detailPackage.recipientPhoto,detailPackage.recipientName,detailPackage.recipientPhone)
                            }else{
                                view?.showError(result.resultMessage)
                            }

                        },{
                            err -> Log.e(javaClass.name, err.message.toString())
                            view?.hideLoading()
                            view?.showError("please try again")
                        })
        )
    }

    override fun processPackage(trackingID: String) {
        view?.showLoading()
        var userInfo = HashMap<String, Any>()
        userInfo.put("phone", loginSession.getPhoneNumber())
        userInfo.put("token", loginSession.getLoginToken())

        var data = HashMap<String, String>()
        data.put("track_id", trackingID)
        if(disposable.size() > 0){
            disposable.clear()
        }
        disposable.add(
                networkService.processPackage(gson.toJson(data),gson.toJson(userInfo))
                        .subscribeOn(scheduler.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            result ->
                            view?.hideLoading()
                            if(result.resultCode == 1){
                                getPackageDetail(trackingID)
                                sendingCourierLocation(trackingID)
                                view?.hideProcessPackageLayout()
                            }else{
                                view?.showError(result.resultMessage)
                            }

                        },{
                            err -> Log.e(javaClass.name, err.message.toString())
                            view?.hideLoading()
                            view?.showError("please try again")
                        })
        )
    }

    override fun finishPackage(trackingID: String) {
        view?.showLoading()
        var userInfo = HashMap<String, Any>()
        userInfo.put("phone", loginSession.getPhoneNumber())
        userInfo.put("token", loginSession.getLoginToken())

        var data = HashMap<String, String>()
        data.put("track_id", trackingID)
        if(disposable.size() > 0){
            disposable.clear()
        }
        disposable.add(
                networkService.finishPackage(gson.toJson(data),gson.toJson(userInfo))
                        .subscribeOn(scheduler.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            result ->
                            view?.hideLoading()
                            if(result.resultCode == 1){
                                deleteTrackingId(trackingID)
                                view?.navigateToDashboard()
                            }else{
                                view?.showError(result.resultMessage)
                            }

                        },{
                            err -> Log.e(javaClass.name, err.message.toString())
                            view?.hideLoading()
                            view?.showError("please try again")
                        })
        )
    }

    fun draw(){
        if((mDriverLat != null && mDriverLong != null)
                && (mDestinationLat != null && mDestinationLong != null)
                && (!mDestinationLat.equals("null") && !mDestinationLong.equals("null"))){
            view?.addMarker(mDriverLat?.toDouble()!!,mDriverLong?.toDouble()!!, R.drawable.ic_marker_kurir, "Lokasi Anda")
            view?.addMarker(mDestinationLat?.toDouble()!!,mDestinationLong?.toDouble()!!, R.drawable.ic_marker_kurir_tujuan, "Tujuan")
            view?.drawDirection(mDriverLat?.toDouble(),mDriverLong?.toDouble(),mDestinationLat?.toDouble(),mDestinationLong?.toDouble())
            view?.setContentDurationAndDistance(mDuration.toString(), mDistance.toString())
        }
    }

    fun sendingCourierLocation(trackingID :String){
        if((mDriverLat != null && mDriverLong != null)
                && (mDestinationLat != null && mDestinationLong!= null)
                && (mDuration != null && mDistance != null)){
            val progressPackageVo = ProgressPackageVo(trackingID, mDriverLat!!, mDriverLong!!, mDestinationLat!!, mDestinationLong!!, mDuration!!,mDistance!!)
            firebaseDB.setInProgressPackageData(progressPackageVo)
        }
    }

    override fun checkingPackageStatus(status: String) {
        if(status == PackageStatus.IN_PROGRESS){
            view?.hideProcessPackageLayout()
        }else if(status == PackageStatus.PENDING){
            view?.showProcessPackageLayout()
        }
    }

    override fun getTrackingPackageFirebase(trackingID: String) {
        firebaseDB.gettingTrackingData(trackingID, object : FirebaseDB.GetFireBaseCallBack{
            override fun onSuccess(dataSnapshot: DataSnapshot) {
                if(dataSnapshot != null){
                    dataSnapshot.children.forEach {
                        mDestinationLat = it.child("destinationCurrentLat").value.toString()
                        mDestinationLong = it.child("destinationCurrentLong").value.toString()
                        mDuration = it.child("duration").value.toString()
                        mDistance = it.child("distance").value.toString()
                    }
                    draw()
                }

            }

            override fun onError(databaseError: DatabaseError) {
                Log.e(javaClass.name, databaseError.message.toString())
            }

        })
    }

    override fun deleteTrackingId(trackingID: String) {
        firebaseDB.removeTrackingData(trackingID)
    }


}