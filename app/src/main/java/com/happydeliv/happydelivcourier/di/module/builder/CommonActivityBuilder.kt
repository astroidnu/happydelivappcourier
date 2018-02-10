package com.happydeliv.happydelivcourier.di.module.builder

import com.happydeliv.happydelivcourier.di.scope.ActivityScope
import com.happydeliv.happydelivcourier.ui.activity.splashscreen.SplashActivity
import com.happydeliv.happydelivcourier.ui.activity.splashscreen.SplashModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by ibnumuzzakkir on 05/02/18.
 * Android Engineer
 * SCO Project
 */
@Module
abstract class CommonActivityBuilder{
    @ActivityScope
    @ContributesAndroidInjector(modules = [(SplashModule::class)])
    internal abstract fun bindSplashActivity(): SplashActivity

}