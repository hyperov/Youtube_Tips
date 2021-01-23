package com.nabil.youtubetips.youtube

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.LoadAdError
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.nabil.youtubetips.R
import com.nabil.youtubetips.home.*
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.free.fragment_youtube_player.*


@AndroidEntryPoint
class YoutubePlayerFragment : Fragment() {

    private var mYouTubePlayer: YouTubePlayer? = null
    private lateinit var mInterstitialAd: InterstitialAd

    private val youtubePlayerViewModel: YoutubePlayerViewModel by activityViewModels()
    private val sharedAdsViewModel: SharedAdsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_youtube_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupTitle()
        Prefs.putAny(COUNTER_FOR_INTERSTITAL_AD, 0)
        FirebaseCrashlytics.getInstance().setCustomKey("SCREEN", "YoutubePlayerFragment")
        FirebaseAnalytics.getInstance(requireContext()).logEvent("SCREEN", Bundle())
        lifecycle.addObserver(youTubePlayerView)
        addListeners()
        initializeAds()
        setupInterstitalAdsListeners()
        Prefs.putAny(COUNTER_FOR_REVIEW, Prefs.getInt(COUNTER_FOR_REVIEW, 0) + 1)
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            findNavController().navigateUp()
        }
    }


    private fun addListeners() {
        youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                super.onReady(youTubePlayer)
                youTubePlayer.loadVideo(youtubePlayerViewModel.videoId.value!!, 0f)
            }

            override fun onVideoDuration(youTubePlayer: YouTubePlayer, duration: Float) {
                mYouTubePlayer = youTubePlayer
                super.onVideoDuration(youTubePlayer, duration)

            }

            override fun onCurrentSecond(youTubePlayer: YouTubePlayer, second: Float) {
                super.onCurrentSecond(youTubePlayer, second)
                when {
                    second >= 300f -> {
                        if (Prefs.getInt(COUNTER_FOR_INTERSTITAL_AD, 0) < 3)
                            showAds()
                    }
                    second >= 120f -> {
                        if (Prefs.getInt(COUNTER_FOR_INTERSTITAL_AD, 0) < 2)
                            showAds()
                    }
                    second >= 15f -> {
                        if (Prefs.getInt(COUNTER_FOR_INTERSTITAL_AD, 0) < 1)
                            showAds()
                    }
                    second in 5f..7f -> {
                        if (Prefs.getInt(COUNTER_FOR_INTERSTITAL_AD, 0) < 1)
                            showAds()
                    }
                }
            }
        })
    }

    private fun setupTitle() {
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title =
            youtubePlayerViewModel.videoTitle.value
    }

    private fun setupInterstitalAdsListeners() {
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                FirebaseCrashlytics.getInstance().setCustomKey("INTERSTITAL_AD_LOADED", true)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                FirebaseCrashlytics.getInstance().setCustomKey("INTERSTITAL_AD_LOADED", false)
            }

            override fun onAdOpened() {
                // Code to be executed when the ad is displayed.
                FirebaseCrashlytics.getInstance().setCustomKey("INTERSTITAL_AD_OPENED", true)
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                FirebaseCrashlytics.getInstance().setCustomKey("INTERSTITAL_AD_CLICK", true)
            }

            override fun onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            override fun onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                mInterstitialAd.loadAd(AdRequest.Builder().build())
                FirebaseCrashlytics.getInstance().setCustomKey("INTERSTITAL_AD_CLOSED", true)
            }
        }
    }

    private fun initializeAds() {
        //interstital ads
        mInterstitialAd = InterstitialAd(requireActivity())
        mInterstitialAd.adUnitId = getString(R.string.interstital_ad_unit_id)

        var builder = AdRequest.Builder()
        if (sharedAdsViewModel.isPersonalizedAds.value!!.not()) {
            builder = builder.addNetworkExtrasBundle(
                AdMobAdapter::class.java,
                sharedAdsViewModel.extrasPersonalAdsBundle.value
            )
        }
        mInterstitialAd.loadAd(builder.build())
    }

    private fun showAds() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
            Prefs.putAny(
                COUNTER_FOR_INTERSTITAL_AD,
                Prefs.getInt(COUNTER_FOR_INTERSTITAL_AD, 0) + 1
            )
        } else {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Prefs.putAny(COUNTER_FOR_INTERSTITAL_AD, 0)
    }

    override fun onPause() {
        super.onPause()
        mYouTubePlayer?.pause()
    }

    override fun onResume() {
        super.onResume()
        mYouTubePlayer?.play()
    }

}