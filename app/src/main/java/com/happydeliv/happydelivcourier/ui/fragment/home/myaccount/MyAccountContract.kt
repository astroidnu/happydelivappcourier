package com.happydeliv.happydelivcourier.ui.fragment.home.myaccount

import com.scoproject.weatherapp.ui.base.BaseView

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class MyAccountContract{
    interface View : BaseView {
        fun setupContent(email :String, phoneNo :String, companyName :String)
        fun navigateToLoginPage()
        fun setupUIListener()
        fun showLoading()
        fun hideLoading()
        fun showError(msg :String)
    }

    interface UserActionListener {
        fun logout()
        fun gettingUserInformation()
    }
}