package com.nabil.youtubetips.youtube

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class YoutubePlayerViewModel @Inject constructor(): ViewModel() {

    val videoId = MutableLiveData<String>()
    val videoTitle = MutableLiveData<String>()

}