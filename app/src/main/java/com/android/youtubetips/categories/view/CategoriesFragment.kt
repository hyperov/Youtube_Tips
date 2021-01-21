package com.android.youtubetips.categories.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.android.youtubetips.R
import com.android.youtubetips.categories.model.CategoryItem
import com.android.youtubetips.home.COUNTER_FOR_REVIEW
import com.android.youtubetips.home.Prefs
import com.android.youtubetips.home.SharedViewModel
import com.android.youtubetips.home.putAny
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_categories.*

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
        setupBannerAds()
        setupAdsListeners()
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
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

}