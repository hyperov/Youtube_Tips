package com.islam.hesn.youtubetips.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.islam.hesn.youtubetips.categories.model.CategoryItem

class SharedViewModel @ViewModelInject constructor() : ViewModel() {

    val selectedCategory = MutableLiveData<CategoryItem>()
}