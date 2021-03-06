package com.happydeliv.happydelivcourier.ui.fragment.home.myaccount

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.happydeliv.happydelivapp.ui.common.BaseFragment
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.session.LoginSession
import com.happydeliv.happydelivcourier.ui.activity.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_my_account.*
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class MyAccountFragment : BaseFragment(), MyAccountContract.View{

    @Inject
    lateinit var mMyAccountPresenter : MyAccountPresenter

    @Inject
    lateinit var mLoginSession : LoginSession

    private val mProfileEmail by lazy{
        tv_profile_email
    }

    private val mProfilePhone by lazy{
        tv_profile_phone
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_my_account
    }

    override fun onLoadFragment(saveInstance: Bundle?) {
        (activity as HomeActivity).hideBtnAddPackage()
        mMyAccountPresenter.attachView(this)
        setupUIListener()
        mMyAccountPresenter.gettingUserInformation()
    }

    override fun setupUIListener() {
        btn_sign_out.setOnClickListener {
            mMyAccountPresenter.logout()
        }
    }

    override fun setupContent(name : String, email: String, phoneNo: String, companyName: String) {
        tv_profile_name.text  = name
        mProfileEmail.text = email
        mProfilePhone.text = phoneNo
        tv_company_name.text = companyName
    }

    override fun navigateToLoginPage() {
        (activity as HomeActivity).mActivityNavigation.navigateToLoginPage()
    }

    override fun onStop() {
        mMyAccountPresenter.detachView()
        super.onStop()
    }

    override fun showLoading() {
        pb_my_account_loading.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb_my_account_loading.visibility = View.GONE
    }

    override fun showError(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }



}