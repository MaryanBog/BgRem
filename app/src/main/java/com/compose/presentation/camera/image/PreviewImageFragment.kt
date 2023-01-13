package com.compose.presentation.camera.image

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bgrem.app.R
import com.compose.presentation.gallery.image.ImageAction
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.PreviewImageScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreviewImageFragment : Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            lifecycleScope.launchWhenStarted {
                removeViewModel.previewImage.collect { imageUri ->
                    val navController = findNavController()
                    setContent {
                        BgRemTheme {
                            PreviewImageScreen(
                                navController = navController,
                                removeViewModel = removeViewModel,
                                imageUri = imageUri
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            removeViewModel.imageActionState.collect { action ->
                when (action) {
                    ImageAction.StartImage -> {}
                    ImageAction.Error -> {
                        showError(
                            resources.getString(
                                R.string.common_error_something_went_wrong
                            )
                        )
                    }
                    is ImageAction.ImageSelected -> {
                        removeViewModel.onMediaSelected(
                            file = action.file,
                            mediaType = action.mediaType,
                            mimeType = action.mimeType,
                            uri = null
                        )
                        removeViewModel.observeRemovingBackgroundProgress()
                        removeViewModel.removeBg()
                        findNavController().navigate(
                            R.id.action_previewImageFragment_to_loadingComposeFragment
                        )

                    }
                }
            }
        }
    }

    private fun showError(message: String) {
        removeViewModel.onErrorShow(message)
        findNavController().popBackStack()
    }
}