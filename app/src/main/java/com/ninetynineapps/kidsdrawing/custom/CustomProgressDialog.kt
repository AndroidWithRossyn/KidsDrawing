package com.ninetynineapps.kidsdrawing.custom

import android.app.ProgressDialog
import android.content.Context
import com.ninetynineapps.kidsdrawing.common.CommonConstants

object CustomProgressDialog {

    lateinit var pDialog: ProgressDialog

    public fun showProgress(context: Context): ProgressDialog {
        try {
            pDialog = ProgressDialog(context)
            pDialog.setMessage(CommonConstants.CapPleaseWait)
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            pDialog.setCancelable(false)
            pDialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pDialog
    }

    fun hideProgress(pDialog:ProgressDialog) {
        try {
            if (pDialog.isShowing) {
                pDialog.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}