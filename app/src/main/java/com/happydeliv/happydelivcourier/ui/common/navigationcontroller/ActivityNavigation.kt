package com.happydeliv.happydelivcourier.ui.common.navigationcontroller

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 24/01/18.
 * Android Engineer
 * SCO Project
 */

class ActivityNavigation @Inject constructor(val activity:AppCompatActivity){

    /**
     * Intent Common Function
     * Handling new intent
     * */
    fun <T> newIntent(context: Context, cls: Class<T>): Intent {
        return Intent(context, cls)
    }

    fun newIntentUri(label: String, uri: Uri): Intent {
        return Intent(label, uri)
    }
}