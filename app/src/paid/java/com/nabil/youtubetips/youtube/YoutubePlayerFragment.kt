package com.nabil.youtubetips.youtube

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.nabil.youtubetips.R
import com.nabil.youtubetips.home.COUNTER_FOR_REVIEW
import com.nabil.youtubetips.home.Prefs
import com.nabil.youtubetips.home.putAny
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.paid.fragment_youtube_player.*


@AndroidEntryPoint
class YoutubePlayerFragment : Fragment() {

    private var mYouTubePlayer: YouTubePlayer? = null
    private val youtubePlayerViewModel: YoutubePlayerViewModel by activityViewModels()

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
        FirebaseCrashlytics.getInstance().setCustomKey("SCREEN", "YoutubePlayerFragment")
        FirebaseAnalytics.getInstance(requireContext()).logEvent("SCREEN", Bundle())
        lifecycle.addObserver(youTubePlayerView)
        addListeners()
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

        })
    }

    private fun setupTitle() {
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title =
            youtubePlayerViewModel.videoTitle.value
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