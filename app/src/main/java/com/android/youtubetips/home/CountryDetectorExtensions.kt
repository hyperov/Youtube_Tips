package com.android.youtubetips.home

import android.telephony.TelephonyManager
import android.text.TextUtils
import java.util.*
import javax.inject.Inject

class CountryDetectorExtensions @Inject constructor(
    private var telephonyManager: TelephonyManager? //ISO country codes are always uppercase
) {

    /** Get the device's current country's 2 letter ISO code
     * @return
     */
    fun getCountryIsoCode(): String? {
        var result: String?
        result = networkBasedCountry
        if (result == null) {
            result = simBasedCountry
        }
        if (result == null) {
            result = localeCountry
        }
        if (result != null) result =
            result.toUpperCase() //ISO country codes are always uppercase
        return result
        // TODO: The document says the result may be unreliable on CDMA networks. Shall we use
        // it on CDMA phone? We may test the Android primarily used countries.
    }


    /**
     * @return the country from the mobile network.
     */
    val networkBasedCountry: String?
        get() {
            var countryIso: String? = null
            // TODO: The document says the result may be unreliable on CDMA networks. Shall we use
            // it on CDMA phone? We may test the Android primarily used countries.
            if (telephonyManager?.phoneType === TelephonyManager.PHONE_TYPE_GSM) {
                countryIso = telephonyManager!!.networkCountryIso
                if (!TextUtils.isEmpty(countryIso)) {
                    return countryIso
                }
            }
            return null
        }

    /**
     * @return the country from SIM card
     */
    val simBasedCountry: String?
        get() {
            var countryIso: String?
            countryIso = telephonyManager?.simCountryIso
            return if (!TextUtils.isEmpty(countryIso)) {
                countryIso
            } else null
        }

    /**
     * @return the country from the system's locale.
     */
    val localeCountry: String?
        get() {
            val defaultLocale: Locale? = Locale.getDefault()
            return defaultLocale?.country
        }
}