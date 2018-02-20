package com.happydeliv.happydelivcourier.ui.fragment.home.inprogress

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.happydeliv.happydelivapp.ui.common.BaseFragment
import com.happydeliv.happydelivapp.ui.fragment.home.inprogress.InProgressPresenter
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.adapter.setUp
import com.happydeliv.happydelivcourier.ui.activity.home.HomeActivity
import com.happydeliv.happydelivcourier.vo.PackageVo
import kotlinx.android.synthetic.main.fragment_in_progress.*
import kotlinx.android.synthetic.main.item_package.view.*
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class InProgressFragment : BaseFragment(), InProgressContract.View{
    @Inject
    lateinit var mInProgressPresenter : InProgressPresenter

    private val mLinearLayoutManager = LinearLayoutManager(context)

    override fun getLayoutId(): Int {
        return R.layout.fragment_in_progress
    }

    override fun onLoadFragment(saveInstance: Bundle?) {
        mInProgressPresenter.attachView(this)
        (activity as HomeActivity).showBtnAddPackage()
        setupUIListener()
    }

    override fun onResume() {
        mInProgressPresenter.getTrackingList()
        super.onResume()
    }


    override fun setupUIListener() {
        btn_add_tracking.setOnClickListener {
            (activity as HomeActivity).mActivityNavigation.navigateToAddTrackingPage()
        }
    }

    override fun setupAdapter(data: List<PackageVo>) {
        rv_in_progress_package?.setUp(data,R.layout.item_package,{
            tv_package_recipient_name.text = it.recipientName
            tv_package_resi_no.text ="Resi no : " +  it.resiNumber
            tv_package_status.text = it.status
            tv_package_track_id.text = "Track id : " + it.trackId

            if(it.status.trim().toLowerCase() == "pending"){
                view_package_color.setBackgroundColor(resources.getColor(R.color.colorDarkOrange))
            }else{
                view_package_color.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            }

            Glide.with(this)
                    .load(it.recipientImage)
                    .into(iv_recipient_logo)
        },{
            (activity as HomeActivity).mActivityNavigation.navigateToDetailPage(it.trackId)
//            if(it.status.trim().toLowerCase() != "pending"){
//
//            }

        },mLinearLayoutManager)
    }

    override fun hideEmptyLayout() {
        ll_in_progress_no_package?.visibility = View.GONE
        rv_in_progress_package?.visibility = View.VISIBLE
    }

    override fun showEmptyLayout() {
        ll_in_progress_no_package?.visibility = View.VISIBLE
        rv_in_progress_package?.visibility = View.GONE
    }

    override fun showError(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }


    override fun hideLoading() {
        pb_in_progress?.visibility = View.GONE
    }

    override fun showLoading() {
        pb_in_progress?.visibility = View.VISIBLE
    }

    override fun onStop() {
        mInProgressPresenter.detachView()
        super.onStop()
    }


}