package  com.happydeliv.happydelivcourier.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.firebase.jobdispatcher.JobService

/**
 * Created by ibnumuzzakkir on 11/13/17.
 * Android Engineer
 * SCO Project
 *
 * Base Job Service foundation for job service
 * @Extend to Firebase Job Dispatcher
 */

abstract class BaseJobService : JobService(){

    var mIntent: Intent? = null
    var mPendingIntent: PendingIntent? = null

    abstract fun setupComponent()

    /**
     * Initialize Component
     * Make Injection After in init
     * */

    init {
        setupComponent()
    }
}