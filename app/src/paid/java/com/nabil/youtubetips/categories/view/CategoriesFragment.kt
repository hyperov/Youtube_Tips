package com.nabil.youtubetips.categories.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nabil.youtubetips.R
import com.nabil.youtubetips.categories.model.CategoryItem
import com.nabil.youtubetips.home.COUNTER_FOR_REVIEW
import com.nabil.youtubetips.home.Prefs
import com.nabil.youtubetips.home.SharedViewModel
import com.nabil.youtubetips.home.putAny
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.paid.fragment_categories.*

@AndroidEntryPoint
class CategoriesFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_categories, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
        Prefs.putAny(COUNTER_FOR_REVIEW, Prefs.getInt(COUNTER_FOR_REVIEW, 0) + 1)
    }

    private fun setup() {
        val adapter =
            CategoryListRecyclerViewAdapter(loadResources()) { category: CategoryItem ->
                sharedViewModel.selectedCategory.value = category
                findNavController().navigate(R.id.mainCategoryFragment)
            }
        rvCategoryList.adapter = adapter
    }

    private fun loadResources(): List<CategoryItem> {

        val categories = arrayListOf<CategoryItem>()

        val categoryNames = resources.getStringArray(R.array.categoryList).toList()

        val tArray = resources.obtainTypedArray(
            R.array.categoryListImages
        )

        val count = tArray.length()
        val categoryImageIds = IntArray(count)

        for (i in categoryImageIds.indices) {
            categoryImageIds[i] = tArray.getResourceId(i, 0)
            categories.add(CategoryItem(categoryNames[i], categoryImageIds[i], i))
        }
        //Recycles the TypedArray, to be re-used by a later caller.
        //After calling this function you must not ever touch the typed array again.
        tArray.recycle()
        return categories
    }

}