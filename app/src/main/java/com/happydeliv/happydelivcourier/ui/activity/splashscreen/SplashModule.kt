package com.happydeliv.happydelivcourier.ui.activity.splashscreen

import com.happydeliv.happydelivcourier.di.scope.ActivityScope
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.ActivityNavigation
import dagger.Module
import dagger.Provides

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
@Module
class SplashModule{
    @Provides
    @ActivityScope
    internal fun provideSplashActivity(splashActivity: SplashActivity) : SplashContract.View{
        return splashActivity
    }

    @Provides
    @ActivityScope
    internal fun provideActivityNavigation(splashActivity: SplashActivity): ActivityNavigation {
        return ActivityNavigation(splashActivity)
    }
}