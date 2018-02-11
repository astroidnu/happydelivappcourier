package com.happydeliv.happydelivapp.ui.fragment.home.inprogress

import android.util.Log
import com.google.gson.Gson
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.ui.fragment.home.inprogress.InProgressContract
import com.happydeliv.happydelivcourier.utils.SchedulerProvider
import com.happydeliv.happydelivcourier.vo.PackageVo
import com.scoproject.newsapp.ui.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class InProgressPresenter @Inject constructor(private val networkService: NetworkService,
                                              disposable : CompositeDisposable,
                                              scheduler : SchedulerProvider,
                                              val gson :Gson,
                                              val loginSession: LoginSession) : BasePresenter<InProgressContract.View>(disposable,scheduler), InProgressContract.UserActionListener{
    override fun getTrackingList() {
        view?.showLoading()
        var userInfo = HashMap<String, Any>()
        userInfo.put("phone", loginSession.getPhoneNumber())
        userInfo.put("token", loginSession.getLoginToken())
        disposable.add(
                networkService.getPackageList(gson.toJson(userInfo))
                        .subscribeOn(scheduler.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            result ->
                            view?.hideLoading()
                            val listPackage = result.data
                            if(result.resultCode == 1){
                                if(listPackage.isNotEmpty()){
                                    view?.hideEmptyLayout()
                                    view?.setupAdapter(listPackage)
                                }else{
                                    view?.showEmptyLayout()
                                }
                            }else{
                                view?.showEmptyLayout()
                            }

                        },{
                            err -> Log.e(javaClass.name, err.message.toString())
                            view?.hideLoading()
                            view?.showError("please try again")
                        })
        )
    }

}