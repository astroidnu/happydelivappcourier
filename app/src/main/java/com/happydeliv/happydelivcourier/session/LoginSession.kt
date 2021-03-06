package com.happydeliv.happydelivcourier.session

import android.content.Context
import android.text.TextUtils
import com.happydeliv.happydelivcourier.HappyDelivCourier

/**
 * Created by ibnumuzzakkir on 11/10/17.
 * Android Engineer
 * SCO Project
 */
class LoginSession {
    var isLogin: Boolean = false

    private val PREF_TOKEN = "token"
    private val PREF_EMAIL = "email"
    private val PREF_NAME = "name"
    private val PREF_PHONE = "phone"
    private val PREF_TOKEN_ID = "token_id"
    private val PREF_COMPANY_NAME = "company_name"

    var pref = Pref()

    inner class Pref internal constructor() : BaseSharedPreferences() {
        init {
            check()
        }

        override fun _getApplicationContext(): Context {
            return HappyDelivCourier.instance
        }

        override fun _getUserProfileName(): String {
            return "user_session"
        }
    }


    fun getLoginToken(): String {
        return pref._getString(PREF_TOKEN, "")!!
    }

    fun saveToken(token: String) {
        pref._setString(PREF_TOKEN, token)
        isLogin = true
    }

    fun getLoginTokenId(): String {
        return pref._getString(PREF_TOKEN_ID, "")!!
    }

    fun saveTokenId(tokenid: String) {
        pref._setString(PREF_TOKEN_ID, tokenid)
        isLogin = true
    }

    fun getEmail(): String {
        return pref._getString(PREF_EMAIL, "")!!
    }

    fun saveEmail(email: String) {
        pref._setString(PREF_EMAIL, email)
    }

    fun getName(): String {
        return pref._getString(PREF_NAME, "")!!
    }

    fun saveName(name: String) {
        pref._setString(PREF_NAME, name)
    }

    fun getCompanyName(): String {
        return pref._getString(PREF_COMPANY_NAME, "")!!
    }

    fun saveCompanyName(companyName: String) {
        pref._setString(PREF_COMPANY_NAME, companyName)
    }

    fun getPhoneNumber(): String {
        return pref._getString(PREF_PHONE, "")!!
    }

    fun savePhoneNumber(phone: String) {
        pref._setString(PREF_PHONE, phone)
    }

    fun isInitialized(): Boolean {
        return !TextUtils.isEmpty(getLoginToken())
    }

    fun clear() {
        //clear the login session
        pref._clear()
        isLogin = false
    }
}