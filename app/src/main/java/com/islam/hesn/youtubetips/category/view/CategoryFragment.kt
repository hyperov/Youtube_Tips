package com.islam.hesn.youtubetips.category.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.islam.hesn.youtubetips.R
import com.islam.hesn.youtubetips.categories.model.CategoryItem
import com.islam.hesn.youtubetips.home.SharedViewModel


class CategoryFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_category_arabic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setup()
    }

    private fun setup() {
        val adapter = CategoryRecyclerViewAdapter(loadResources())
//        rvCategoryList.adapter = adapter
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