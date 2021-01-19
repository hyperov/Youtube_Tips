package com.android.youtubetips.category.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.android.youtubetips.R
import com.android.youtubetips.category.viewmodel.CategoryViewModel
import com.android.youtubetips.home.*
import com.android.youtubetips.youtube.YoutubePlayerViewModel
import com.google.android.gms.ads.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_category.*

@AndroidEntryPoint
class EnglishCategoryFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val categoryViewModel: CategoryViewModel by viewModels()
    private val youtubePlayerViewModel: YoutubePlayerViewModel by activityViewModels()

    private lateinit var adView: AdView

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
        setupBannerAds()
        setupAdsListeners()
        Prefs.putAny(COUNTER_FOR_REVIEW, Prefs.getInt(COUNTER_FOR_REVIEW, 0) + 1)
    }

    private fun setupTitle() {
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title =
            getString(R.string.english_channels)
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
            val adapter = CategoryRecyclerViewAdapter(videos, false) { videoItem ->
                youtubePlayerViewModel.videoId.value = videoItem.id
                youtubePlayerViewModel.videoTitle.value = videoItem.snippet.title
                findNavController().navigate(R.id.youtubePlayerFragment)
            }
            rvCategory.adapter = adapter
        })
    }

    private fun setupAdsListeners() {
        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                FirebaseCrashlytics.getInstance().setCustomKey("BANNER_AD_LOADED", true)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                FirebaseCrashlytics.getInstance().setCustomKey("BANNER_AD_LOADED", false)
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        }
    }

    private fun setupBannerAds() {
        adView = AdView(requireActivity())
        adView.adSize = AdSize.BANNER
        adView.adUnitId = getString(R.string.banner_ad_unit_id)
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

}