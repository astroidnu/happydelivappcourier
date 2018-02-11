package com.happydeliv.happydelivcourier.di.module.builder

import com.happydeliv.happydelivcourier.di.scope.ActivityScope
import com.happydeliv.happydelivcourier.ui.activity.addtracking.AddTrackingActivity
import com.happydeliv.happydelivcourier.ui.activity.addtracking.AddTrackingModule
import com.happydeliv.happydelivcourier.ui.activity.detailpackage.DetailPackageActivity
import com.happydeliv.happydelivcourier.ui.activity.detailpackage.DetailPackageModule
import com.happydeliv.happydelivcourier.ui.activity.home.HomeActivity
import com.happydeliv.happydelivcourier.ui.activity.home.HomeModule
import com.happydeliv.happydelivcourier.ui.activity.login.LoginActivity
import com.happydeliv.happydelivcourier.ui.activity.login.LoginModule
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


    @ActivityScope
    @ContributesAndroidInjector(modules = [(LoginModule::class)])
    internal abstract fun bindLoginActivity(): LoginActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(HomeModule::class),(HomeActivityBuilder::class)])
    internal abstract fun bindHomeActivity(): HomeActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(DetailPackageModule::class)])
    internal abstract fun bindDetailPackageActivity(): DetailPackageActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [(AddTrackingModule::class)])
    internal abstract fun bindAddTrackingActivity(): AddTrackingActivity


}