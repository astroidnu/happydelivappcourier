package com.happydeliv.happydelivcourier.ui.activity.splashscreen

import android.os.Bundle
import android.os.Handler
import com.happydeliv.happydelivcourier.R
import com.scoproject.newsapp.ui.common.BaseActivity

/**
 * Created by ibnumuzzakkir on 05/02/18.
 * Android Engineer
 * SCO Project
 */
class SplashActivity : BaseActivity(), SplashContract.View{

    private val mDelayHandler by lazy{
        Handler()
    }
    private val SPLASH_DELAY: Long = 3000 //3 seconds

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            mActivityNavigation.navigateToLoginPage()
        }
    }

    override fun onActivityReady(savedInstanceState: Bundle?) {
        //Navigate with delay
        mDelayHandler.postDelayed(mRunnable, SPLASH_DELAY)
    }

    override fun onDestroy() {
        mDelayHandler.removeCallbacks(mRunnable)
        super.onDestroy()
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_splashscreen
    }

}