package com.nabil.youtubetips.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nabil.youtubetips.categories.model.CategoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(): ViewModel() {

    val selectedCategory = MutableLiveData<CategoryItem>()
    val selectedTabPosition = MutableLiveData<Int>()
}