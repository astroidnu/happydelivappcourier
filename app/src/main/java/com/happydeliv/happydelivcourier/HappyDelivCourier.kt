package com.happydeliv.happydelivcourier

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import com.happydeliv.happydelivcourier.di.component.AppComponent
import com.happydeliv.happydelivcourier.di.component.DaggerAppComponent
import com.happydeliv.happydelivcourier.di.module.NetworkModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
class HappyDelivCourier :Application(), HasActivityInjector {
    companion object {
        @JvmStatic lateinit var instance: HappyDelivCourier
        @JvmStatic lateinit var appComponent: AppComponent
    }

    @Inject
    lateinit var mActivityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        //Initialize Multidex for prevent limit over 80k method
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        //Set instance
        instance = this
        //Create App Component
        appComponent = createComponent()
        appComponent.inject(this)
    }

    /**
     * Initialize Dependency Injection With Dagger
     * Level DI Application
     * */


    private fun createComponent(): AppComponent {
        return DaggerAppComponent.builder()
                .application(this)
                .networkModule(NetworkModule(this))
                .build()
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return mActivityDispatchingAndroidInjector
    }
}