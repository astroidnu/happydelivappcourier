package com.happydeliv.happydelivcourier.ui.fragment.home.bestroute

import android.os.Bundle
import android.util.Log
import com.happydeliv.happydelivapp.ui.common.BaseFragment
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.ui.activity.home.HomeActivity

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
class BestRouteFragment : BaseFragment(), BestRouteContract.View{
    override fun getLayoutId(): Int {
       return R.layout.fragment_best_route
    }

    override fun onLoadFragment(saveInstance: Bundle?) {
        (activity as HomeActivity).hideBtnAddPackage()
        Log.d(javaClass.name, "onLoadFragment()")
    }

}