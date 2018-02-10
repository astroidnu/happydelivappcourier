package com.happydeliv.happydelivcourier.ui.common.navigationcontroller

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class FragmentNavigation @Inject constructor(val activity: AppCompatActivity, val containerId: Int) {
    var mFragmentManager = activity.supportFragmentManager

    /**
     * Load Fragment
     * Handling all load fragment navigation
     * */
    private fun loadFragment(fragment: Fragment?) {
        mFragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commitAllowingStateLoss()
    }

    /**
     * Get open current fragment
     * */

    fun getOpenFragment(): Fragment {
        return mFragmentManager.findFragmentById(containerId)
    }
}