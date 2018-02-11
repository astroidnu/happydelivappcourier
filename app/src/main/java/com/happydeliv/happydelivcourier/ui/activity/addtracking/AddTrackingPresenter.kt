package com.happydeliv.happydelivcourier.ui.activity.addtracking

import com.google.gson.Gson
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.utils.SchedulerProvider
import com.scoproject.newsapp.ui.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 07/02/18.
 * Android Engineer
 * SCO Project
 */
class AddTrackingPresenter @Inject constructor(private val networkService: NetworkService,
                                               disposable : CompositeDisposable,
                                               scheduler : SchedulerProvider,
                                               val loginSession: LoginSession,
                                               val gson: Gson) : BasePresenter<AddTrackingContract.View>(disposable,scheduler),AddTrackingContract.UserActionListener{
    override fun addTrackingPackage(trackingId: String) {
        if(trackingId.isNotEmpty()){
            val userInfo = HashMap<String, Any>()
            userInfo.put("phone", loginSession.getPhoneNumber())
            userInfo.put("token", loginSession.getLoginToken())

            val trackingData = HashMap<String, Any>()
            trackingData.put("resi_number", trackingId)

            view?.showLoading()
            disposable.add(
                    networkService.addTrackPage(gson.toJson(trackingData), gson.toJson(userInfo))
                            .subscribeOn(scheduler.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({
                                result ->
                                view?.hideLoading()
                                if(result.resultCode == 1){
                                    view?.navigateToHome()
                                }else{
                                    view?.showError(result.resultMessage)
                                }
                            },{
                                _ ->
                                view?.hideLoading()
                                view?.showError("Please Try again")
                            })
            )
        }else{
            view?.showError("Harap masukkan no resi")
        }
    }

}