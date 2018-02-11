package com.scoproject.newsapp.ui.common

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.hardware.TriggerEvent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.jobdispatcher.*
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.ActivityNavigation
import com.scoproject.weatherapp.ui.base.BaseView
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 24/01/18.
 * Android Engineer
 * SCO Project
 */
abstract class BaseActivity : AppCompatActivity(),BaseView, LifecycleOwner{

    @Inject
    lateinit var mActivityNavigation : ActivityNavigation

    private var presenter: BasePresenter<*>? = null


    private val mLifecycleRegistry : LifecycleRegistry by lazy{
        LifecycleRegistry(this)
    }

    private val mFirebaseJobDispatcher by lazy {
        FirebaseJobDispatcher(GooglePlayDriver(this))
    }

    /**
     * This function replace onCreate as main function run in activity
     * Auto Dependency Injection
     * */

    abstract fun onActivityReady(savedInstanceState: Bundle?)

    /**
     * Getting Layout ID from activity
     * */

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        //Auto DI
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        onActivityReady(savedInstanceState)
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
        this.presenter = presenter
    }

    /**
     * Start Firebase Job Dispatcher
     * @Param Class, Job Tag, Frequency, Tolerence
     */

    fun <T> startFirebaseJobDispatcher(tClass: Class<T>, jobTag: String, frequency: Int, tolerance: Int) {
        val myJob = mFirebaseJobDispatcher.newJobBuilder()
                .setService(tClass as Class<out JobService>)
                .setTag(jobTag)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.NOW)
                .build()
        mFirebaseJobDispatcher.mustSchedule(myJob)
    }

    /**
     * Stop firebase job dispatcher
     * @Param tag job
     */

    fun stopFirebaseJobDispatcher(tagName: String) {
        Log.d(javaClass.name, "OnStopJob")
        mFirebaseJobDispatcher.cancelAll()
    }


}