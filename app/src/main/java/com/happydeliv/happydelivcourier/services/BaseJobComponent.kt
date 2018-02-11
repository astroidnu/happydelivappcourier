package com.happydeliv.happydelivcourier.services

import com.happydeliv.happydelivcourier.di.scope.AppScope
import dagger.Subcomponent

/**
 * Created by ibnumuzzakkir on 11/15/17.
 * Android Engineer
 * SCO Project
 */
@AppScope
interface BaseJobComponent {
    fun inject(locationUpdateService: LocationUpdateService)
}