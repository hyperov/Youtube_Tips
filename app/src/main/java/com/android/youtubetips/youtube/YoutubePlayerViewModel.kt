package com.android.youtubetips.youtube

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class YoutubePlayerViewModel : ViewModel() {

    val videoId = MutableLiveData<String>()
    val videoTitle = MutableLiveData<String>()

}