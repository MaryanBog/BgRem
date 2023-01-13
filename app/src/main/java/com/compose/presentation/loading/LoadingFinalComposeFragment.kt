package com.compose.presentation.loading

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.compose.presentation.remove.RemoveViewModel
import com.compose.screen.LoadingFinalScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoadingFinalComposeFragment:Fragment() {

    private val removeViewModel: RemoveViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
        removeViewModel.state.observe(viewLifecycleOwner) { progress ->
                setContent {
                    val navController = findNavController()
                    BgRemTheme {
                        LoadingFinalScreen(
                            navController = navController,
                            progress = progress,
                            removeViewModel = removeViewModel)
                    }
                }
            }
        }
    }
}
