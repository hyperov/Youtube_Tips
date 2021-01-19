package com.android.youtubetips.category.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.youtubetips.category.model.repo.YoutubeRepo
import com.android.youtubetips.category.model.response.VideoItem
import kotlinx.coroutines.launch

class CategoryViewModel @ViewModelInject constructor(private val repo: YoutubeRepo) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData(false)
    val success = MutableLiveData(false)

    val videos = MutableLiveData<List<VideoItem>>()

    fun getCategory(idList: Array<String>) {

        loading.value = true
        error.value = false
        success.value = false

        viewModelScope.launch {
            try {
                val stringBuilder = StringBuilder()
                idList.forEach { videoId ->
                    stringBuilder.append(videoId).append(",")
                }

                val categoryRes = repo.getYoutubeCategoryVideosById(stringBuilder.toString())

                success.value = true
                videos.value = categoryRes.items

            } catch (e: Exception) {
                error.value = true
                success.value = false
            } finally {
                loading.value = false

            }

        }
    }

}