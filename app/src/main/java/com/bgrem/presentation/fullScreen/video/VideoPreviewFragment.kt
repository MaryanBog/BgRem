package com.bgrem.presentation.fullScreen.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bgrem.app.R
import com.bgrem.app.databinding.FragmentVideoPreviewBinding
import com.bgrem.presentation.common.extensions.getExtra
import com.bgrem.presentation.common.extensions.getParentAsListener
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.util.EventLogger
import kotlinx.coroutines.delay

class VideoPreviewFragment : Fragment(R.layout.fragment_video_preview) {

    private var _binding: FragmentVideoPreviewBinding? = null
    private val binding get() = _binding!!

    private val videoUriString by lazy { getExtra<String>(KEY_URI) }

    private val previewPlayer by lazy {
        ExoPlayer.Builder(requireContext()).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_OFF
            playWhenReady = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVideoPreviewBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            initView()
        }
        previewPlayer.addAnalyticsListener(EventLogger())
    }

    override fun onPause() {
        super.onPause()
        previewPlayer.pause()
    }

    private suspend fun initView() = with(binding) {
//        delay(1000)
        previewPlayerView.player = previewPlayer

        videoUriString?.let {
            previewPlayer.setMediaItem(MediaItem.fromUri(videoUriString!!), PLAYER_FIRST_FRAME_POSITION)
            previewPlayer.prepare()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        previewPlayer.stop()
    }

    companion object {
        val TAG: String = VideoPreviewFragment::class.java.simpleName

        private const val KEY_URI = "KEY_URI"
        private const val PLAYER_FIRST_FRAME_POSITION = 1L

        fun newInstance(uri: String?) = VideoPreviewFragment().apply {
            arguments = bundleOf(KEY_URI to uri)
        }
    }
}
