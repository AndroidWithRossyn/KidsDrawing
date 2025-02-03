package com.ninetynineapps.kidsdrawing.activities

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ninetynineapps.kidsdrawing.R
import com.ninetynineapps.kidsdrawing.adapters.BGImageAdapter
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.common.CommonUtilities
import com.ninetynineapps.kidsdrawing.custom.CustomProgressDialog
import com.ninetynineapps.kidsdrawing.databinding.ActivityBgImageListBinding
import com.ninetynineapps.kidsdrawing.interfaces.AdapterItemCallback
import com.ninetynineapps.kidsdrawing.interfaces.AdsCallback
import com.ninetynineapps.kidsdrawing.pojo.BGImageClass
import com.ninetynineapps.kidsdrawing.utils.CommonConstantAd
import com.ninetynineapps.kidsdrawing.utils.Utils
import java.util.*

class BGImageListActivity : AppCompatActivity(), AdapterItemCallback, View.OnClickListener,
    AdsCallback {


    private var levelClassArrayList = ArrayList<BGImageClass>()
    private var isEasy = true

    private lateinit var binding: ActivityBgImageListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBgImageListBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_bg_image_list)
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        initViews()
        loadDataFromIntent()

        Utils.loadBannerAd(binding.llAdView,binding.llAdViewFacebook,this)
    }

    override fun onResume() {
        super.onResume()
        if (Utils.getPref(this, CommonConstants.AD_TYPE_FB_GOOGLE, "").equals(CommonConstants.AD_GOOGLE)) {
            CommonConstantAd.googlebeforloadAd(this)
        } else if (Utils.getPref(this, CommonConstants.AD_TYPE_FB_GOOGLE, "").equals(CommonConstants.AD_FACEBOOK)) {
            CommonConstantAd.facebookbeforeloadFullAd(this,this)
        }
    }


    private fun initViews() {
        levelClassArrayList = ArrayList()
        binding.rvBGImageList.layoutManager = GridLayoutManager(
            this,
            2,
            RecyclerView.VERTICAL,
            false
        )
        binding.ivBack.setOnClickListener(this)
    }

    private fun loadDataFromIntent() {
        val intent = intent
        if (intent != null && intent.hasExtra(CommonConstants.KeyBGImageType)) {
            isEasy = intent.getBooleanExtra(CommonConstants.KeyBGImageType, true)
            if (isEasy) {
                binding.tvTitle.text = "Easy"
            } else {
                binding.tvTitle.text = "Hard"
            }
            LoadImageListAsync(this).execute()
        }
    }

    override fun onClick(v: View?) {
        val id = v!!.id
        if (id == R.id.ivBack) {
            finish()
        }
    }

    @SuppressLint("StaticFieldLeak")
    inner class LoadImageListAsync(private val context: Context) : AsyncTask<Void, Void, Void>() {

        lateinit var pDialog: ProgressDialog

        override fun onPreExecute() {
            super.onPreExecute()
            pDialog = CustomProgressDialog.showProgress(context)
        }

        override fun doInBackground(vararg params: Void?): Void? {
            if (isEasy) {
                levelClassArrayList = CommonUtilities.getSmallEasyBGImageList(context)
            } else {
                levelClassArrayList = CommonUtilities.getSmallHardBGImageList(context)
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            CustomProgressDialog.hideProgress(pDialog)
            binding.rvBGImageList.adapter = BGImageAdapter(context, levelClassArrayList, this@BGImageListActivity)
        }
    }

    var position = 0
    override fun onAdapterItemClick(mPos: Int) {

        position = mPos
        if (Utils.getPref(this, CommonConstants.CLICK_IMAGE_COUNT, 1) == 1) {
            Utils.setPref(this, CommonConstants.CLICK_IMAGE_COUNT, 2)
            startEditorActivity()
        }else{
            checkAd()
        }

    }

    private fun checkAd() {

        if (Utils.getPref(this, CommonConstants.STATUS_ENABLE_DISABLE, "").equals(CommonConstants.ENABLE)) {
//            Handler().postDelayed({
                if (Utils.getPref(this, CommonConstants.AD_TYPE_FB_GOOGLE, "").equals(CommonConstants.AD_GOOGLE)) {
                    CommonConstantAd.showInterstitialAdsGoogle(this, this)
                } else if (Utils.getPref(this, CommonConstants.AD_TYPE_FB_GOOGLE, "").equals(CommonConstants.AD_FACEBOOK)) {
                    CommonConstantAd.showInterstitialAdsFacebook(this)
                } else {
                    startEditorActivity()
                }
//            }, 3000)
            Utils.setPref(this, CommonConstants.CLICK_IMAGE_COUNT, 1)
        } else {
            startEditorActivity()
        }
    }

    private fun startEditorActivity(){
        CommonConstants.IS_CLEAR = true
        CommonConstants.IS_CLEAR_BRUSH = true
        CommonConstants.IS_CLEAR_MAGIC = true
        CommonConstants.IS_CLEAR_PENCIL = true
        CommonConstants.IS_CLEAR_STICKER = true
        CommonConstants.IS_CLEAR_TEXT = true
        CommonConstants.STICKER_COUNT = 0


        val intent = Intent(this, EditorActivity::class.java)
        intent.putExtra(CommonConstants.KeyBGImageType, isEasy)
        intent.putExtra(CommonConstants.KeyItemPos, position)
        startActivity(intent)
    }

    override fun adLoadingFailed() {
        startEditorActivity()
    }

    override fun adClose() {
        startEditorActivity()
    }

    override fun startNextScreen() {
        startEditorActivity()
    }

    override fun onLoaded() {

    }


}