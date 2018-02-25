package com.happydeliv.happydelivcourier.ui.common.navigationcontroller

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.happydeliv.happydelivcourier.ui.fragment.home.bestroute.BestRouteFragment
import com.happydeliv.happydelivcourier.ui.fragment.home.history.HistoryFragment
import com.happydeliv.happydelivcourier.ui.fragment.home.inprogress.InProgressFragment
import com.happydeliv.happydelivcourier.ui.fragment.home.myaccount.MyAccountFragment
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class FragmentNavigation @Inject constructor(val activity: AppCompatActivity, val containerId: Int) {
    var mFragmentManager = activity.supportFragmentManager


    /**
     * Navigate To In Progress Page
     * */

    fun navigateToInProgressPage() {
        val mInProgressFragment = InProgressFragment()
        loadFragment(mInProgressFragment, "inprogress")
    }


    /**
     * Navigate To History Page
     * */

    fun navigateToHistoryPage() {
        val mHistoryFragment = HistoryFragment()
        loadFragment(mHistoryFragment,"history")
    }



    /**
     * Navigate To History Page
     * */

    fun navigateToBestRoutePage() {
        val mBestRouteFragment = BestRouteFragment()
        loadFragment(mBestRouteFragment,"bestroute")
    }


    /**
     * Navigate To My Account Page
     * */

    fun navigateToMyAccountPage() {
        val mMyAccountFragment = MyAccountFragment()
        loadFragment(mMyAccountFragment, "myaccount")
    }

    /**
     * Load Fragment
     * Handling all load fragment navigation
     * */
    private fun loadFragment(fragment: Fragment?, tag :String) {
        mFragmentManager.beginTransaction()
                .replace(containerId, fragment,tag)
                .addToBackStack(null)
                .commitAllowingStateLoss()
    }

    /**
     * Get open current fragment
     * */

    fun getOpenFragment(): Fragment {
        return mFragmentManager.findFragmentById(containerId)
    }
}