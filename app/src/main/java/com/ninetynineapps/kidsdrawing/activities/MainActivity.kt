package com.ninetynineapps.kidsdrawing.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import com.google.firebase.messaging.FirebaseMessaging
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.databinding.ActivityEditorBinding
import com.ninetynineapps.kidsdrawing.databinding.ActivityMainBinding
import com.ninetynineapps.kidsdrawing.interfaces.CallbackListener
import com.ninetynineapps.kidsdrawing.mywork.MyWorkActivity
import com.ninetynineapps.kidsdrawing.utils.CommonConstantAd
import com.ninetynineapps.kidsdrawing.utils.Utils


class MainActivity : AppCompatActivity(), View.OnClickListener, CallbackListener {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initViews()
        successCall()
        try {
            //subScribeToFirebaseTopic();
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }

        Utils.loadBannerAd(binding.llAdView,binding.llAdViewFacebook,this)
    }

    private fun subScribeToFirebaseTopic() {
        try {
            FirebaseMessaging.getInstance().subscribeToTopic("kids_drawing_topic")
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.e("subScribeFirebaseTopic", ": Fail")
                    } else {
                        Log.e("subScribeFirebaseTopic", ": Success")
                    }
                }
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun successCall() {
        if (Utils.isNetworkConnected(this)) {
            if (CommonConstants.ENABLE_DISABLE == CommonConstants.ENABLE) {
                Utils.setPref(
                    this,
                    CommonConstants.AD_TYPE_FB_GOOGLE,
                    CommonConstants.AD_TYPE_FACEBOOK_GOOGLE
                )
                Utils.setPref(
                    this,
                    CommonConstants.FB_BANNER,
                    CommonConstants.FB_BANNER_ID
                )
                Utils.setPref(
                    this,
                    CommonConstants.FB_INTERSTITIAL,
                    CommonConstants.FB_INTERSTITIAL_ID
                )
                Utils.setPref(
                    this,
                    CommonConstants.GOOGLE_BANNER,
                    CommonConstants.GOOGLE_BANNER_ID
                )
                Utils.setPref(
                    this,
                    CommonConstants.GOOGLE_INTERSTITIAL,
                    CommonConstants.GOOGLE_INTERSTITIAL_ID
                )
                Utils.setPref(
                    this,
                    CommonConstants.STATUS_ENABLE_DISABLE,
                    CommonConstants.ENABLE_DISABLE
                )
                setAppAdId(CommonConstants.GOOGLE_ADMOB_APP_ID)
            } else {
                Utils.setPref(
                    this,
                    CommonConstants.STATUS_ENABLE_DISABLE,
                    CommonConstants.ENABLE_DISABLE
                )
            }
        } else {
            Utils.openInternetDialog(this, this, true)
        }
    }


    fun setAppAdId(id: String?) {
        try {
            val applicationInfo =
                packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            val bundle = applicationInfo.metaData
            val beforeChangeId = bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
            Log.e("TAG", "setAppAdId:BeforeChange:::::  $beforeChangeId")
            applicationInfo.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", id)
            val AfterChangeId = bundle.getString("com.google.android.gms.ads.APPLICATION_ID")
            Log.e("TAG", "setAppAdId:AfterChange::::  $AfterChangeId")
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun initViews() {
        binding.ivLetsPlay.setOnClickListener(this)
        binding.ivMyCreation.setOnClickListener(this)
        binding.ivShare.setOnClickListener(this)
        binding.ivRate.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        if (id == R.id.ivLetsPlay) {
            showEasyHardDialog()
        } else if (id == R.id.ivMyCreation) {
            if (Utils.checkPermission(this)) {
                startActivity(Intent(this, MyWorkActivity::class.java))
            }
        } else if (id == R.id.ivShare) {
            shareApp()
        } else if (id == R.id.ivRate) {
            rateUs()
        }
    }

    private fun showEasyHardDialog() {
        val dialog = Dialog(this)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        val customView = LayoutInflater.from(this).inflate(R.layout.dialog_fragment_level, null)
        dialog.setContentView(customView)
        val ivClose = customView.findViewById(R.id.ivClose) as AppCompatImageView
        ivClose.setOnClickListener {
            dialog.dismiss()
        }
        val ivEasy = customView.findViewById(R.id.ivEasy) as AppCompatImageView
        ivEasy.setOnClickListener {
            dialog.dismiss()
            startBGImageListActivity(true)
        }
        val ivHard = customView.findViewById(R.id.ivHard) as AppCompatImageView
        ivHard.setOnClickListener {
            dialog.dismiss()
            startBGImageListActivity(false)
        }

        dialog.show()
    }

    private fun startBGImageListActivity(isEasy: Boolean) {
        val intent = Intent(this, BGImageListActivity::class.java)
        intent.putExtra(CommonConstants.KeyBGImageType, isEasy)
        startActivity(intent)
    }


    private fun rateUs() {
        val appPackageName = packageName
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store")))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
        }
    }


    private fun shareApp() {
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        val link = "https://play.google.com/store/apps/details?id=$packageName"
        shareIntent.putExtra(Intent.EXTRA_TEXT, link)
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, resources.getString(R.string.app_name))
        shareIntent.type = "text/plain"
        startActivity(Intent.createChooser(shareIntent, resources.getString(R.string.app_name)))
    }

    override fun onSuccess() {

    }

    override fun onCancel() {

    }

    override fun onRetry() {

    }
}