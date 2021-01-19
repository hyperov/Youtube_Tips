package com.android.youtubetips.youtube

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.android.youtubetips.R
import com.android.youtubetips.home.COUNTER_FOR_REVIEW
import com.android.youtubetips.home.Prefs
import com.android.youtubetips.home.putAny
import com.google.android.material.appbar.MaterialToolbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_youtube_player.*

@AndroidEntryPoint
class YoutubePlayerFragment : Fragment() {

    private val youtubePlayerViewModel: YoutubePlayerViewModel by activityViewModels()
    private lateinit var navOptions: NavOptions

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_youtube_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseCrashlytics.getInstance().setCustomKey("SCREEN", "YoutubePlayerFragment")
        lifecycle.addObserver(youTubePlayerView)
        addListeners()
        setupTitle()
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
        })
    }

    private fun setupTitle() {
        requireActivity().findViewById<MaterialToolbar>(R.id.toolbar).title =
            youtubePlayerViewModel.videoTitle.value
    }
}