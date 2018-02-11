package com.happydeliv.happydelivcourier.ui.fragment.home.history

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.happydeliv.happydelivapp.ui.common.BaseFragment
import com.happydeliv.happydelivcourier.R
import com.happydeliv.happydelivcourier.adapter.setUp
import com.happydeliv.happydelivcourier.ui.activity.home.HomeActivity
import com.happydeliv.happydelivcourier.vo.PackageVo
import kotlinx.android.synthetic.main.fragment_history.*
import kotlinx.android.synthetic.main.item_package.view.*
import javax.inject.Inject

/**
 * Created by ibnumuzzakkir on 06/02/18.
 * Android Engineer
 * SCO Project
 */
class HistoryFragment : BaseFragment(), HistoryContract.View{
    @Inject
    lateinit var mHistoryPresenter : HistoryPresenter

    private val mRvHistory by lazy {
        rv_history
    }

    private val mLinearLayoutManager = LinearLayoutManager(context)

    override fun getLayoutId(): Int {
        return R.layout.fragment_history
    }

    override fun onLoadFragment(saveInstance: Bundle?) {
        (activity as HomeActivity).hideBtnAddPackage()
        mHistoryPresenter.attachView(this)
        mHistoryPresenter.getHistoryList()
    }

    override fun setupAdapter(data : List<PackageVo>) {
        mRvHistory?.setUp(data, R.layout.item_package,{
            tv_package_recipient_name.text = it.recipientName
            tv_package_resi_no.text ="Resi no : " +  it.resiNumber
            tv_package_status.text = "Sudah sampai pada : " + it.deliveredAt
            tv_package_track_id.text = "Track id : " + it.trackId
            Glide.with(this)
                    .load(it.recipientImage)
                    .into(iv_recipient_logo)

        },{},mLinearLayoutManager)
    }

    override fun hideEmptyLayout() {
        ll_history_no_package?.visibility = View.GONE
        mRvHistory?.visibility = View.VISIBLE
    }

    override fun showEmptyLayout() {
        ll_history_no_package?.visibility = View.VISIBLE
        mRvHistory?.visibility = View.GONE
    }

    override fun showError(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }

    override fun hideLoading() {
        pb_history?.visibility = View.GONE
    }

    override fun showLoading() {
        pb_history?.visibility = View.VISIBLE
    }

    override fun onStop() {
        mHistoryPresenter.detachView()
        super.onStop()
    }


}