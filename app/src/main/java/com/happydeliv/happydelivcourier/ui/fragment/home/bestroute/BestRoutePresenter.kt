package com.happydeliv.happydelivcourier.ui.fragment.home.bestroute

import android.graphics.Color
import android.util.Log
import com.google.gson.Gson
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.utils.SchedulerProvider
import com.happydeliv.happydelivcourier.vo.BestRouteVo
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
    override fun getBestRouteData(currLat: String, currLong: String){
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
                                view?.tampungNilai(result.data)
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