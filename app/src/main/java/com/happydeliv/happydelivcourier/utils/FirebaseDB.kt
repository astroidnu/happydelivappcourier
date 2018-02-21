package com.happydeliv.happydelivcourier.utils

import android.content.Context
import com.google.firebase.database.*
import com.happydeliv.happydelivcourier.vo.ProgressPackageVo

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
class FirebaseDB(context : Context): ValueEventListener{
    companion object {
        /**
         * List of Firebase DB Tables name
         * */
        val TABLE_PACKAGE_IN_PROGRESS = "package_in_progress"

    }

    private var mDatabaseReference  : DatabaseReference? = null
    internal val mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    lateinit var mCallBack : GetFireBaseCallBack

    /**
     * Sending data to firebase
     * */

    fun setInProgressPackageData(progressPackageVo: ProgressPackageVo){
        mDatabaseReference = mFirebaseDatabase
                .getReference(TABLE_PACKAGE_IN_PROGRESS)
        mDatabaseReference!!.run {
            keepSynced(true)
            child(progressPackageVo.trackId).setValue(progressPackageVo)
        }
    }

    fun onDisconnect(){
        mDatabaseReference?.onDisconnect()
    }


    /**
     * getting data to firebase
     * */

    fun gettingTrackingData(trackId :String, callback: GetFireBaseCallBack?) {
        if (callback != null) {
            val mDatabaseReference = mFirebaseDatabase
                    .getReference(TABLE_PACKAGE_IN_PROGRESS)
            val  mQuery = mDatabaseReference
                    .orderByChild("trackId")
                    .equalTo(trackId)
            mCallBack = callback
            mDatabaseReference.keepSynced(true)
            mQuery?.addValueEventListener(this)
        }
    }


    /**
     * Delete tracking data from firebase DB
     * by tracking id
     * */

    fun removeTrackingData(trackId :String) {
        val mDatabaseReference = mFirebaseDatabase
                .getReference(TABLE_PACKAGE_IN_PROGRESS)
        mDatabaseReference
                .child(trackId)
                .removeValue()
    }

    override fun onCancelled(databaseError: DatabaseError?) {
        databaseError?.let {
            mCallBack.onError(it)
        }
    }

    override fun onDataChange(dataSnapshot: DataSnapshot?) {
        dataSnapshot?.let {
            mCallBack.onSuccess(it)
        }
    }

    /**
     * Interface for callback data from search
     */

    interface GetFireBaseCallBack {
        fun onSuccess(dataSnapshot: DataSnapshot)
        fun onError(databaseError: DatabaseError)
    }

}