package com.happydeliv.happydelivcourier.ui.activity.addtracking

import android.Manifest
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.happydeliv.happydelivcourier.R
import com.scoproject.newsapp.ui.common.BaseActivity
import com.tedpark.tedpermission.rx2.TedRx2Permission
import kotlinx.android.synthetic.main.activity_add_tracking.*
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 07/02/18.
 * Android Engineer
 * SCO Project
 */
class AddTrackingActivity : BaseActivity(), AddTrackingContract.View{
    @Inject
    lateinit var mAddTrackingPresenter : AddTrackingPresenter

    override fun onActivityReady(savedInstanceState: Bundle?) {
        mAddTrackingPresenter.attachView(this)
        TedRx2Permission.with(this)
                .setPermissions(Manifest.permission.CAMERA)
                .request()
                .subscribe({ tedPermissionResult ->
                    if (tedPermissionResult.isGranted) {
                       //Permission Granted
                    } else {
                        //Denied by user
                    }
                }, { throwable -> throwable.message}, { })
        setupUIListener()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_tracking
    }

    override fun showLoading() {
        pb_add_tracking.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        pb_add_tracking.visibility = View.GONE
    }

    override fun setupUIListener() {
        btn_add_tracking_submit.setOnClickListener {
            val mTrackingId = et_add_tracking_id.text.toString()
            mAddTrackingPresenter.addTrackingPackage(mTrackingId)
        }

        imgbtn_back.setOnClickListener {
            this.finish()
        }

        scanBarcode.setOnClickListener{
            mActivityNavigation.navigateToQRScan()
        }
    }

    override fun onBackPressed() {
        mActivityNavigation.navigateToHomePage()
        super.onBackPressed()
    }
    override fun navigateToHome() {
       onBackPressed()
    }

    override fun showError(content: String) {
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
    }
}