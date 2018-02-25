package com.happydeliv.happydelivcourier.ui.fragment.home.myaccount

import android.util.Log
import com.google.gson.Gson
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.utils.SchedulerProvider
import com.scoproject.newsapp.ui.common.BasePresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class MyAccountPresenter  @Inject constructor(private val networkService: NetworkService,
                                              disposable : CompositeDisposable,
                                              scheduler : SchedulerProvider,
                                              val gson : Gson,
                                              val loginSession: LoginSession) : BasePresenter<MyAccountContract.View>(disposable,scheduler), MyAccountContract.UserActionListener{
    override fun gettingUserInformation() {
        view?.showLoading()
        var userInfo = HashMap<String, Any>()
        userInfo.put("phone", loginSession.getPhoneNumber())
        userInfo.put("token", loginSession.getLoginToken())
        if(disposable.size() > 0){
            disposable.clear()
        }
        disposable.add(
                networkService.getCourierInformation(gson.toJson(userInfo))
                        .subscribeOn(scheduler.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            result ->
                            view?.hideLoading()
                            if(result.resultCode == 1){
                                val data = result.data
                                view?.setupContent(data.name, data.email, data.phone,data.company)
                            }else{
                                view?.showError(result.resultMessage)
                            }

                        },{
                            err ->
                            Log.e(javaClass.name, err.message.toString())
                            view?.hideLoading()
                            view?.showError("please try again")
                        })
        )

    }

    override fun logout() {
        loginSession.clear()
        view?.navigateToLoginPage()
    }

}