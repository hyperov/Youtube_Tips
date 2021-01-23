package com.nabil.youtubetips.home

import android.os.Bundle
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedAdsViewModel @ViewModelInject constructor() : ViewModel() {

    val isPersonalizedAds = MutableLiveData<Boolean>(true)

    //use for consent ads
    val extrasPersonalAdsBundle = MutableLiveData(Bundle(1))
}