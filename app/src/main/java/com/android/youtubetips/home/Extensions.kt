package com.android.youtubetips.home

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import androidx.fragment.app.Fragment
import com.android.youtubetips.R
import java.util.*

lateinit var Prefs: SharedPreferences
const val IS_CONNECTED = "IS_CONNECTED"
const val COUNTER_FOR_REVIEW = "COUNTER_FOR_REVIEW"
const val COUNTER_FOR_INTERSTITAL_AD = "COUNTER_FOR_INTERSTITAL_AD"
const val MAX_COUNT_REVIEW_DIALOG_SHOW = 10


fun Fragment.getCategoryVideoIds(position: Int, isArabic: Boolean): Array<String> =
    when (position) {

        0 -> {
            if (isArabic) resources.getStringArray(R.array.ideas_arabic)
            else resources.getStringArray(R.array.ideas_english)
        }

        1 -> {
            if (isArabic) resources.getStringArray(R.array.niche_arabic)
            else resources.getStringArray(R.array.niche_english)
        }

        2 -> {
            if (isArabic) resources.getStringArray(R.array.create_channel_arabic)
            else resources.getStringArray(R.array.create_channel_english)
        }

        3 -> {
            if (isArabic) resources.getStringArray(R.array.beginners_arabic)
            else resources.getStringArray(R.array.beginners_english)
        }

        4 -> {
            if (isArabic) resources.getStringArray(R.array.record_arabic)
            else resources.getStringArray(R.array.record_english)
        }

        5 -> {
            if (isArabic) resources.getStringArray(R.array.sound_arabic)
            else resources.getStringArray(R.array.sound_english)
        }

        6 -> {
            if (isArabic) resources.getStringArray(R.array.light_arabic)
            else resources.getStringArray(R.array.light_english)
        }

        7 -> {
            if (isArabic) resources.getStringArray(R.array.editing_arabic)
            else resources.getStringArray(R.array.editing_english)
        }

        8 -> {
            if (isArabic) resources.getStringArray(R.array.mistakes_arabic)
            else resources.getStringArray(R.array.mistakes_english)
        }

        9 -> {
            if (isArabic) resources.getStringArray(R.array.thumbnail_arabic)
            else resources.getStringArray(R.array.thumbnail_english)
        }

        10 -> {
            if (isArabic) resources.getStringArray(R.array.analytics_arabic)
            else resources.getStringArray(R.array.analytics_english)
        }

        11 -> {
            if (isArabic) resources.getStringArray(R.array.monetization_arabic)
            else resources.getStringArray(R.array.monetization_english)
        }

        12 -> {
            if (isArabic) resources.getStringArray(R.array.keywords_arabic)
            else resources.getStringArray(R.array.keywords_english)
        }

        13 -> {
            if (isArabic) resources.getStringArray(R.array.subscribers_arabic)
            else resources.getStringArray(R.array.subscribers_english)
        }

        14 -> {
            if (isArabic) resources.getStringArray(R.array.views_arabic)
            else resources.getStringArray(R.array.views_english)
        }

        15 -> {
            if (isArabic) resources.getStringArray(R.array.grow_arabic)
            else resources.getStringArray(R.array.grow_english)
        }
        else -> {
            if (isArabic) resources.getStringArray(R.array.grow_arabic)
            else resources.getStringArray(R.array.grow_english)
        }

    }


fun registerNetworkConnectionEvents(context: Application) {
    val cm: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val builder: NetworkRequest.Builder = NetworkRequest.Builder()

    cm.registerNetworkCallback(
        builder.build(),
        object : ConnectivityManager.NetworkCallback() {

            override fun onAvailable(network: Network) {
                Prefs.putAny(IS_CONNECTED, true)
            }

            override fun onLost(network: Network) {
                Prefs.putAny(IS_CONNECTED, false)
            }
        })
}


// add entry in shared preference
fun SharedPreferences.putAny(name: String, any: Any) {
    when (any) {
        is String -> edit().putString(name, any).apply()
        is Int -> edit().putInt(name, any).apply()
        is Boolean -> edit().putBoolean(name, any).apply()

    }
}
    fun isEuUser(countryDetectorExtensions: CountryDetectorExtensions): Boolean {
        var country = countryDetectorExtensions.getCountryIsoCode()

        country = country ?: Locale.getDefault().country
        val euCountries = arrayListOf(
            "BE", "EL", "LT", "PT", "BG", "ES", "LU", "RO", "CZ", "FR", "HU", "SI", "DK", "HR",
            "MT", "SK", "DE", "IT", "NL", "FI", "EE", "CY", "AT", "SE", "IE", "LV", "PL", "UK",
            "CH", "NO", "IS", "LI"
        )
        return euCountries.contains(country?.toUpperCase())
    }




