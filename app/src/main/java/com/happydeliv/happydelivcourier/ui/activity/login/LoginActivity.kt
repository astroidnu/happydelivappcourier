package com.happydeliv.happydelivcourier.ui.activity.login

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.Toast
import com.happydeliv.happydelivcourier.R
import com.scoproject.newsapp.ui.common.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 05/02/18.
 * Android Engineer
 * SCO Project
 */
class LoginActivity : BaseActivity(), LoginContract.View{
    @Inject
    lateinit var mLoginPresenter : LoginPresenter

    private val mEtLoginUserName by lazy { et_login_username }

    private val mEtPassword by lazy { et_login_password }

    private val mTvSignUpHere by lazy { tv_sign_up_here }

    private val mBtnSignIn by lazy { btn_sign_in }



    override fun onActivityReady(savedInstanceState: Bundle?) {
        mLoginPresenter.attachView(this)
        mLoginPresenter.checkIsLogin()
        tv_sign_up_here.text = Html.fromHtml("Klik <a href=\'http://happydeliv.com/login_guide\'>disini</a> untuk membaca petunjuk Sign in untuk kurir")
        tv_sign_up_here.movementMethod = LinkMovementMethod.getInstance()
        setupUIListener()
    }

    override fun setupUIListener() {
        mBtnSignIn.setOnClickListener {
            val mUsername = mEtLoginUserName.text.toString()
            val mPassword = mEtPassword.text.toString()
            mLoginPresenter.login(mUsername, mPassword)
        }
    }

    override fun getLayoutId(): Int {
       return R.layout.activity_login
    }

    override fun showError(content: String) {
        Toast.makeText(applicationContext, content, Toast.LENGTH_SHORT).show()
    }

    override fun navigateToHomePage() {
        mActivityNavigation.navigateToHomePage()
    }

    override fun hideLoading() {
        pb_login.visibility = View.GONE
    }

    override fun showLoading() {
        pb_login.visibility = View.VISIBLE
    }


}