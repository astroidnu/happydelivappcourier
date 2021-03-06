package com.happydeliv.happydelivcourier.di.module

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.utils.AppSchedulerProvider
import com.happydeliv.happydelivcourier.utils.FirebaseDB
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
@Module
class AppModule{
    @Provides
    @Singleton
    fun provideContext(application: Application): Context {
        return application
    }

    @Provides
    @Singleton
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun provideSchedulerProvider() = AppSchedulerProvider()

    @Singleton
    @Provides
    fun provideLoginSession() =  LoginSession()

    @Singleton
    @Provides
    fun provideGson() =  Gson()

    @Singleton
    @Provides
    fun provideFirebaseDB(context: Context) =  FirebaseDB(context)
}