package com.ninetynineapps.kidsdrawing.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.interfaces.AdsCallback
import com.ninetynineapps.kidsdrawing.interfaces.CallbackListener
import com.ninetynineapps.kidsdrawing.utils.CommonConstantAd
import com.ninetynineapps.kidsdrawing.utils.Utils

class SplashActivity : AppCompatActivity(), CallbackListener, AdsCallback {


    private var isLoaded = false
    private val handler = Handler()
    private val myRunnable = Runnable {
        if (Utils.isNetworkConnected(this@SplashActivity)) {
            if (!isLoaded) {
                startNextActivity(0)
            }
        }
    }

    private fun startNextActivity(time: Long) {
        Handler().postDelayed({
            val mainIntent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(mainIntent)
            finish()
        }, time)

    }

    fun callApi() {
        if (Utils.isNetworkConnected(this)) {
            successCall()
        } else {
            Utils.openInternetDialog(this, this, true)
        }
        handler.postDelayed(myRunnable, 10000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        supportActionBar!!.hide()
        callApi()

    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {
        callApi()
    }


    private fun successCall() {
        if (Utils.getPref(this, CommonConstants.SPLASH_SCREEN_COUNT, 1) == 1) {
            Utils.setPref(this, CommonConstants.SPLASH_SCREEN_COUNT, 2)
            startNextActivity(1000)
        } else {
            checkAd()
        }
    }

    private fun checkAd() {
        if (Utils.getPref(this, CommonConstants.STATUS_ENABLE_DISABLE, "").equals(CommonConstants.ENABLE)) {
            if (Utils.getPref(this, CommonConstants.AD_TYPE_FB_GOOGLE, "").equals(CommonConstants.AD_GOOGLE)) {
                CommonConstantAd.googlebeforloadAd(this)
            } else if (Utils.getPref(this, CommonConstants.AD_TYPE_FB_GOOGLE, "").equals(CommonConstants.AD_FACEBOOK)) {
                CommonConstantAd.facebookbeforeloadFullAd(this,this)
            }
            if (Utils.getPref(this, CommonConstants.STATUS_ENABLE_DISABLE, "")
                    .equals(CommonConstants.ENABLE)
            ) {
                Handler().postDelayed({
                    if (Utils.getPref(
                            this@SplashActivity,
                            CommonConstants.AD_TYPE_FB_GOOGLE,
                            ""
                        ).equals(CommonConstants.AD_GOOGLE)
                    ) {
                        CommonConstantAd.showInterstitialAdsGoogle(
                            this@SplashActivity,
                            this@SplashActivity
                        )
                    } else if (Utils.getPref(
                            this@SplashActivity,
                            CommonConstants.AD_TYPE_FB_GOOGLE,
                            ""
                        ).equals(CommonConstants.AD_FACEBOOK)
                    ) {
                        CommonConstantAd.showInterstitialAdsFacebook(this@SplashActivity)
                    } else {
                        startNextActivity(0)
                    }
                }, 3000)
                Utils.setPref(this, CommonConstants.SPLASH_SCREEN_COUNT, 1)
            } else {
                startNextActivity(0)
            }
        } else {
            Utils.setPref(this, CommonConstants.SPLASH_SCREEN_COUNT, 1)
            startNextActivity(1000)
        }
    }

    override fun adLoadingFailed() {
        startNextActivity(0)
    }

    override fun adClose() {
        startNextActivity(0)
    }

    override fun startNextScreen() {
        startNextActivity(0)
    }

    override fun onLoaded() {
        isLoaded = true
    }


    override fun onStop() {
        super.onStop()
        handler.removeCallbacks(myRunnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(myRunnable)
    }
}