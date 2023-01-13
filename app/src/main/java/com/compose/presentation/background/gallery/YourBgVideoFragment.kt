package com.compose.presentation.background.gallery

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bgrem.presentation.trimming.TrimmingContract
import com.compose.presentation.background.BackgroundViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.YourBgVideoScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class YourBgVideoFragment : Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()
    private val backgroundViewModel: BackgroundViewModel by activityViewModels()
    private val yourBgViewModel: YourBgViewModel by viewModels()

    private val trimming = registerForActivityResult(TrimmingContract()) { uri ->
        uri ?: findNavController().popBackStack()
        handleSelectedVideo(uri)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BgRemTheme {
                    YourBgVideoScreen(
                        yourBgViewModel = yourBgViewModel
                    )
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            yourBgViewModel.yourBgAction.collect { action ->
                when (action) {
                    is YourBgAction.AddVideo -> {
                        onVideoSaved(action.uri)
                        backgroundViewModel.yourBgType = "video"
                        backgroundViewModel.videoUri = action.uri
                    }
                    else -> {}
                }
            }
        }
    }

    private fun onVideoSaved(uri: Uri) {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            delay(TIME_MILLIS)
            trimming.launch(uri)
        }
    }

    private fun handleSelectedVideo(uri: Uri?) = try {
        with(requireContext()) {
            val fileStream = uri?.let { contentResolver.openInputStream(it) }
            val mimeType = uri?.let { contentResolver.getType(it) }

            backgroundViewModel.yourBgType = "video"
            removeViewModel.getMyBgVideoInfo(mimeType, fileStream)
            findNavController().popBackStack()
        }
    } catch (_: Exception) {
        findNavController().popBackStack()
    }

    companion object {
        private const val TIME_MILLIS = 2000L
    }
}