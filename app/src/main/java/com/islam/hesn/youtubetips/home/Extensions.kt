package com.islam.hesn.youtubetips.home

import android.app.Activity
import com.islam.hesn.youtubetips.R

fun Activity.getCategoryVideoIds(position: Int, isArabic: Boolean): String = when (position) {

    0 -> {
        if (isArabic) resources.getStringArray(R.array.ideas_arabic).toString()
        else resources.getStringArray(R.array.ideas_english).toString()
    }

    1 -> {
        if (isArabic) resources.getStringArray(R.array.niche_arabic).toString()
        else resources.getStringArray(R.array.niche_english).toString()
    }

    2 -> {
        if (isArabic) resources.getStringArray(R.array.create_channel_arabic).toString()
        else resources.getStringArray(R.array.create_channel_english).toString()
    }

    3 -> {
        if (isArabic) resources.getStringArray(R.array.beginners_arabic).toString()
        else resources.getStringArray(R.array.beginners_english).toString()
    }

    4 -> {
        if (isArabic) resources.getStringArray(R.array.record_arabic).toString()
        else resources.getStringArray(R.array.record_english).toString()
    }

    5 -> {
        if (isArabic) resources.getStringArray(R.array.sound_arabic).toString()
        else resources.getStringArray(R.array.sound_english).toString()
    }

    6 -> {
        if (isArabic) resources.getStringArray(R.array.light_arabic).toString()
        else resources.getStringArray(R.array.light_english).toString()
    }

    7 -> {
        if (isArabic) resources.getStringArray(R.array.editing_arabic).toString()
        else resources.getStringArray(R.array.editing_english).toString()
    }

    8 -> {
        if (isArabic) resources.getStringArray(R.array.mistakes_arabic).toString()
        else resources.getStringArray(R.array.mistakes_english).toString()
    }

    9 -> {
        if (isArabic) resources.getStringArray(R.array.thumbnail_arabic).toString()
        else resources.getStringArray(R.array.thumbnail_english).toString()
    }

    10 -> {
        if (isArabic) resources.getStringArray(R.array.analytics_arabic).toString()
        else resources.getStringArray(R.array.analytics_english).toString()
    }

    11 -> {
        if (isArabic) resources.getStringArray(R.array.monetization_arabic).toString()
        else resources.getStringArray(R.array.monetization_english).toString()
    }

    12 -> {
        if (isArabic) resources.getStringArray(R.array.keywords_arabic).toString()
        else resources.getStringArray(R.array.keywords_english).toString()
    }

    13 -> {
        if (isArabic) resources.getStringArray(R.array.subscribers_arabic).toString()
        else resources.getStringArray(R.array.subscribers_english).toString()
    }

    14 -> {
        if (isArabic) resources.getStringArray(R.array.views_arabic).toString()
        else resources.getStringArray(R.array.views_english).toString()
    }

    15 -> {
        if (isArabic) resources.getStringArray(R.array.grow_arabic).toString()
        else resources.getStringArray(R.array.grow_english).toString()
    }
    else -> {
        if (isArabic) resources.getStringArray(R.array.grow_arabic).toString()
        else resources.getStringArray(R.array.grow_english).toString()
    }

}


