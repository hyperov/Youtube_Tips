package com.nabil.youtubetips.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nabil.youtubetips.categories.model.CategoryItem

class SharedViewModel @ViewModelInject constructor() : ViewModel() {

    val selectedCategory = MutableLiveData<CategoryItem>()
    val selectedTabPosition = MutableLiveData<Int>()
}