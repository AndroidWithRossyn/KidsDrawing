package com.ninetynineapps.kidsdrawing.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import com.facebook.ads.*
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.ninetynineapps.kidsdrawing.common.CommonConstants
import com.ninetynineapps.kidsdrawing.interfaces.AdsCallback

object CommonConstantAd {
    private val adRequest: AdRequest
        private get() = AdRequest.Builder().build()
    var googleinterstitial: InterstitialAd? = null
    fun googlebeforloadAd(context: Context?) {
        try {
            InterstitialAd.load(context!!,
                Utils.getPref(context, CommonConstants.GOOGLE_INTERSTITIAL, "")!!,
                adRequest,
                object : InterstitialAdLoadCallback() {
                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        super.onAdLoaded(interstitialAd)
                        Log.e("TAG", "onAdLoaded::::INTERS " )
                        googleinterstitial = interstitialAd
                    }

                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        super.onAdFailedToLoad(loadAdError)
                        Log.e("TAG", "onAdFailedToLoad::::INTERS $loadAdError" )
                        googleinterstitial = null
                    }
                })
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showInterstitialAdsGoogle(activity: Activity?, adsCallback: AdsCallback) {
        try {
            if (googleinterstitial != null) {
                googleinterstitial!!.fullScreenContentCallback = object :
                    FullScreenContentCallback() {
                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        super.onAdFailedToShowFullScreenContent(adError)
                        googleinterstitial = null
                        adsCallback.startNextScreen()
                    }

                    override fun onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent()
                        adsCallback.onLoaded()
                    }

                    override fun onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent()
                        googleinterstitial = null
                        adsCallback.startNextScreen()
                    }

                    override fun onAdImpression() {
                        super.onAdImpression()
                    }
                }
                googleinterstitial!!.show(activity!!)
            } else {
                adsCallback.startNextScreen()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var facebookinterstitialAd: com.facebook.ads.InterstitialAd? = null
    private var facebookAdscallback1: AdsCallback? = null
    fun facebookbeforeloadFullAd(context: Context?,facebookAdscallback2: AdsCallback) {
        try {
            facebookinterstitialAd = com.facebook.ads.InterstitialAd(
                context,
                Utils.getPref(context, CommonConstants.FB_INTERSTITIAL, "")
            )
            facebookAdscallback1 = facebookAdscallback2
            val interstitialAdListener: InterstitialAdListener = object : InterstitialAdListener {
                override fun onInterstitialDisplayed(ad: Ad) {
                    Log.e("TAG", "Interstitial ad displayed.")
                }

                override fun onInterstitialDismissed(ad: Ad) {
                    Log.e("TAG", "Interstitial ad dismissed.")
                    try {
                        facebookAdscallback1!!.adClose()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                override fun onError(ad: Ad, adError: com.facebook.ads.AdError) {
                    Log.e(
                        "TAG",
                        "onError:Facebook :::::::::  " + adError.errorMessage + "  " + adError.errorCode
                    )
                }

                override fun onAdLoaded(ad: Ad) {
                    Log.e("TAG", "Interstitial ad is loaded and ready to be displayed!")
                }

                override fun onAdClicked(ad: Ad) {
                    Log.e("TAG", "Interstitial ad clicked!")
                }

                override fun onLoggingImpression(ad: Ad) {
                    Log.e("TAG", "Interstitial ad impression logged!")
                }
            }
            facebookinterstitialAd!!.loadAd(
                facebookinterstitialAd!!.buildLoadAdConfig().withAdListener(interstitialAdListener)
                    .build()
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showInterstitialAdsFacebook(facebookAdscallback: AdsCallback) {
        facebookAdscallback1 = facebookAdscallback
        if (facebookinterstitialAd != null) {
            if (facebookinterstitialAd!!.isAdLoaded) {
                facebookinterstitialAd!!.show()
                facebookAdscallback.onLoaded()
            } else {
                facebookAdscallback.startNextScreen()
            }
        } else {
            facebookAdscallback.startNextScreen()
        }
    }

    fun loadFacebookBannerAd(context: Context?, banner_container: LinearLayout) {
        val adView = AdView(
            context,
            Utils.getPref(context, CommonConstants.FB_BANNER, ""),
            AdSize.BANNER_HEIGHT_50
        )
        banner_container.addView(adView)
        val adListener: AdListener = object : AdListener {
            override fun onError(ad: Ad, adError: com.facebook.ads.AdError) {
                Log.e(
                    "TAG",
                    "onError:Fb:::: \${adError.errorCode}   \${adError.errorMessage}   " + adError.errorMessage + "  " + adError.errorCode
                )
                banner_container.setVisibility(View.GONE)
            }

            override fun onAdLoaded(ad: Ad) {
                Log.e("TAG", "onAdLoaded:::::: ")
                banner_container.setVisibility(View.VISIBLE)
            }

            override fun onAdClicked(ad: Ad) {}
            override fun onLoggingImpression(ad: Ad) {}
        }
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build())
    }

    fun loadBannerGoogleAd(context: Context?, llAdview: RelativeLayout) {
        val adViewBottom = com.google.android.gms.ads.AdView(context!!)
        adViewBottom.setAdSize(com.google.android.gms.ads.AdSize.BANNER)
        adViewBottom.adUnitId = Utils.getPref(context, CommonConstants.GOOGLE_BANNER, "")!!
        llAdview.addView(adViewBottom)
        val adRequest = AdRequest.Builder().build()
        adViewBottom.loadAd(adRequest)
        adViewBottom.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                adViewBottom.visibility = View.VISIBLE
                llAdview.setVisibility(View.VISIBLE)
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                super.onAdFailedToLoad(loadAdError)
                llAdview.setVisibility(View.VISIBLE)
            }
        }
    }
}