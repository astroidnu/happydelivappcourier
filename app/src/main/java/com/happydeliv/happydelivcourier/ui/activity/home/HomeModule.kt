package com.happydeliv.happydelivcourier.ui.activity.home

import com.google.gson.Gson
import com.happydeliv.happydelivapp.ui.fragment.home.inprogress.InProgressPresenter
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.api.NetworkService
import com.happydeliv.happydelivcourier.di.scope.ActivityScope
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.ActivityNavigation
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.FragmentNavigation
import com.happydeliv.happydelivcourier.ui.fragment.home.bestroute.BestRoutePresenter
import com.happydeliv.happydelivcourier.ui.fragment.home.history.HistoryPresenter
import com.happydeliv.happydelivcourier.ui.fragment.home.myaccount.MyAccountPresenter
import com.happydeliv.happydelivcourier.utils.AppSchedulerProvider
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
@Module
class HomeModule{
    @Provides
    @ActivityScope
    internal fun provideHomeActivity(homeActivity: HomeActivity) : HomeContract.View{
        return homeActivity
    }

    @Provides
    @ActivityScope
    internal fun provideActivityNavigation(homeActivity: HomeActivity) =  ActivityNavigation(homeActivity)


    @Provides @ActivityScope
    internal fun provideFragmentNavigation(homeActivity: HomeActivity) = FragmentNavigation(homeActivity, R.id.frame_home)


    /**
     * Provide all fragment presenter
     * */

    @Provides @ActivityScope
    internal  fun provideInProgressPresenter(networkService: NetworkService, compositeDisposable: CompositeDisposable,
                                             schedulerProvider: AppSchedulerProvider, loginSession: LoginSession, gson: Gson)
            = InProgressPresenter(networkService,compositeDisposable, schedulerProvider, gson,loginSession)


    @Provides @ActivityScope
    internal  fun provideHistoryPresenter(networkService: NetworkService, compositeDisposable: CompositeDisposable,
                                             schedulerProvider: AppSchedulerProvider, loginSession: LoginSession, gson: Gson)
            = HistoryPresenter(networkService,compositeDisposable, schedulerProvider, gson,loginSession)



    @Provides @ActivityScope
    internal  fun provideMyAccountPresenter(networkService: NetworkService, compositeDisposable: CompositeDisposable,
                                          schedulerProvider: AppSchedulerProvider, loginSession: LoginSession, gson: Gson)
            =  MyAccountPresenter(networkService,compositeDisposable, schedulerProvider, gson,loginSession)


    @Provides @ActivityScope
    internal  fun provideBestRoutePresenter(networkService: NetworkService, compositeDisposable: CompositeDisposable,
                                            schedulerProvider: AppSchedulerProvider, loginSession: LoginSession, gson: Gson)
            =  BestRoutePresenter(networkService,compositeDisposable, schedulerProvider, gson,loginSession)

}