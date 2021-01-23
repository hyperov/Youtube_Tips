package com.nabil.youtubetips.category.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.nabil.youtubetips.R
import com.nabil.youtubetips.category.viewmodel.CategoryViewModel
import com.nabil.youtubetips.home.*
import com.nabil.youtubetips.youtube.YoutubePlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.paid.fragment_category.*

@AndroidEntryPoint
class ArabicCategoryFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val youtubePlayerViewModel: YoutubePlayerViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
        setup()
        setupTitle()
        Prefs.putAny(COUNTER_FOR_REVIEW, Prefs.getInt(COUNTER_FOR_REVIEW, 0) + 1)
    }

    private fun setupTitle() {
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title =
            getString(R.string.arabic)
    }

    private fun setup() {
        val categoryVideoIds = getCategoryVideoIds(
            sharedViewModel.selectedCategory.value!!.categoryId,
            sharedViewModel.selectedTabPosition.value == 1
        )
        categoryViewModel.getCategory(categoryVideoIds)

    }

    private fun observeData() {
        categoryViewModel.videos.observe(viewLifecycleOwner, Observer { videos ->
            val adapter = CategoryRecyclerViewAdapter(videos, true) { videoItem ->
                youtubePlayerViewModel.videoId.value = videoItem.id
                youtubePlayerViewModel.videoTitle.value = videoItem.snippet.title
                findNavController().navigate(R.id.youtubePlayerFragment)
            }
            rvCategory.adapter = adapter
        })
    }

}