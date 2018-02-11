package com.happydeliv.happydelivcourier.ui.activity.detailpackage

import android.util.Log
import com.google.gson.Gson
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.utils.FirebaseDB
import com.happydeliv.happydelivcourier.utils.SchedulerProvider
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
                                val progressPackageVo = ProgressPackageVo(trackingID,"6.20","10")
                                firebaseDB.setInProgressPackageData(progressPackageVo)
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

}