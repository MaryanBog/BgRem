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
import com.compose.presentation.background.BackgroundViewModel
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.YourBgImageScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class YourBgImageFragment : Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()
    private val backgroundViewModel: BackgroundViewModel by activityViewModels()
    private val yourBgViewModel: YourBgViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                BgRemTheme {
                    YourBgImageScreen(
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
                    is YourBgAction.AddImage -> {
                        handleSelectedImage(action.uri)
                        backgroundViewModel.yourBgType = "image"
                        backgroundViewModel.imageUri = action.uri
                    }
                    else -> {}
                }
            }
        }
    }

    private fun handleSelectedImage(uri: Uri?) = try {
        with(requireContext()) {
            val fileStream = uri?.let { contentResolver.openInputStream(it) }
            val mimeType = uri?.let { contentResolver.getType(it) }
            backgroundViewModel.yourBgType = "image"
            removeViewModel.getMyBgImageInfo(mimeType, fileStream)
            findNavController().popBackStack()
        }
    } catch (_: Exception) {
        findNavController().popBackStack()
    }
}