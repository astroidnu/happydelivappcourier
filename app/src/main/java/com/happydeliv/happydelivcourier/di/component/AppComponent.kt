package com.happydeliv.happydelivcourier.di.component

import android.app.Application
import com.happydeliv.happydelivcourier.HappyDelivCourier
import com.happydeliv.happydelivcourier.di.module.AppModule
import com.happydeliv.happydelivcourier.di.module.FirebaseJobDispatcherModule
import com.happydeliv.happydelivcourier.di.module.NetworkModule
import com.happydeliv.happydelivcourier.di.module.builder.CommonActivityBuilder
import com.happydeliv.happydelivcourier.services.LocationUpdateService
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
@Singleton
@Component(modules = [
    (AndroidInjectionModule::class),
    (AppModule::class),
    (CommonActivityBuilder::class),
    (NetworkModule::class),
    (FirebaseJobDispatcherModule::class)])
 interface  AppComponent{

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun networkModule(networkModule: NetworkModule): Builder
        fun firebaseModule(firebaseJobDispatcherModule: FirebaseJobDispatcherModule): Builder
        fun build(): AppComponent
    }

    fun inject(happyDelivCourier: HappyDelivCourier)
    fun inject(locationUpdateService: LocationUpdateService)
}