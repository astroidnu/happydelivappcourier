package com.happydeliv.happydelivcourier.ui.activity.home

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.View
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.ui.common.navigationcontroller.FragmentNavigation
import com.scoproject.newsapp.ui.common.BaseActivity
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_toolbar.*
import javax.inject.Inject
import com.google.firebase.iid.FirebaseInstanceId



/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class HomeActivity : BaseActivity(), HasSupportFragmentInjector, HomeContract.View{
    @Inject
    lateinit var mFragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var mFragmentNavigation: FragmentNavigation

    private val mBottomNavDashboard by lazy {
        dashboard_bottom_bar
    }

    private val mBtnAddPackage by lazy {
        imgbtn_add_package
    }


    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupBottomNavigation()
        setupUIListener()
        //Getting firebaseinstanceid token
        val token = FirebaseInstanceId.getInstance().token
        Log.d(javaClass.name, token)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    override fun navigateToInProgressPage() {
        mFragmentNavigation.navigateToInProgressPage()
    }

    override fun navigateToHistoryPage() {
        mFragmentNavigation.navigateToHistoryPage()
    }

    override fun navigateToMyAccountPage() {
        mFragmentNavigation.navigateToMyAccountPage()
    }

    override fun setupUIListener() {
        mBtnAddPackage.setOnClickListener {
            mActivityNavigation.navigateToAddTrackingPage()
        }

        imgbtn_add_package.setOnClickListener {
            mActivityNavigation.navigateToAddTrackingPage()
        }
    }
    override fun setupBottomNavigation() {
        //Setup Default Bottom nav index selected
        mBottomNavDashboard.selectTabAtPosition(0)
        //Setup listener for bottom nav
        mBottomNavDashboard.setOnTabSelectListener { tabId ->
           when(tabId){
               R.id.navigation_item_home -> navigateToInProgressPage()
               R.id.navigation_item_history -> navigateToHistoryPage()
               R.id.navigation_item_my_account -> navigateToMyAccountPage()
               R.id.navigation_item_best_route -> mFragmentNavigation.navigateToBestRoutePage()
               else -> navigateToInProgressPage()
           }
       }
    }

    override fun showBtnAddPackage() {
        mBtnAddPackage.visibility = View.VISIBLE
    }

    override fun hideBtnAddPackage() {
        mBtnAddPackage.visibility = View.GONE
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return mFragmentDispatchingAndroidInjector
    }

    override fun onBackPressed() {
        var mOpenNavigationId = mBottomNavDashboard.currentTabId
        if (mOpenNavigationId != R.id.navigation_item_home) {
            mBottomNavDashboard.selectTabWithId(R.id.navigation_item_home)
        } else {
            this.finishAffinity()
        }
    }

}