package com.happydeliv.happydelivcourier.ui.fragment.home.bestroute

import android.graphics.Color
import android.util.Log
import com.google.gson.Gson
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.utils.SchedulerProvider
import com.scoproject.newsapp.ui.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 23/02/18.
 * Android Engineer
 * SCO Project
 */
class BestRoutePresenter @Inject constructor(private val networkService: NetworkService,
                                             disposable : CompositeDisposable,
                                             scheduler : SchedulerProvider,
                                             val gson : Gson,
                                             val loginSession: LoginSession)  : BasePresenter<BestRouteContract.View>(disposable,scheduler), BestRouteContract.UserActionListener{
    override fun getBestRouteData(currLat: String, currLong: String) {
        var userInfo = HashMap<String, Any>()
        userInfo.put("phone", loginSession.getPhoneNumber())
        userInfo.put("token", loginSession.getLoginToken())


        var data = HashMap<String, String>()
        data.put("current_lat", currLat)
        data.put("current_longi", currLong)
        if(disposable.size() > 0){
            disposable.clear()
        }

        disposable.add(
                networkService.getBestRoute(gson.toJson(data),gson.toJson(userInfo))
                        .subscribeOn(scheduler.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            result ->
                            view?.hideLoading()
                            if(result.resultCode == 1){
                                for(res in result.data){
                                    Log.d(javaClass.name,gson.toJson(res))
                                    view?.addMarker(
                                            res.latAddress.toDouble(), res.longinAddress.toDouble(),
                                            R.drawable.ic_marker_kurir_tujuan,
                                            res.recipientAddress,res.sequence,
                                            false)
                                    view?.drawDirection(
                                            res.previousLati.toDouble(),
                                            res.previousLongi.toDouble(),
                                            res.latAddress.toDouble(),
                                            res.longinAddress.toDouble(), Color.GRAY)
                                }
                            }
//                            if(result.resultCode == 1){
//                                deleteTrackingId(trackingID)
//                                view?.navigateToDashboard()
//                            }else{
//                                view?.showError(result.resultMessage)
//                            }

                        },{
                            err -> Log.e(javaClass.name, err.message.toString())
                            view?.hideLoading()
                            view?.showError("please try again")
                        })

        )
    }

}