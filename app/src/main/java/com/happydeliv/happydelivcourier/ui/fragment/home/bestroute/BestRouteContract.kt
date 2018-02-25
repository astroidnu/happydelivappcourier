package com.happydeliv.happydelivcourier.ui.fragment.home.bestroute

import com.happydeliv.happydelivcourier.vo.BestRouteVo
import com.scoproject.weatherapp.ui.base.BaseView

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
class BestRouteContract{
    interface View : BaseView{
        fun showLoading()
        fun hideLoading()
        fun showError(msg :String)
        fun addMarker(lati :Double, longi :Double, marker : Int,titleMarker : String, sequence : String, isInitializeState : Boolean)
        fun drawDirection(currentLat: Double?, currentLong: Double?, destinationLat: Double?, destinationLong: Double?, color :Int)
        fun tampungNilai(data : List<BestRouteVo>)
    }
    interface UserActionListener {
        fun getBestRouteData(currLat : String, currLong : String)
    }
}