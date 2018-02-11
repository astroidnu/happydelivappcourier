package com.happydeliv.happydelivcourier.ui.activity.addtracking

import com.google.gson.Gson
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.di.scope.ActivityScope
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.ActivityNavigation
import com.happydeliv.happydelivcourier.utils.AppSchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ibnumuzzakkir on 07/02/18.
 * Android Engineer
 * SCO Project
 */
@Module
class AddTrackingModule{
    @Provides
    @ActivityScope
    internal fun provideAddTackingActivity(addTrackingActivity: AddTrackingActivity) : AddTrackingContract.View{
        return addTrackingActivity
    }

    @Provides
    @ActivityScope
    internal fun provideAddTrackingPresenter(networkService: NetworkService, compositeDisposable: CompositeDisposable,
                                             schedulerProvider: AppSchedulerProvider, loginSession: LoginSession, gson: Gson) : AddTrackingPresenter{
        return AddTrackingPresenter(networkService,compositeDisposable, schedulerProvider, loginSession, gson)
    }

    @Provides
    @ActivityScope
    internal fun provideActivityNavigation(addTrackingActivity: AddTrackingActivity): ActivityNavigation {
        return ActivityNavigation(addTrackingActivity)
    }
}