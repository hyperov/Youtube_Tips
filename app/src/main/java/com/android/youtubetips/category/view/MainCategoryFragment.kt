package com.android.youtubetips.category.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.android.youtubetips.R
import com.android.youtubetips.home.SharedViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.tabs.TabLayout
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_categories.*
import kotlinx.android.synthetic.main.fragment_category_main.*
import kotlinx.android.synthetic.main.fragment_category_main.adView

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    lateinit var navOptions: NavOptions

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_category_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBannerAds()
        setupAdsListeners()
        val navController = setupNavigation()
        tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                sharedViewModel.selectedTabPosition.value = tab!!.position

                when (tab.position) {
                    0 -> navController.navigate(R.id.englishCategoryFragment, null, navOptions)
                    1 -> navController.navigate(R.id.arabicCategoryFragment, null, navOptions)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }

    private fun setupNavigation(): NavController {
        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.nav_host_fragment2) as NavHostFragment
        val navController = navHostFragment.navController

        navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestination, false)
            .build()
        return navController
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