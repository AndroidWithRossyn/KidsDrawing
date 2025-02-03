package com.ninetynineapps.kidsdrawing.interfaces

/**
 * Created by Naynesh Patel on 06-Apr-19.
 */
interface AdsCallback {
    fun adLoadingFailed()
    fun adClose()
    fun startNextScreen()
    fun onLoaded()
}