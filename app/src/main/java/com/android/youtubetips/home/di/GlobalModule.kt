package com.android.youtubetips.home.di

import android.content.Context
import android.telephony.TelephonyManager
import com.android.youtubetips.home.CountryDetectorExtensions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ApplicationComponent::class)
object GlobalModule {
    @Provides
    fun getTelephonyManagerInstance(@ApplicationContext context: Context): TelephonyManager {
        return context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    @Provides
    fun getCountryDetectorDetectionInstance(telephonyManager: TelephonyManager): CountryDetectorExtensions {
        return CountryDetectorExtensions(telephonyManager)
    }
}