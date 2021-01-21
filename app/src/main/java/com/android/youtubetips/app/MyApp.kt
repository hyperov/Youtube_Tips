package com.android.youtubetips.app

import android.app.Application
import androidx.preference.PreferenceManager
import com.android.youtubetips.BuildConfig
import com.android.youtubetips.home.COUNTER_FOR_REVIEW
import com.android.youtubetips.home.Prefs
import com.android.youtubetips.home.putAny
import com.android.youtubetips.home.registerNetworkConnectionEvents
import com.google.android.gms.ads.MobileAds
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import com.startapp.sdk.adsbase.StartAppSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        StartAppSDK.setTestAdsEnabled(true)
        if (!BuildConfig.DEBUG) {
            Firebase.crashlytics.setCrashlyticsCollectionEnabled(true)
        }
        Prefs = PreferenceManager.getDefaultSharedPreferences(this)
        Prefs.putAny(COUNTER_FOR_REVIEW, Prefs.getInt(COUNTER_FOR_REVIEW, 0))
        registerNetworkConnectionEvents(this)

    }
}