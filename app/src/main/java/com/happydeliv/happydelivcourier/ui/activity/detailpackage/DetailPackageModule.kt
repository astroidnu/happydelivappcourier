package com.happydeliv.happydelivcourier.ui.activity.detailpackage

import com.google.gson.Gson
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.di.scope.ActivityScope
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.ActivityNavigation
import com.happydeliv.happydelivcourier.utils.AppSchedulerProvider
import com.happydeliv.happydelivcourier.utils.FirebaseDB
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ibnumuzzakkir on 07/02/18.
 * Android Engineer
 * SCO Project
 */
@Module
class DetailPackageModule{
    @Provides
    @ActivityScope
    internal fun provideDetailPackageActivity(detailPackageActivity: DetailPackageActivity) : DetailPackageContract.View{
        return detailPackageActivity
    }


    @Provides
    @ActivityScope
    internal fun provideDetailPackagePresenter(networkService: NetworkService, compositeDisposable: CompositeDisposable,
                                               schedulerProvider: AppSchedulerProvider, loginSession: LoginSession, gson: Gson, firebaseDB: FirebaseDB) : DetailPackagePresenter{
        return DetailPackagePresenter(networkService,compositeDisposable, schedulerProvider, loginSession, gson, firebaseDB)
    }


    @Provides
    @ActivityScope
    internal fun provideActivityNavigation(detailPackageActivity: DetailPackageActivity): ActivityNavigation {
        return ActivityNavigation(detailPackageActivity)
    }
}