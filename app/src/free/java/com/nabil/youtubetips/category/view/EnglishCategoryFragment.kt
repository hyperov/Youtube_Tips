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
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.nabil.youtubetips.R
import com.nabil.youtubetips.category.viewmodel.CategoryViewModel
import com.nabil.youtubetips.home.*
import com.nabil.youtubetips.youtube.YoutubePlayerViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.free.fragment_category.*

@AndroidEntryPoint
class EnglishCategoryFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val sharedAdsViewModel: SharedAdsViewModel by activityViewModels()
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
        var builder = AdRequest.Builder()
        if (sharedAdsViewModel.isPersonalizedAds.value!!.not()) {
            builder = builder.addNetworkExtrasBundle(
                AdMobAdapter::class.java,
                sharedAdsViewModel.extrasPersonalAdsBundle.value
            )
        }
        val adRequest = builder.build()
        adView.loadAd(adRequest)
    }

}