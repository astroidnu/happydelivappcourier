package com.happydeliv.happydelivcourier.ui.activity.scanbarcode

import com.google.gson.Gson
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.di.scope.ActivityScope
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.ui.activity.addtracking.AddTrackingActivity
import com.happydeliv.happydelivcourier.ui.activity.addtracking.AddTrackingContract
import com.happydeliv.happydelivcourier.ui.activity.addtracking.AddTrackingPresenter
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.ActivityNavigation
import com.happydeliv.happydelivcourier.utils.AppSchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ibnumuzzakkir on 25/02/18.
 * Android Engineer
 * SCO Project
 */
@Module
class ScanBarcodeModule{
    @Provides
    @ActivityScope
    internal fun provideScanBarcodeActivity(scanBarcodeActivity: ScanBarcodeActivity) : ScanBarcodeContract.View{
        return scanBarcodeActivity
    }

    @Provides
    @ActivityScope
    internal fun provideScanBarcodePresenter(networkService: NetworkService, compositeDisposable: CompositeDisposable,
                                             schedulerProvider: AppSchedulerProvider, loginSession: LoginSession, gson: Gson) : ScanBarcodePresenter {
        return ScanBarcodePresenter(networkService,compositeDisposable, schedulerProvider, loginSession, gson)
    }

    @Provides
    @ActivityScope
    internal fun provideActivityNavigation(scanBarcodeActivity: ScanBarcodeActivity): ActivityNavigation {
        return ActivityNavigation(scanBarcodeActivity)
    }
}