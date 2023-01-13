package com.compose.presentation.remove

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.compose.presentation.background.BackgroundViewModel
import com.compose.presentation.welcome.WelcomeComposeViewModel
import com.compose.screen.RemoveScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RemoveComposeFragment : Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()
    private val welcomeComposeViewModel: WelcomeComposeViewModel by activityViewModels()
    private val backgroundViewModel: BackgroundViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        welcomeComposeViewModel.observeBackColor()
        backgroundViewModel.getImageIdsFromStore()
        backgroundViewModel.getVideoIdsFromStore()
        backgroundViewModel.getBgEmpty()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if (backgroundViewModel.favoritesImages.isEmpty()) {
            backgroundViewModel.getFavoritesImagesForFoldersIfListEmpty()
            backgroundViewModel.putDefualtIdsImagesToList()
        } else {
            backgroundViewModel.getFavoritesImagesForFolders()
        }

        if (backgroundViewModel.favoritesVideos.isEmpty()) {
            backgroundViewModel.getFavoritesVideosForFoldersIfListEmpty()
            backgroundViewModel.putDefualtIdsVideosToList()
        } else {
            backgroundViewModel.getFavoritesVideosForFolders()
        }


        return ComposeView(requireContext()).apply {
            lifecycleScope.launchWhenStarted {
                removeViewModel.errorMessage.collect { errorMessage ->
                    setContent {
                        val navController = findNavController()
                        BgRemTheme {
                            RemoveScreen(
                                navController = navController,
                                errorMessage = errorMessage,
                                removeViewModel = removeViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}