package com.compose.presentation.background

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.compose.data.models.BackgroundType
import com.compose.presentation.welcome.WelcomeComposeViewModel
import com.compose.screen.FoldersScreen
import com.compose.ui.theme.BgRemTheme
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FoldersBottomSheetComposeFragment : BottomSheetDialogFragment() {

    private val backgroundsViewModel: BackgroundViewModel by activityViewModels()
    private val welcomeComposeViewModel: WelcomeComposeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = ComposeView(requireContext())
        val navController = findNavController()

        if (backgroundsViewModel.backgroundType == BackgroundType.IMAGE) {
                view.setContent {
                    BgRemTheme {
                        FoldersScreen(
                            navController = navController,
                            viewModel = backgroundsViewModel,
                            welcomeViewModel = welcomeComposeViewModel,
                        )
                    }
                }
        } else if (backgroundsViewModel.backgroundType == BackgroundType.VIDEO) {
                view.setContent {
                    BgRemTheme {
                        FoldersScreen(
                            navController = navController,
                            viewModel = backgroundsViewModel,
                            welcomeViewModel = welcomeComposeViewModel
                        )
                    }
                }
        }


        return view
    }
}
