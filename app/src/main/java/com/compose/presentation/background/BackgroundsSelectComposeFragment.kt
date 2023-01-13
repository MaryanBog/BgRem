package com.compose.presentation.background

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.compose.presentation.remove.RemoveViewModel
import com.compose.presentation.welcome.WelcomeComposeViewModel
import com.compose.screen.SelectBackgroundScreen
import com.compose.ui.theme.BgRemTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BackgroundsSelectComposeFragment : Fragment() {

    private val backgroundsViewModel: BackgroundViewModel by activityViewModels()
    private val welcomeComposeViewModel: WelcomeComposeViewModel by activityViewModels()
    private val removeViewModel: RemoveViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        backgroundsViewModel.getVideoCategories()
        backgroundsViewModel.getImageCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    removeViewModel.task.observe(viewLifecycleOwner) { task ->
                        backgroundsViewModel.bg.observe(viewLifecycleOwner) { background ->
                            setContent {
                                val navController = findNavController()
                                BgRemTheme {
                                    SelectBackgroundScreen(
                                        navController,
                                        backgroundsViewModel,
                                        welcomeComposeViewModel,
                                        background,
                                        task,
                                        removeViewModel
                                    )
                                }
                            }

                        }
                    }

                }
            }
        }
    }
}
