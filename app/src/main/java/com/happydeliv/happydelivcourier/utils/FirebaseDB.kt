package com.happydeliv.happydelivcourier.utils

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.happydeliv.happydelivcourier.vo.ProgressPackageVo

/**
 * Created by ibnumuzzakkir on 10/02/18.
 * Android Engineer
 * SCO Project
 */
class FirebaseDB(context : Context){
    companion object {
        /**
         * List of Firebase DB Tables name
         * */
        val TABLE_PACKAGE_IN_PROGRESS = "package_in_progress"

    }
    internal var mFirebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

    /**
     * Sending data to firebase
     * */

    fun setInProgressPackageData(progressPackageVo: ProgressPackageVo){
        val mDatabaseReference = mFirebaseDatabase
                .getReference(TABLE_PACKAGE_IN_PROGRESS)
        mDatabaseReference.keepSynced(true)
        mDatabaseReference.child(mDatabaseReference.push().key).setValue(progressPackageVo)
    }



    /**
     * Interface for callback data from search
     */

    interface GetFireBaseCallBack {
        fun onSuccess(dataSnapshot: DataSnapshot)
        fun onError(databaseError: DatabaseError)
    }

}