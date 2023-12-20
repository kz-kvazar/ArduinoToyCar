//package com.qazar.arduinotoycar
//
//import android.app.Activity
//import android.app.Application
//import android.content.Context
//import android.os.Bundle
//import android.util.Log
//import androidx.core.content.PackageManagerCompat.LOG_TAG
//import com.google.android.gms.ads.*
//import com.google.android.gms.ads.appopen.AppOpenAd
//
//private const val LOG_TAG = "AppOpenAdManager"
//private const val AD_UNIT_ID = "ca-app-pub-7270220917037921/1686331191"
//
//class MyApplication : Application(), Application.ActivityLifecycleCallbacks{
//    private lateinit var appOpenAdManager: AppOpenAdManager
//    private var currentActivity: Activity? = null
//
//    override fun onCreate() {
//        super.onCreate()
//        registerActivityLifecycleCallbacks(this)
//        MobileAds.initialize(this) {}
//        appOpenAdManager = AppOpenAdManager()
//        appOpenAdManager.loadAd(this)
//    }
//
//    interface OnShowAdCompleteListener {
//        fun onShowAdComplete()
//    }
//
//    private inner class AppOpenAdManager {
//
//        private var appOpenAd: AppOpenAd? = null
//        private var isLoadingAd = false
//        var isShowingAd = false
//
//        /** Request an ad. */
//        fun loadAd(context: Context) {
//            // Do not load ad if there is an unused ad or one is already loading.
//            if (isLoadingAd || isAdAvailable()) {
//                return
//            }
//
//            isLoadingAd = true
//            val request = AdRequest.Builder().build()
//            AppOpenAd.load(
//                context, AD_UNIT_ID, request,
//                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
//                object : AppOpenAd.AppOpenAdLoadCallback() {
//
//                    override fun onAdLoaded(ad: AppOpenAd) {
//                        // Called when an app open ad has loaded.
//                        //Log.d(LOG_TAG, "Ad was loaded.")
//                        appOpenAd = ad
//                        isLoadingAd = false
//                    }
//
//                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
//                        // Called when an app open ad has failed to load.
//                        //Log.d(LOG_TAG, loadAdError.message)
//                        isLoadingAd = false;
//                    }
//                })
//        }
//
//        /** Check if ad exists and can be shown. */
//        private fun isAdAvailable(): Boolean {
//            return appOpenAd != null
//        }
//        /** Shows the ad if one isn't already showing. */
//        fun showAdIfAvailable(
//            activity: Activity,
//            onShowAdCompleteListener: OnShowAdCompleteListener) {
//            // If the app open ad is already showing, do not show the ad again.
//            if (isShowingAd) {
//                //Log.d(LOG_TAG, "The app open ad is already showing.")
//                return
//            }
//
//            // If the app open ad is not available yet, invoke the callback then load the ad.
//            if (!isAdAvailable()) {
//                //Log.d(LOG_TAG, "The app open ad is not ready yet.")
//                onShowAdCompleteListener.onShowAdComplete()
//                loadAd(activity)
//                return
//            }
//
//            appOpenAd?.setFullScreenContentCallback(
//                object : FullScreenContentCallback() {
//
//                    override fun onAdDismissedFullScreenContent() {
//                        // Called when full screen content is dismissed.
//                        // Set the reference to null so isAdAvailable() returns false.
//                        //Log.d(LOG_TAG, "Ad dismissed fullscreen content.")
//                        appOpenAd = null
//                        isShowingAd = false
//
//                        onShowAdCompleteListener.onShowAdComplete()
//                        loadAd(activity)
//                    }
//
//                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
//                        // Called when fullscreen content failed to show.
//                        // Set the reference to null so isAdAvailable() returns false.
//                        //Log.d(LOG_TAG, adError.message)
//                        appOpenAd = null
//                        isShowingAd = false
//
//                        onShowAdCompleteListener.onShowAdComplete()
//                        loadAd(activity)
//                    }
//
//                    override fun onAdShowedFullScreenContent() {
//                        // Called when fullscreen content is shown.
//                        //Log.d(LOG_TAG, "Ad showed fullscreen content.")
//                    }
//                })
//            isShowingAd = true
//            appOpenAd?.show(activity)
//        }
//    }
//    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
//
//    override fun onActivityStarted(activity: Activity) {
//        // Updating the currentActivity only when an ad is not showing.
//        if (!appOpenAdManager.isShowingAd) {
//            currentActivity = activity
//        }
//    }
//
//    override fun onActivityResumed(activity: Activity) {}
//
//    override fun onActivityPaused(activity: Activity) {}
//
//    override fun onActivityStopped(activity: Activity) {}
//
//    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
//
//    override fun onActivityDestroyed(activity: Activity) {}
//}