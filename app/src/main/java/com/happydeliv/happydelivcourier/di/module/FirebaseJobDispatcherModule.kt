package com.happydeliv.happydelivcourier.di.module

import android.content.Context
import com.firebase.jobdispatcher.FirebaseJobDispatcher
import com.firebase.jobdispatcher.GooglePlayDriver
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by ibnumuzzakkir on 11/15/17.
 * Android Engineer
 * SCO Project
 */
@Module
class FirebaseJobDispatcherModule(val context:Context) {
    var mFirebaseJobDispatcher : FirebaseJobDispatcher?= null

    @Provides
    @Singleton
    fun provideFirebaseJobDispatcher() : FirebaseJobDispatcher{
        if(mFirebaseJobDispatcher == null){
            mFirebaseJobDispatcher = FirebaseJobDispatcher(GooglePlayDriver(context))
        }
        return mFirebaseJobDispatcher!!
    }
}