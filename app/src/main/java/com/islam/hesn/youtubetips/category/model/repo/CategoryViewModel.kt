package com.islam.hesn.youtubetips.category.model.repo

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class CategoryViewModel @ViewModelInject constructor(private val repo: YoutubeRepo) : ViewModel() {

    val loading = MutableLiveData<Boolean>()
    val error = MutableLiveData(false)
    val success = MutableLiveData(false)

    fun getCategory(idList: List<String>) {

        loading.value = true
        error.value = false
        success.value = false

        viewModelScope.launch {
            try {
                val categoryRes = repo.getYoutubeCategoryVideosById(idList.toString())
                categoryRes.items
                success.value = true
            } catch (e: Exception) {
                error.value = true
                success.value = false
            } finally {
                loading.value = false

            }

        }
    }

}